package com.financia.exchange.engine;

import com.alibaba.fastjson.JSON;
import com.financia.common.core.enums.BusinessSubType;
import com.financia.common.core.utils.SpringUtils;
import com.financia.exchange.*;
import com.financia.exchange.feign.client.RemoteMemberWalletService;
import com.financia.exchange.handler.MarketHandler;
import com.financia.exchange.service.*;
import com.financia.exchange.util.GeneratorUtil;
import com.financia.exchange.websocket.ExchangePushJob;
import com.financia.superleverage.SuperContractCoin;
import com.financia.superleverage.SuperContractOrderEntrust;
import com.financia.swap.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.util.*;

import static com.financia.exchange.AssetRecordReasonEnum.CONTRACT_FORCE_CLOSE_EXCHANGE_LOSS;

/**
 * 合约撮合引擎
 */

public class ContractCoinMatch {

    private Logger logger = LoggerFactory.getLogger(ContractCoinMatch.class);

    private String symbol;                                           // 交易对：BTC/USDT
    private String baseSymbol;                                       // 基币：USDT
    private String coinSymbol;                                       // 币种：BTC
    private CoinThumb thumb;                                         // 交易对行情
    private LinkedList<ContractTrade> lastedTradeList;               // 最新成交明细
    private int lastedTradeListSize = 50;

    private long lastUpdateTime = 0L;                                // 上次价格更新时间（主要用于控制价格刷新周期，因为websokcet获取的价格变化较快）
    private boolean isTriggerComplete = true;                        // 价格刷新是否完成，触发委托及爆仓
    private BigDecimal nowPrice = BigDecimal.ZERO;                   // 当前最新价格

    private SuperContractCoinService superContractCoinService;                  // 合约币种服务

    private SuperContractOrderEntrustService contractOrderEntrustService;  // 合约委托单服务
    private SuperMemberTransactionService memberTransactionService;
    private MemberSuperContractWalletService memberContractWalletService;
    private MemberRechargeRecordService memberRechargeRecordService;
    private SuperContractCoin contractCoin;

    private List<SuperContractOrderEntrust> contractOrderEntrustList = new ArrayList<>();      // 委托列表(计划委托)
    private Map<Long,MemberSuperContractWallet> memberContractWalletList = new HashMap<>();      // 用户仓位信息


    private List<MarketHandler> handlers;                             // 行情、概要等处理者
    private ExchangePushJob exchangePushJob;                          // 推送任务

    //卖盘盘口信息
    private TradePlate sellTradePlate;
    //买盘盘口信息
    private TradePlate buyTradePlate;

    private boolean isStarted = false;                                // 是否启动完成（用于初始化时，获取一些数据库未处理的订单的，如果没获取完，不允许处理）

    private LinkedList<SuperContractOrderEntrust> openOrderList = new LinkedList<SuperContractOrderEntrust>(); // 开仓订单
    private LinkedList<SuperContractOrderEntrust> closeOrderList = new LinkedList<SuperContractOrderEntrust>(); // 平仓订单

    private LinkedList<SuperContractOrderEntrust> openOrderSpotList = new LinkedList<SuperContractOrderEntrust>(); // 开仓止盈止损订单
    private LinkedList<SuperContractOrderEntrust> closeOrderSpotList = new LinkedList<SuperContractOrderEntrust>(); // 平仓止盈止损订单


    MemberAssetRecordService assetRecordService;

    public void setAssetRecordService(MemberAssetRecordService assetRecordService) {
        this.assetRecordService = assetRecordService;
    }

    private MemberBusinessWalletService memberBusinessWalletService;

    public void setMemberWalletService(RemoteMemberWalletService memberWalletService) {
        this.memberWalletService = memberWalletService;
    }

    public void setMemberBusinessWalletService(MemberBusinessWalletService memberBusinessWalletService) {
        this.memberBusinessWalletService = memberBusinessWalletService;
    }

    /**
     * 构造函数
     *
     * @param symbol
     */
    public ContractCoinMatch(String symbol) {
        this.symbol = symbol;
        this.coinSymbol = symbol.split("/")[0];
        this.baseSymbol = symbol.split("/")[1];
        this.handlers = new ArrayList<>();
        this.lastedTradeList = new LinkedList<>();
        this.buyTradePlate = new TradePlate(symbol, ExchangeOrderDirection.BUY);
        this.sellTradePlate = new TradePlate(symbol, ExchangeOrderDirection.SELL);
        // 初始化行情
        this.initializeThumb();
    }

    public void trade(SuperContractOrderEntrust order) throws ParseException {
        if (!this.isStarted) {
            return;
        }
        if (!symbol.equalsIgnoreCase(order.getSymbol())) {
            logger.info("unsupported symbol,coin={},base={}", order.getCoinSymbol(), order.getBaseSymbol());
            return;
        }

        logger.info("current type is {}",order.getType());
        if (order.getEntrustType() == ContractOrderEntrustType.OPEN) { // 开仓
            if (order.getType() == ContractOrderType.MARKET_PRICE) { // 市价，直接成交
                this.dealOpenOrder(order);
            } else if (order.getType() == ContractOrderType.LIMIT_PRICE) { // 限价
                if (order.getDirection() == ContractOrderDirection.BUY && order.getEntrustPrice().compareTo(nowPrice) >= 0
                        || order.getDirection() == ContractOrderDirection.SELL && order.getEntrustPrice().compareTo(nowPrice) <= 0) { // 出价高于当前价，直接成交
                    this.dealOpenOrder(order);
                } else {
                    // 放入列表中
                    synchronized (openOrderList) {
                        logger.info("开仓订单进入监控队列");
                        openOrderList.addLast(order);
                    }
                }
            } else if (order.getType() == ContractOrderType.SPOT_LIMIT) { // 计划委托
                synchronized (openOrderSpotList) {
                    logger.info("开仓计划委托订单进入监控队列");
                    openOrderSpotList.add(order);
                }
            }
        } else if (order.getEntrustType() == ContractOrderEntrustType.CLOSE) { // 平仓
            if (order.getType() == ContractOrderType.MARKET_PRICE) { // 市价，直接成交
                this.dealCloseOrder(order);
            } else if (order.getType() == ContractOrderType.LIMIT_PRICE) { // 限价
                if (order.getDirection() == ContractOrderDirection.BUY && order.getEntrustPrice().compareTo(nowPrice) > 0
                        || order.getDirection() == ContractOrderDirection.SELL && order.getEntrustPrice().compareTo(nowPrice) < 0) { // 出价高于当前价，直接成交
                    this.dealCloseOrder(order);
                } else {
                    // 放入列表中
                    synchronized (closeOrderList) {
                        logger.info("平仓订单进入监控队列");
                        closeOrderList.addLast(order);
                    }
                }
            } else if (order.getType() == ContractOrderType.SPOT_LIMIT) { // 计划委托
                synchronized (closeOrderSpotList) {
                    logger.info("平仓计划委托订单进入监控队列");
                    closeOrderSpotList.add(order);
                }
            }
        } else if(order.getEntrustType()==ContractOrderEntrustType.ADD){
            MemberSuperContractWallet wallet=getWallet(order.getMemberId(), order.getContractId(),order.getContractOrderEntrustId());
            this.addOrder(wallet,order);
        }else if(order.getEntrustType()==ContractOrderEntrustType.SUB){
            MemberSuperContractWallet wallet=getWallet(order.getMemberId(), order.getContractId(),order.getContractOrderEntrustId());
            this.subOrder(wallet,order);
        }else if(order.getEntrustType()==ContractOrderEntrustType.STOP_LOSS||order.getEntrustType()==ContractOrderEntrustType.STOP_PROFIT){
            MemberSuperContractWallet wallet=getWallet(order.getMemberId(), order.getContractId(),order.getContractOrderEntrustId());
            this.setStopLossPrice(wallet,order);
            this.setStopProfitPrice(wallet,order);

            order.setWalletId(wallet.getId());
            order.setCurrentPrice(nowPrice);
            order.setTriggeringTime(System.currentTimeMillis());

            order.setTriggeringTime(System.currentTimeMillis());
        }
        contractOrderEntrustService.saveOrUpdate(order);
    }

    private MemberSuperContractWallet getWallet(Long memberId,Long contractId,String entrustOrder){

        MemberSuperContractWallet wallet;
        if(StringUtils.isEmpty(entrustOrder)){
           throw new RuntimeException("Entrust order can not be empty");
        }else{
            wallet=memberContractWalletService.findByMemberIdAndContractCoin(memberId, contractId,entrustOrder);
           logger.info("member id is :{}, wallet is :{}",memberId,wallet);
            memberContractWalletList.put(wallet.getId(),wallet);
        }

        return wallet;

    }

    /**
     * 资金转移
     * 收取频次：00:00 UTC、08:00 UTC、16:00 UTC 收取（只有在交易者于预定收取资金费用时间点尚有任何方向持仓的情况下，才需支付相应的资金费用。如果交易者当时无任何仓位，将不需支付任何资金费用）
     *
     * 资金金额=持仓价值 x 资金费率
     *
     *
     *
     * 资金费率：在后台填写
     *
     *
     *
     * 持仓价值=市价*持仓数量
     * */

    public void transferAsset(){
        synchronized (memberContractWalletList) {
            Iterator<MemberSuperContractWallet> walletIterator = memberContractWalletList.values().iterator();
            while (walletIterator.hasNext()) {
                MemberSuperContractWallet wallet = walletIterator.next();
                if (wallet == null) {
                    continue;
                }

                if(wallet.getCoinBuyPrincipalAmount().compareTo(BigDecimal.ZERO)<=0){
                    continue;
                }

                BigDecimal feePercent=contractCoin.getFeePercent();
                BigDecimal value=feePercent.multiply(wallet.getUsdtBuyPosition()).multiply(nowPrice);
                SuperContractOrderEntrust entrust=new SuperContractOrderEntrust();
                entrust.setEntrustType(ContractOrderEntrustType.FUNDING_FEE);

                if(feePercent.compareTo(BigDecimal.ZERO)>0){

                    memberContractWalletService.decreaseUsdtPosition(wallet.getId(),value);
                    entrust.setValue(value.negate());
                    //更新缓存钱包数据
                    wallet.setUsdtBuyPrincipalAmount(wallet.getUsdtBuyPrincipalAmount().subtract(value));


                }else{

                    memberContractWalletService.increaseUsdtPosition(wallet.getId(),value);
                    entrust.setValue(value);
                    //更新缓存钱包数据
                    wallet.setUsdtBuyPrincipalAmount(wallet.getUsdtBuyPrincipalAmount().add(value));

                }

                entrust.setContractOrderEntrustId(GeneratorUtil.getOrderId("SUPER_CE"));

                entrust.setStatus(ContractOrderEntrustStatus.ENTRUST_SUCCESS);

                contractOrderEntrustService.saveOrUpdate(entrust);

                addTransferOrder(wallet.getMemberId(),value,wallet.getSymbol(),"",AssetRecordTypeEnum.SUPER_LEVER_TRADE,AssetRecordReasonEnum.CONTRACT_ASSET_TRANSFER);

            }
        }
    }

    /**
     * 添加客户资金变动记录
     * */
    public void addTransferOrder(Long memberId,BigDecimal val,String symbol,String order, AssetRecordTypeEnum typeEnum,AssetRecordReasonEnum recordReasonEnum){


        MemberAssetRecord memberAssetRecord=new MemberAssetRecord();
//
        memberAssetRecord.setMemberId(memberId);
        memberAssetRecord.setAssetType(typeEnum);
        memberAssetRecord.setComment(recordReasonEnum);
        memberAssetRecord.setAmount(val);
        memberAssetRecord.setSymbol(symbol);
        memberAssetRecord.setEntrustOrder(order);

        boolean res=assetRecordService.save(memberAssetRecord);

        logger.info("save res is {}",res);

    }

    private AssetRecordReasonEnum entrustType2TransactionType(ContractOrderEntrustType type){
        switch (type){
            case OPEN:
                return AssetRecordReasonEnum.CONTRACT_OPEN;
            case CLOSE:
                return AssetRecordReasonEnum.CONTRACT_CLOSE;
            case ADD:
                return AssetRecordReasonEnum.CONTRACT_ADD;
            case SUB:
                return AssetRecordReasonEnum.CONTRACT_SUB;
            case STOP_LOSS:
                return AssetRecordReasonEnum.CONTRACT_STOP_LOSS;
            case STOP_PROFIT:
                return AssetRecordReasonEnum.CONTRACT_STOP_PROFIT;
            case FUNDING_FEE:
                return AssetRecordReasonEnum.CONTRACT_ASSET_TRANSFER;
        }

        return AssetRecordReasonEnum.CONTRACT_ADD;
    }


    /**
     * 加仓
     */
    @Transactional
    public void addOrder(MemberSuperContractWallet wallet, SuperContractOrderEntrust order) {

        logger.info("begin to add order :{} \n wallet is :{}",order,wallet);

        //判断加仓和持仓的方向。加仓只能加同方向的仓
        if((wallet.getUsdtBuyPosition().compareTo(BigDecimal.ZERO)<0&&order.getDirection()==ContractOrderDirection.BUY)
            ||(wallet.getUsdtBuyPosition().compareTo(BigDecimal.ZERO)>0&&order.getDirection()==ContractOrderDirection.SELL)
        ){
            logger.info("加仓方向与持仓方向相反，错误。order:{} ,\n wallet:{}",order,wallet);
//            throw new RuntimeException("加仓方向只能与持仓方向相同");
            return;
        }

        MemberBusinessWallet businessWallet=memberBusinessWalletService.getMemberBusinessWalletByMemberId(wallet.getMemberId());
        //扣除冻结金额
        logger.info("decrease money is {} and order pri {}",businessWallet,order.getPrincipalAmount());
        memberBusinessWalletService.decreaseFreezeBalance(businessWallet.getId(),order.getPrincipalAmount(), businessWallet.getMoney(), order.getContractOrderEntrustId(), order.getEntrustType()==ContractOrderEntrustType.OPEN?BusinessSubType.SUPER_OPEN_SUCCESS:BusinessSubType.SUPER_ADD_SUCCESS,"扣除保证金");


        //扣除保证金
//        memberContractWalletService.decreaseUsdtBalance(wallet.getId(),order.getPrincipalAmount());
        wallet.setUsdtBalance(wallet.getUsdtBalance().subtract(order.getPrincipalAmount()));

        //追加保证金
        memberContractWalletService.increaseUsdtBuyPrincipalAmountWithFrozen(wallet.getId(), order.getPrincipalAmount());
        wallet.setUsdtBuyPrincipalAmount(wallet.getUsdtBuyPrincipalAmount().add(order.getPrincipalAmount()));

        memberContractWalletService.increaseCloseFee(wallet.getId(),order.getCloseFee());
        wallet.setCloseFee(wallet.getCloseFee().add(order.getCloseFee()));


        //开仓费计入盈亏
        memberContractWalletService.increaseUsdtLoss(wallet.getId(),order.getOpenFee());
        wallet.setUsdtLoss(wallet.getUsdtLoss().add(order.getOpenFee()));

        if(order.getEntrustType()==ContractOrderEntrustType.ADD||wallet.getUsdtBuyPrice().compareTo(BigDecimal.ZERO)<=0){
            //更新持仓均价
            BigDecimal avaPrice=BigDecimal.ZERO;

            if(order.getDirection()==ContractOrderDirection.BUY){
                //如果是买多
                avaPrice = wallet.getUsdtBuyPosition().multiply(wallet.getUsdtBuyPrice()).add(order.getPrincipalAmount().multiply(wallet.getUsdtBuyLeverage())).divide(wallet.getUsdtBuyPosition().add(order.getVolume()), 8, BigDecimal.ROUND_DOWN);

            }else{
                //如果是买空，则是绝对值
                avaPrice = wallet.getUsdtBuyPosition().abs().multiply(wallet.getUsdtBuyPrice()).add(order.getPrincipalAmount().multiply(wallet.getUsdtBuyLeverage())).divide(wallet.getUsdtBuyPosition().abs().add(order.getVolume().abs()), 8, BigDecimal.ROUND_DOWN);

            }

            logger.info("current ava price is {}, and buy position is {}",avaPrice,wallet.getUsdtBuyPosition());

            memberContractWalletService.updateUsdtBuyPrice(wallet.getId(),avaPrice);
            wallet.setUsdtBuyPrice(avaPrice);
        }

        memberContractWalletService.increaseUsdtPosition(wallet.getId(),order.getVolume());
        wallet.setUsdtBuyPosition(wallet.getUsdtBuyPosition().add(order.getVolume()));

        memberContractWalletService.setBuyOrSell(wallet.getId(),wallet.getUsdtBuyPosition().compareTo(BigDecimal.ZERO)>=0?0:1);

        this.addTransferOrder(wallet.getMemberId(),order.getPrincipalAmount(),order.getBaseSymbol(),order.getContractOrderEntrustId(),AssetRecordTypeEnum.SUPER_LEVER_TRADE,entrustType2TransactionType(order.getEntrustType()));

        this.addTransferOrder(wallet.getMemberId(),order.getVolume().abs(),order.getCoinSymbol(),order.getContractOrderEntrustId(),AssetRecordTypeEnum.SUPER_LEVER_TRADE,entrustType2TransactionType(order.getEntrustType()));

        if(order.getOpenFee().compareTo(BigDecimal.ZERO)>0){
            this.addTransferOrder(wallet.getMemberId(),order.getOpenFee(),order.getBaseSymbol(),order.getContractOrderEntrustId(),AssetRecordTypeEnum.SUPER_LEVER_TRADE,AssetRecordReasonEnum.OPEN_FEE);
        }

        //更新订单委托状态
        order.setStatus(ContractOrderEntrustStatus.ENTRUST_SUCCESS); // 委托状态：已成交
        order.setTriggeringTime(System.currentTimeMillis());
        order.setTradedVolume(order.getVolume()); // 设置已交易数量
        order.setTradedPrice(order.getCurrentPrice());
        order.setWalletId(wallet.getId());
        order.setAvaPrice(wallet.getUsdtBuyPrice());
        order.setForceClosePrice(wallet.getForceClosePrice());
        contractOrderEntrustService.saveOrUpdate(order);

        //计算强制平仓价
        this.calcuForceClosePrice(wallet, order);

    }

      @Autowired
      RemoteMemberWalletService memberWalletService;

    /**
     * 减仓
     */
    @Transactional
    public void subOrder(MemberSuperContractWallet wallet, SuperContractOrderEntrust order) {

        //持仓数量还是正数，持仓方向用 SELL和BUY区分
        order.setVolume(order.getVolume().abs());
        if (order.getVolume().compareTo(wallet.getUsdtBuyPosition().abs()) > 0) {
            logger.info("减仓的量不能大于当前持仓的量");
            return;
//            throw new RuntimeException("减仓的量不能大于当前持仓的量");
        }


        BigDecimal userHold = wallet.getUsdtBuyPosition();
        //计算盈亏，大于0 是盈利，小于0是亏损
        BigDecimal subValue=BigDecimal.ZERO;
        if(wallet.getUsdtBuyPosition().compareTo(BigDecimal.ZERO)>0){
            //如果是多仓，则是当前价格减去开仓均价
            subValue = order.getVolume().multiply(nowPrice.subtract(wallet.getUsdtBuyPrice()));
        }else if(wallet.getUsdtBuyPosition().compareTo(BigDecimal.ZERO)<0){
            //如果是空仓，则是开仓均价减去当前价格
            subValue = order.getVolume().multiply(nowPrice.subtract(wallet.getUsdtBuyPrice()).negate());
        }else{
            logger.info("减仓不可能在仓位为0的时候减");
            return;
//            throw new RuntimeException("减仓不可能在仓位为0的时候减");
        }

        logger.info("current price is {}, buy price is {}, profit is {}",nowPrice,wallet.getUsdtBuyPrice(),subValue);
        order.setProfitAndLoss(subValue);


        BigDecimal subRate = order.getVolume().divide(userHold, 8, RoundingMode.DOWN).abs();

        logger.info("sub rate is {}",subRate);

        BigDecimal subPrincipal = wallet.getUsdtBuyPrincipalAmount().multiply(subRate);

        order.setPrincipalAmount(subPrincipal);

        logger.info("sub principal is {}",subPrincipal);

        //平仓手续费
        BigDecimal closeFee = wallet.getCloseFee().multiply(subRate);

        logger.info("close fee is {}",closeFee);

        BigDecimal userBackAsset; //归还本金

        userBackAsset = subPrincipal.add(subValue).subtract(closeFee);

        logger.info("user back asset is {}",userBackAsset);

        order.setBackAsset(userBackAsset);

        MemberBusinessWallet memberBusinessWallet = memberBusinessWalletService.getMemberBusinessWalletByMemberId(wallet.getMemberId());


        //将利润返还给余额
        if(userBackAsset.compareTo(BigDecimal.ZERO)>0){

            memberBusinessWalletService.increaseBalance(memberBusinessWallet.getId(),userBackAsset,memberBusinessWallet.getMoney().add(userBackAsset),order.getContractOrderEntrustId(),order.getEntrustType()==ContractOrderEntrustType.CLOSE?BusinessSubType.SUPER_CLOSE_SUCCESS:BusinessSubType.SUPER_SUB_SUCCESS,"盈利计算");

            this.addTransferOrder(wallet.getMemberId(),userBackAsset,order.getBaseSymbol(),order.getContractOrderEntrustId(),AssetRecordTypeEnum.SUPER_LEVER_TRADE,entrustType2TransactionType(order.getEntrustType()));

        }



        //减少持仓数量和保证金
        if(wallet.getUsdtBuyPosition().compareTo(BigDecimal.ZERO)>0){
            //如果是多仓
            memberContractWalletService.decreasePrincipalAmount(wallet.getId(),subPrincipal);
            memberContractWalletService.decreaseUsdtPosition(wallet.getId(),order.getVolume());

            wallet.setUsdtBuyPrincipalAmount(wallet.getUsdtBuyPrincipalAmount().subtract(subPrincipal));
            wallet.setUsdtBuyPosition(wallet.getUsdtBuyPosition().subtract(order.getVolume()));


            this.addTransferOrder(wallet.getMemberId(),order.getPrincipalAmount().negate(),order.getBaseSymbol(),order.getContractOrderEntrustId(),AssetRecordTypeEnum.SUPER_LEVER_TRADE,AssetRecordReasonEnum.CONTRACT_SUB_PRINCIPLE);

            this.addTransferOrder(wallet.getMemberId(),order.getVolume().abs().negate(),order.getCoinSymbol(),order.getContractOrderEntrustId(),AssetRecordTypeEnum.SUPER_LEVER_TRADE,AssetRecordReasonEnum.CONTRACT_SUB_COIN);

        }else if(wallet.getUsdtBuyPosition().compareTo(BigDecimal.ZERO)<0){
            //如果是空仓
            memberContractWalletService.decreasePrincipalAmount(wallet.getId(),subPrincipal);
            memberContractWalletService.decreaseUsdtPosition(wallet.getId(),order.getVolume().negate());

            wallet.setUsdtBuyPrincipalAmount(wallet.getUsdtBuyPrincipalAmount().subtract(subPrincipal));
            wallet.setUsdtBuyPosition(wallet.getUsdtBuyPosition().subtract(order.getVolume().negate()));
        }

        //减少剩余平仓手续费
        memberContractWalletService.decreaseCloseFee(wallet.getId(), order.getCloseFee());
        wallet.setCloseFee(wallet.getCloseFee().subtract(order.getCloseFee()));

        //增加交易收取的平仓手续费
        superContractCoinService.increaseTotalCloseFee(wallet.getContractId(), closeFee);
        //将平仓手续费计入亏损
        memberContractWalletService.increaseUsdtLoss(wallet.getId(),closeFee);
        wallet.setUsdtLoss(wallet.getUsdtLoss().add(closeFee));



        //如果是盈利
        if(subValue.compareTo(BigDecimal.ZERO)>0){
            superContractCoinService.increaseTotalProfit(wallet.getContractId(),subValue);
            //统计用户盈利
            memberContractWalletService.increaseUsdtProfit(wallet.getId(),subValue);
            wallet.setUsdtProfit(wallet.getUsdtProfit().add(subValue));
        }else if(subValue.compareTo(BigDecimal.ZERO)<0){

            if(subValue.add(wallet.getUsdtBuyPrincipalAmount()).compareTo(BigDecimal.ZERO)<0){
                //交易所损失的资产
                BigDecimal exchangeLoss=wallet.getUsdtBuyPrincipalAmount().add(subValue).negate();
                subValue=wallet.getUsdtBuyPrincipalAmount().negate();
                memberContractWalletService.increaseExchangeLoss(wallet.getId(),exchangeLoss);

                this.addTransferOrder(0L,exchangeLoss.negate(),order.getBaseSymbol(),order.getContractOrderEntrustId(),AssetRecordTypeEnum.SUPER_LEVER_TRADE,CONTRACT_FORCE_CLOSE_EXCHANGE_LOSS);

            }

            //如果是亏损
            superContractCoinService.increaseTotalLoss(wallet.getContractId(),subValue.negate());
            //统计用户损失
            memberContractWalletService.increaseUsdtLoss(wallet.getId(),subValue.negate());
            wallet.setUsdtLoss(wallet.getUsdtLoss().add(subValue.negate()));
        }




        //更新订单委托状态
        order.setStatus(ContractOrderEntrustStatus.ENTRUST_SUCCESS); // 委托状态：已成交
        order.setTriggeringTime(System.currentTimeMillis());
        order.setTradedVolume(order.getVolume()); // 设置已交易数量
        order.setTradedPrice(order.getCurrentPrice());
        order.setAvaPrice(wallet.getUsdtBuyPrice());
        order.setForceClosePrice(wallet.getForceClosePrice());
        order.setWalletId(wallet.getId());

        order.setCurrentPrice(nowPrice);

        if(order.getEntrustType().equals(ContractOrderEntrustType.OPEN)
            ||order.getEntrustType().equals(ContractOrderEntrustType.SUB)
                ||order.getEntrustType().equals(ContractOrderEntrustType.ADD)
        ) {
            //计算强制平仓价
            this.calcuForceClosePrice(wallet, order);
        }else if(order.getEntrustType().equals(ContractOrderEntrustType.CLOSE)||order.getEntrustType().equals(ContractOrderEntrustType.STOP_PROFIT)||order.getEntrustType()==ContractOrderEntrustType.STOP_LOSS){
            memberContractWalletService.setForceClose(wallet.getId(),BigDecimal.ZERO);
            wallet.setCloseFee(BigDecimal.ZERO);
            memberContractWalletService.updateUsdtBuyPrice(wallet.getId(),BigDecimal.ZERO);
            wallet.setUsdtBuyPrice(BigDecimal.ZERO);
            memberContractWalletService.decreaseOpenFee(wallet.getId(),wallet.getOpenFee());
            wallet.setOpenFee(BigDecimal.ZERO);


            wallet.setStopLossPrice(BigDecimal.ZERO);
            wallet.setStopProfitPrice(BigDecimal.ZERO);

            memberContractWalletService.setStopLossPrice(wallet.getId(),BigDecimal.ZERO);
            memberContractWalletService.setStopProfitPrice(wallet.getId(),BigDecimal.ZERO);
        }


    }
    @Transactional
    public void dealOpenOrder(SuperContractOrderEntrust order){
        logger.info("成交开仓订单");
        MemberSuperContractWallet memberContractWallet = getWallet(order.getMemberId(), contractCoin.getId(),order.getContractOrderEntrustId());
        //从余额中扣除持仓金额
        memberContractWalletService.decreaseUsdtBalance(memberContractWallet.getId(),order.getCloseFee());
        //记录新增开仓手续费
        memberContractWalletService.increaseOpenFee(memberContractWallet.getId(),order.getOpenFee());

        logger.error("begin to decrease freeze, wallet is {}",memberBusinessWalletService);
        MemberBusinessWallet businessWallet=memberBusinessWalletService.getMemberBusinessWalletByMemberId(memberContractWallet.getMemberId());
        memberBusinessWalletService.decreaseFreezeBalance(businessWallet.getId(),order.getCloseFee(),businessWallet.getMoney(),order.getContractOrderEntrustId(),BusinessSubType.SUPER_OPEN_SUCCESS,"开仓手续费");
        logger.error("has to decrease freeze");

        //交易对手续费增加
        superContractCoinService.increaseTotalOpenFee(contractCoin.getId(),order.getOpenFee());

        BigDecimal userHold=memberContractWallet.getUsdtBuyPosition();//获取当前持仓余额，正数代表多仓，负数代表空仓

        //因为有委托单存在，所以，需要根据传入金额计算持仓数量。
        BigDecimal leverage = memberContractWallet.getUsdtBuyLeverage();

        //计算持仓数量，持仓数量=购买价格*杠杆数/当前价格*持仓方向 （1 买多，-1 买空）
        order.setVolume(
                order.getPrincipalAmount().multiply(leverage)
                        .divide(nowPrice,8,RoundingMode.DOWN)
                        .multiply(order.getDirection().equals(ContractOrderDirection.BUY)?BigDecimal.ONE:BigDecimal.ONE.negate())

        );

        logger.info("open userHold is:{} , volume is {}",userHold,order.getVolume());


        this.addOrder(memberContractWallet,order);

        this.setStopLossPrice(memberContractWallet,order);
        this.setStopProfitPrice(memberContractWallet,order);

    }


    /**
     * 设置止盈价
     * */
    @Transactional
    public void setStopProfitPrice(MemberSuperContractWallet wallet,SuperContractOrderEntrust entrust){
        if(entrust.getStopProfitPrice().compareTo(BigDecimal.ZERO)>0){
            logger.info("设置止盈：{}",entrust.getStopProfitPrice());
            wallet.setStopProfitPrice(entrust.getStopProfitPrice());
            memberContractWalletService.setStopProfitPrice(wallet.getId(),entrust.getStopProfitPrice());
        }
    }

    /**
     * 设置止损价
     * */
    @Transactional
    public void setStopLossPrice(MemberSuperContractWallet wallet,SuperContractOrderEntrust entrust){
        if(entrust.getStopLossPrice().compareTo(BigDecimal.ZERO)>0){
            logger.info("设置止损：{}， wallet is {}",entrust.getStopLossPrice(),wallet);
            wallet.setStopLossPrice(entrust.getStopLossPrice());
            memberContractWalletService.setStopLossPrice(wallet.getId(),entrust.getStopLossPrice());
        }
    }


    public void calcuForceClosePrice(MemberSuperContractWallet memberContractWallet, SuperContractOrderEntrust order) {
        //计算强平价
        /**
         *
         * 强平价=开仓均价-开仓均价*风险率
         * 风险率=用户拥有的资产/交易所拥有的资产（均指该笔持仓中的资产）
         * 用户拥有的资产=（剩余持仓本金-未扣除平仓手续费）/开仓价
         * 交易所拥有的资产：
         * 交易所拥有的资产=总资产-用户资产
         * */


        BigDecimal userAsset = memberContractWallet.getUsdtBuyPrincipalAmount().subtract(order.getCloseFee());
        BigDecimal exchangeAsset = memberContractWallet.getUsdtBuyPrice().multiply(memberContractWallet.getUsdtBuyPosition()).abs();

//        BigDecimal rate = userAsset.divide(exchangeAsset, 8, RoundingMode.DOWN).subtract(BigDecimal.valueOf(0.0007f));
        BigDecimal rate = userAsset.divide(exchangeAsset, 8, RoundingMode.DOWN);
        BigDecimal forceClose=BigDecimal.ZERO;
        if(memberContractWallet.getUsdtBuyPosition().compareTo(BigDecimal.ZERO)>0){
            forceClose = memberContractWallet.getUsdtBuyPrice().multiply(BigDecimal.ONE.subtract(rate));
        }else{
            forceClose = memberContractWallet.getUsdtBuyPrice().multiply(BigDecimal.ONE.add(rate));
        }

        memberContractWallet.setForceClosePrice(forceClose.setScale(8,RoundingMode.HALF_UP));
        memberContractWalletService.setForceClose(memberContractWallet.getId(),memberContractWallet.getForceClosePrice());


        if(memberContractWallet.getUsdtBuyPosition().compareTo(BigDecimal.ZERO)>0){
            logger.info("强平价, price : {},保证金 {}, 损失 {}",forceClose,memberContractWallet.getUsdtBuyPrincipalAmount(),memberContractWallet.getUsdtBuyPrice().subtract(forceClose).multiply(memberContractWallet.getUsdtBuyPosition()));
        }else{
            logger.info("强平价, price : {},保证金 {} 损失 {}",forceClose,memberContractWallet.getUsdtBuyPrincipalAmount(),memberContractWallet.getUsdtBuyPrice().subtract(forceClose).multiply(memberContractWallet.getUsdtBuyPosition()).negate());
        }


    }

    /**
     * 成交平仓委托
     *平仓本质上是减仓操作，只不过有减多减少的问题。
     * @param order
     */
    @Transactional
    public void dealCloseOrder(SuperContractOrderEntrust order) {
        logger.info("成交平仓委托");


        MemberSuperContractWallet memberContractWallet = getWallet(order.getMemberId(), contractCoin.getId(),order.getContractOrderEntrustId());

        if(order.getEntrustType()==ContractOrderEntrustType.CLOSE){
            order.setVolume(memberContractWallet.getUsdtBuyPosition().abs());
            order.setCloseFee(memberContractWallet.getCloseFee());
        }

        this.subOrder(memberContractWallet,order);

        // 同步最新用户持仓数据
        memberWalletChange(memberContractWallet.getId());
        // 同步最新用户持仓数据
        // syncMemberPosition();
    }

    /**
     * 启动引擎，加载未处理订单
     */
    public void run() {
        logger.info(symbol + " 合约引擎启动，加载数据库订单....");
        contractCoin = superContractCoinService.findBySymbol(symbol);
        if (contractCoin == null) {
            logger.info(contractCoin.getSymbol() + "引擎启动失败，找不到合约交易对");
            return;
        }
        // 数据库查找订单，加载到列表中
        contractOrderEntrustList = contractOrderEntrustService.loadUnMatchOrders(contractCoin.getId());
        if (contractOrderEntrustList != null && contractOrderEntrustList.size() > 0) {
            logger.info(contractCoin.getSymbol() + "加载订单，共计 " + contractOrderEntrustList.size());
            for (SuperContractOrderEntrust item : contractOrderEntrustList) {
                if (item.getEntrustType() == ContractOrderEntrustType.OPEN) { // 开仓单
                    if (item.getType() == ContractOrderType.SPOT_LIMIT) { // 计划委托单（止盈止损单）
                        openOrderSpotList.add(item);
                    } else {
                        openOrderList.add(item);
                    }
                } else { // 平仓单
                    if (item.getType() == ContractOrderType.SPOT_LIMIT) { // 计划委托单（止盈止损单）
                        closeOrderSpotList.add(item);
                    } else {
                        closeOrderList.add(item);
                    }
                }
            }
        }
        // 加载用户持仓信息
        this.syncMemberPosition();
        this.isStarted = true;
    }

    /**
     * 更新盘口（买卖盘，火币Websocket获取到的是20条）
     *
     * @param buyPlateItems
     * @param sellPlateItems
     */
    public void refreshPlate(List<TradePlateItem> buyPlateItems, List<TradePlateItem> sellPlateItems) {
        if (!this.isStarted) {
            return;
        }

        this.buyTradePlate.setItems(new LinkedList<>(buyPlateItems));
        this.sellTradePlate.setItems(new LinkedList<>(sellPlateItems));

        this.exchangePushJob.addPlates(symbol, sellTradePlate);
        this.exchangePushJob.addPlates(symbol, buyTradePlate);

//        logger.info("{} 盘口刷新：买盘大小-{}, 卖盘大小-{}", symbol, buyPlateItems.size(), sellPlateItems.size());
    }

    /**
     * 更新行情
     *
     * @param thumb
     */
    public void refreshThumb(CoinThumb thumb) {
        if (!this.isStarted) {
            return;
        }

        this.thumb.setHigh(thumb.getHigh());
        this.thumb.setLow(thumb.getLow());
        this.thumb.setOpen(thumb.getClose());
        this.thumb.setClose(thumb.getClose());
        this.thumb.setTurnover(thumb.getTurnover());
        this.thumb.setVolume(thumb.getVolume());
        this.thumb.setUsdRate(thumb.getClose());
        // 计算变化（变化金额以及变化比例，其中变化比例会出现负数）
        this.thumb.setChange(thumb.getClose().subtract(thumb.getOpen()));
        if (thumb.getOpen().compareTo(BigDecimal.ZERO) > 0) {
            this.thumb.setChg(this.thumb.getChange().divide(this.thumb.getOpen(), 4, RoundingMode.UP));
        }

//        logger.info("{} 行情刷新：Hight-{}, Low-{}, Open-{}, Close-{}", symbol, thumb.getHigh(), thumb.getLow(), thumb.getOpen(), thumb.getClose());
        // 推送行情
        handleCoinThumb();
    }

    /**
     * 更新价格
     * 更新价格时，涉及到计划委托、止盈止损检测、爆仓检查，有一定耗时操作
     *
     * @param newPrice
     */
    public void refreshPrice(BigDecimal newPrice) {
        // 尚未启动
        if (!this.isStarted) {
            return;
        }
        if(this.isCheckBlashTest){
            return;
        }

        // 上一次任务尚未完成
        if (!isTriggerComplete) {
            return;
        }
        long currentTime = Calendar.getInstance().getTimeInMillis();
        // 控制1秒+更新一次
        if (currentTime - lastUpdateTime > 100) {
            lastUpdateTime = currentTime;

            synchronized (this.nowPrice) {
                // 价格未发生变化，无需继续操作
                if (this.nowPrice.compareTo(newPrice) == 0) {
                    return;
                }
                this.nowPrice = newPrice;
            }
            // 开始检查委托
            isTriggerComplete = false;
            this.process(newPrice);

        }
    }

    /**
     * 更新成交明细
     *
     * @param tradeArrayList
     */
    public void refreshLastedTrade(List<ContractTrade> tradeArrayList) {
        synchronized (lastedTradeList) {
            for (ContractTrade trade : tradeArrayList) {
                if (lastedTradeList.size() > lastedTradeListSize) {
                    this.lastedTradeList.removeLast();
                    this.lastedTradeList.addFirst(trade);
                } else {
                    this.lastedTradeList.addFirst(trade);
                }
            }
            // 添加成交明细
            this.exchangePushJob.addTrades(symbol, tradeArrayList);
        }
    }

    /**
     * 处理委托单
     *
     * @param newPrice
     */
    public void process(BigDecimal newPrice) {

//        logger.info("entrust price refresh : {}, current is {}",newPrice,this.coinSymbol);

//        long startTick = System.currentTimeMillis();
        try{
            this.newBuyBlastCheck(newPrice);
            //止盈止损处理
            this.stopProfitOrLossCheck(newPrice);

            this.processOpenSpotEntrustList(newPrice);     // 2、开仓计划委托处理

            this.processCloseSpotEntrustList(newPrice);    // 3、平仓计划委托处理

            this.processCloseEntrustList(newPrice);        // 4、平仓委托处理

            this.processOpenEntrustList(newPrice);         // 5、开仓委托处理
        }catch (Exception e){
            e.printStackTrace();
        }

        this.isTriggerComplete = true;
//        logger.info("委托处理耗时：{}", System.currentTimeMillis() - startTick);
    }

    /**
     * 止盈止损处理
     * */
    public void stopProfitOrLossCheck(BigDecimal newPrice){
        synchronized (memberContractWalletList) {
            Iterator<MemberSuperContractWallet> walletIterator = memberContractWalletList.values().iterator();
            while (walletIterator.hasNext()) {
                MemberSuperContractWallet wallet = walletIterator.next();
                if (wallet == null) {
                    continue;
                }
                if(wallet.getStopProfitPrice().compareTo(BigDecimal.ZERO)>0){
                    logger.info("wallet list is: {}",wallet);
                }

                if(wallet.getDirection()==ContractOrderDirection.BUY.getCode()&&wallet.getUsdtBuyPosition().compareTo(BigDecimal.ZERO)<0){
                    //如果是买多，同时用户余额为负，则代表过度止盈止损
                    continue;
                }
                if(wallet.getDirection()==ContractOrderDirection.SELL.getCode()&&wallet.getUsdtBuyPosition().compareTo(BigDecimal.ZERO)>0){
                    //如果是买空，同时用户余额为正，则代表过度止盈止损
                    continue;
                }

                //如果是多仓
                if (wallet.getUsdtBuyPosition().compareTo(BigDecimal.ZERO) > 0 ) {
                    //如果当前价格大于止盈价格，则代表可以止盈
                    if (wallet.getStopProfitPrice().compareTo(newPrice) <= 0 && wallet.getStopProfitPrice().compareTo(BigDecimal.ZERO)>0) {
                        logger.info("超级杠杆-多仓止盈 price is:{},wallet is:{}",newPrice,wallet);
                        stopProfit(wallet, newPrice);
                    }else if(wallet.getStopLossPrice().compareTo(newPrice)>=0 && wallet.getStopLossPrice().compareTo(BigDecimal.ZERO)>0){
                        //如果当前价格小于止损价格，则表示止损
                        logger.info("超级杠杆-多仓止损 price is:{},wallet is:{}",newPrice,wallet);
                        stopLoss(wallet,newPrice);
                    }
                }

                if (wallet.getUsdtBuyPosition().compareTo(BigDecimal.ZERO) < 0) {
                    //如果当前价格大于止损价格，则代表止损
                    if (wallet.getStopLossPrice().compareTo(newPrice) >= 0 && wallet.getStopLossPrice().compareTo(BigDecimal.ZERO)>0) {
                        logger.info("空仓止损 price is:{},wallet is:{}",newPrice,wallet);
                        stopLoss(wallet, newPrice);
                    }else if(wallet.getStopProfitPrice().compareTo(newPrice)>=0 && wallet.getStopProfitPrice().compareTo(BigDecimal.ZERO)>0){
                        //若当前价格小于止盈价格，则带遍止盈
                        logger.info("空仓止盈 price is:{},wallet is:{}",newPrice,wallet);
                        stopProfit(wallet,newPrice);
                    }
                }

            }
        }
    }

    public void stopProfit(MemberSuperContractWallet wallet,BigDecimal newPrice){
        //创建止盈止损单

        wallet=getWallet(wallet.getMemberId(),wallet.getContractId(),wallet.getEntrustOrder());
        if(wallet.getUsdtBuyPosition().compareTo(BigDecimal.ZERO)>0&&wallet.getStopProfitPrice().compareTo(newPrice)>0){
            return;
        }
        if(wallet.getUsdtBuyPosition().compareTo(BigDecimal.ZERO)<0 && wallet.getStopProfitPrice().compareTo(newPrice)<0){
            return;
        }


        logger.info("开始进行止盈 {},price {}",wallet,newPrice);


        SuperContractOrderEntrust orderEntrust = new SuperContractOrderEntrust();
        orderEntrust.setContractId(contractCoin.getId()); // 合约ID
        orderEntrust.setMemberId(wallet.getMemberId()); // 用户ID
        orderEntrust.setSymbol(contractCoin.getSymbol()); // 交易对符号
        orderEntrust.setBaseSymbol(contractCoin.getSymbol().split("/")[1]); // 基币/结算币
        orderEntrust.setCoinSymbol(contractCoin.getSymbol().split("/")[0]); // 币种符号
        orderEntrust.setDirection(ContractOrderDirection.SELL); // 平仓方向：平空/平多
        orderEntrust.setContractOrderEntrustId(GeneratorUtil.getOrderId("SUPER_CE"));
        orderEntrust.setVolume(wallet.getUsdtBuyPosition().add(wallet.getUsdtFrozenBuyPosition())); // 平仓张数
        orderEntrust.setTradedVolume(wallet.getUsdtBuyPosition().add(wallet.getUsdtFrozenBuyPosition())); // 已交易数量
        orderEntrust.setTradedPrice(newPrice); // 成交价格
        orderEntrust.setPrincipalUnit("USDT"); // 保证金单位
        orderEntrust.setCreateTime(System.currentTimeMillis()); // 开仓时间
        orderEntrust.setType(ContractOrderType.MARKET_PRICE);
        orderEntrust.setTriggerPrice(nowPrice); // 触发价
        orderEntrust.setEntrustType(ContractOrderEntrustType.CLOSE); // 止盈
        orderEntrust.setCloseOrderType(ContractOrderEntrustCloseType.CLOSE_STOP_PROFIT);
        orderEntrust.setTriggeringTime(System.currentTimeMillis()); // 触发时间，暂时无效
        orderEntrust.setPatterns(wallet.getUsdtPattern()); // 仓位模式
        orderEntrust.setCloseFee(wallet.getCloseFee());
        orderEntrust.setCurrentPrice(newPrice);

        orderEntrust.setEntrustPrice(nowPrice);


        this.subOrder(wallet,orderEntrust);

    }

    public void stopLoss(MemberSuperContractWallet wallet,BigDecimal newPrice){

        wallet=getWallet(wallet.getMemberId(),wallet.getContractId(),wallet.getEntrustOrder());
        if(wallet.getUsdtBuyPosition().compareTo(BigDecimal.ZERO)>0&&wallet.getStopLossPrice().compareTo(newPrice)<0){
            return;
        }
        if(wallet.getUsdtBuyPosition().compareTo(BigDecimal.ZERO)<0 && wallet.getStopLossPrice().compareTo(newPrice)>0){
            return;
        }



        logger.info("开始进行止损 {},price {}",wallet,newPrice);


        SuperContractOrderEntrust orderEntrust = new SuperContractOrderEntrust();
        orderEntrust.setContractId(contractCoin.getId()); // 合约ID
        orderEntrust.setMemberId(wallet.getMemberId()); // 用户ID
        orderEntrust.setSymbol(contractCoin.getSymbol()); // 交易对符号
        orderEntrust.setBaseSymbol(contractCoin.getSymbol().split("/")[1]); // 基币/结算币
        orderEntrust.setCoinSymbol(contractCoin.getSymbol().split("/")[0]); // 币种符号
        orderEntrust.setDirection(ContractOrderDirection.SELL); // 平仓方向：平空/平多
        orderEntrust.setContractOrderEntrustId(GeneratorUtil.getOrderId("SUPER_CE"));
        orderEntrust.setVolume(wallet.getUsdtBuyPosition().add(wallet.getUsdtFrozenBuyPosition())); // 平仓张数
        orderEntrust.setTradedVolume(wallet.getUsdtBuyPosition().add(wallet.getUsdtFrozenBuyPosition())); // 已交易数量
        orderEntrust.setTradedPrice(newPrice); // 成交价格
        orderEntrust.setPrincipalUnit("USDT"); // 保证金单位
        orderEntrust.setCreateTime(System.currentTimeMillis()); // 开仓时间
        orderEntrust.setType(ContractOrderType.MARKET_PRICE);
        orderEntrust.setTriggerPrice(nowPrice); // 触发价
        orderEntrust.setEntrustType(ContractOrderEntrustType.CLOSE); // 止损
        orderEntrust.setTriggeringTime(System.currentTimeMillis()); // 触发时间，暂时无效
        orderEntrust.setCloseOrderType(ContractOrderEntrustCloseType.CLOSE_STOP_LOSS);
        orderEntrust.setPatterns(wallet.getUsdtPattern()); // 仓位模式
        orderEntrust.setCloseFee(wallet.getCloseFee());
        orderEntrust.setCurrentPrice(newPrice);

        orderEntrust.setEntrustPrice(nowPrice);

        orderEntrust.setStatus(ContractOrderEntrustStatus.ENTRUST_SUCCESS);

        this.subOrder(wallet,orderEntrust);
    }


    /**
     * 处理开仓限价委托订单
     *
     * @param newPrice
     */
    public void processOpenEntrustList(BigDecimal newPrice) {
        synchronized (openOrderList) {
            Iterator<SuperContractOrderEntrust> orderIterator = openOrderList.iterator();
            while ((orderIterator.hasNext())) {
                SuperContractOrderEntrust order = orderIterator.next();
//                logger.info("委托订单监听: price :{} , order: {}",nowPrice,order);
                if (order.getDirection() == ContractOrderDirection.BUY) { // 多单
                    if (order.getEntrustPrice().compareTo(newPrice) >= 0) {
                        logger.info("[买入开多]触发成交开仓多单委托订单, 实时价/委托价：" + newPrice + "/" + order.getEntrustPrice() + ", Order: " + JSON.toJSONString(order));
                        dealOpenOrder(order);
                        orderIterator.remove();
                    }
                } else { // 空单
                    if (order.getEntrustPrice().compareTo(newPrice) <= 0) {
                        logger.info("[卖出开空]触发成交开仓空单委托订单, 实时价/委托价：" + newPrice + "/" + order.getEntrustPrice() + ", Order: " + JSON.toJSONString(order));
                        dealOpenOrder(order);
                        orderIterator.remove();
                    }
                }
            }
        }
    }

    /**
     * 处理平仓限价委托订单
     *
     * @param newPrice
     */
    public void processCloseEntrustList(BigDecimal newPrice) {
        synchronized (closeOrderList) {
            Iterator<SuperContractOrderEntrust> orderIterator = closeOrderList.iterator();
            while ((orderIterator.hasNext())) {
                SuperContractOrderEntrust order = orderIterator.next();
                if (order.getDirection() == ContractOrderDirection.BUY) {
                    if (order.getEntrustPrice().compareTo(newPrice) >= 0) {
                        logger.info("[买入平空]触发成交平仓空单委托订单, 实时价/委托价：" + newPrice + "/" + order.getEntrustPrice() + ", Order: " + JSON.toJSONString(order));
                        dealCloseOrder(order);
                        orderIterator.remove();
                    }
                } else {
                    if (order.getEntrustPrice().compareTo(newPrice) <= 0) {
                        logger.info("[卖出平多]触发成交平仓空单委托订单, 实时价/委托价：" + newPrice + "/" + order.getEntrustPrice() + ", Order: " + JSON.toJSONString(order));
                        dealCloseOrder(order);
                        orderIterator.remove();
                    }
                }
            }
        }
    }

    /**
     * 处理计划委托开仓委托订单
     *
     * @param newPrice
     */
    public void processOpenSpotEntrustList(BigDecimal newPrice) {
        synchronized (openOrderSpotList) {
            Iterator<SuperContractOrderEntrust> orderIterator = openOrderSpotList.iterator();
            while ((orderIterator.hasNext())) {
                SuperContractOrderEntrust order = orderIterator.next();
                // 这里分为两种可能性计划委托
                // 1、用户委托时，委托的触发价格大于当时的价格，到现在这个时候，触发价格小于行情价，说明价格涨到触发价，该触发了 （开多时相当于止盈，开空时相当于止损）
                // 2、用户委托时，委托的触发价格小于当时的价格，到现在这个时候，触发价格大于行情价，说明价格跌到触发价，该触发了 （开多时相当于止损，开空时相当于止盈）
                if ((order.getEntrustPrice().compareTo(order.getCurrentPrice()) >= 0 && order.getEntrustPrice().compareTo(newPrice) <= 0)
                        || (order.getEntrustPrice().compareTo(order.getCurrentPrice()) <= 0 && order.getEntrustPrice().compareTo(newPrice) >= 0)) {

                    MemberSuperContractWallet wallet = getWallet(order.getMemberId(), contractCoin.getId(),order.getContractOrderEntrustId());
                    // 检查保证金是否足够开单
                    // 0、计算当前开仓订单所需保证金
                    // 合约张数 * 合约面值 / 杠杆倍数 （该计算方式适合于金本位，即USDT作为保证金模式）
                    BigDecimal principalAmount = order.getPrincipalAmount();

                    // 1、计算开仓手续费(合约张数 * 合约面值 * 开仓费率）
                    BigDecimal openFee = order.getOpenFee();

                    MemberBusinessWallet businessWallet=memberBusinessWalletService.getMemberBusinessWalletByMemberId(order.getMemberId());

                    //判断当前余额是否足够创建开仓
                    if (principalAmount.add(openFee).compareTo(businessWallet.getFreezeMoney()) > 0) {
                        logger.info("超级杠杆-委托失败，余额不足：" + order.getMemberId() + " - " + order.getId() + " - " + order.getContractOrderEntrustId());
                        continue;
                    }
                    // 触发委托
//                    if (order.getEntrustPrice().compareTo(BigDecimal.ZERO) == 0) { // 市价委托
//                        // 更改委托单类型
//                        order.setType(ContractOrderType.MARKET_PRICE);
//                    } else { // 限价委托
//                        // 更改委托单类型
//                        order.setType(ContractOrderType.LIMIT_PRICE);
//                    }
//                    contractOrderEntrustService.saveOrUpdate(order);
                    try {
                        this.dealOpenOrder(order);
                        orderIterator.remove();
                    } catch (Exception e) {
                        logger.info("计划委托失败，请检查交易引擎");
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 处理计划委托平仓委托订单
     *
     * @param newPrice
     */
    public void processCloseSpotEntrustList(BigDecimal newPrice) {
//
    }

    /**
     * 同步仓位信息
     */
    public void syncMemberPosition() {
        // 同步最新用户仓位
        synchronized (memberContractWalletList) {

            List<MemberSuperContractWallet> list = memberContractWalletService.findAllNeedSync(contractCoin);
            for(MemberSuperContractWallet wallet:list){
                memberContractWalletList.put(wallet.getId(),wallet);
            }
            logger.info(contractCoin.getSymbol() + "=====>同步最新用户仓位信息, size: " + memberContractWalletList.size());
            if(memberContractWalletList.size()>0){
                logger.info("找到同步仓位 {}",memberContractWalletList);
            }
        }
    }

    private boolean isCheckBlashTest;
    public void checkBlashTest(MemberContractWallet wallet){
        isCheckBlashTest=true;
        this.nowPrice=wallet.getForceClosePrice();
        this.process(wallet.getForceClosePrice());
    }



    public void newBuyBlastCheck(BigDecimal newPrice) {
        synchronized (memberContractWalletList) {
            Iterator<MemberSuperContractWallet> walletIterator = memberContractWalletList.values().iterator();
            while (walletIterator.hasNext()) {
                MemberSuperContractWallet wallet = walletIterator.next();
                if (wallet == null) {
                    continue;
                }

//                logger.info("爆仓检查 : new price: {}, wallet: {}",newPrice,wallet);

                //如果是多仓
                if (wallet.getUsdtBuyPosition().compareTo(BigDecimal.ZERO) > 0) {
                    //如果平仓价格大于当前价格，说明已经爆仓
                    if (wallet.getForceClosePrice().compareTo(newPrice) >= 0) {
                        blastBuy(wallet, newPrice);
                    }
                }

                if (wallet.getUsdtBuyPosition().compareTo(BigDecimal.ZERO) < 0) {
                    //如果平仓价格小于当前价格，则多仓爆仓
                    if (wallet.getForceClosePrice().compareTo(newPrice) <= 0) {
                        blastSell(wallet, newPrice);
                    }
                }

            }
        }


    }



    // 爆多单
    @Transactional
    public void blastBuy(MemberSuperContractWallet wallet, BigDecimal price) {
        if (wallet.getUsdtBuyPosition().add(wallet.getUsdtFrozenBuyPosition()).compareTo(BigDecimal.ZERO) > 0 && wallet.getUsdtBuyPrice().compareTo(BigDecimal.ZERO) > 0) {
            logger.info("多仓爆仓，用户ID：{}，爆仓执行价：{}", wallet.getMemberId(), price);
            // 计算收益
            BigDecimal closeFee = wallet.getCloseFee();
            // 新建合约委托单
            SuperContractOrderEntrust orderEntrust = new SuperContractOrderEntrust();
            orderEntrust.setContractId(contractCoin.getId()); // 合约ID
            orderEntrust.setMemberId(wallet.getMemberId()); // 用户ID
            orderEntrust.setSymbol(contractCoin.getSymbol()); // 交易对符号
            orderEntrust.setBaseSymbol(contractCoin.getSymbol().split("/")[1]); // 基币/结算币
            orderEntrust.setCoinSymbol(contractCoin.getSymbol().split("/")[0]); // 币种符号
            orderEntrust.setDirection(ContractOrderDirection.SELL); // 平仓方向：平空/平多
            orderEntrust.setContractOrderEntrustId(GeneratorUtil.getOrderId("SUPER_CE"));
            orderEntrust.setVolume(wallet.getUsdtBuyPosition().add(wallet.getUsdtFrozenBuyPosition())); // 平仓张数
            orderEntrust.setTradedVolume(wallet.getUsdtBuyPosition().add(wallet.getUsdtFrozenBuyPosition())); // 已交易数量
            orderEntrust.setTradedPrice(price); // 成交价格
            orderEntrust.setPrincipalUnit("USDT"); // 保证金单位
            orderEntrust.setPrincipalAmount(BigDecimal.ZERO); // 保证金数量
            orderEntrust.setCreateTime(System.currentTimeMillis()); // 开仓时间
            orderEntrust.setType(ContractOrderType.MARKET_PRICE);
            orderEntrust.setTriggerPrice(BigDecimal.ZERO); // 触发价
            orderEntrust.setEntrustPrice(BigDecimal.ZERO); // 委托价格
            orderEntrust.setEntrustType(ContractOrderEntrustType.CLOSE); // 平仓
            orderEntrust.setCloseOrderType(ContractOrderEntrustCloseType.CLOSE_FORCE);
            orderEntrust.setTriggeringTime(System.currentTimeMillis()); // 触发时间，暂时无效
            orderEntrust.setPatterns(wallet.getUsdtPattern()); // 仓位模式
            orderEntrust.setCloseFee(closeFee);
            orderEntrust.setCurrentPrice(price);
            orderEntrust.setIsBlast(1); // 是爆仓单
            orderEntrust.setStatus(ContractOrderEntrustStatus.ENTRUST_SUCCESS);
            boolean result = contractOrderEntrustService.saveOrUpdate(orderEntrust);
            this.subOrder(wallet,orderEntrust);
        }
    }

    // 爆空单
    @Transactional
    public void blastSell(MemberSuperContractWallet wallet, BigDecimal price) {

        if (wallet.getUsdtBuyPosition().add(wallet.getUsdtFrozenBuyPosition()).compareTo(BigDecimal.ZERO) < 0 && wallet.getUsdtBuyPrice().compareTo(BigDecimal.ZERO) > 0) {
            logger.info("空仓爆仓，用户ID：{}，爆仓执行价：{}", wallet.getMemberId(), price);
            BigDecimal closeFee = wallet.getCloseFee();
            // 新建合约委托单
            SuperContractOrderEntrust orderEntrust = new SuperContractOrderEntrust();
            orderEntrust.setContractId(contractCoin.getId()); // 合约ID
            orderEntrust.setMemberId(wallet.getMemberId()); // 用户ID
            orderEntrust.setSymbol(contractCoin.getSymbol()); // 交易对符号
            orderEntrust.setBaseSymbol(contractCoin.getSymbol().split("/")[1]); // 基币/结算币
            orderEntrust.setCoinSymbol(contractCoin.getSymbol().split("/")[0]); // 币种符号
            orderEntrust.setDirection(ContractOrderDirection.BUY); // 平仓方向：平空/平多
            orderEntrust.setContractOrderEntrustId(GeneratorUtil.getOrderId("SUPER_CE"));
            orderEntrust.setTradedPrice(price); // 成交价格
            orderEntrust.setPrincipalUnit("USDT"); // 保证金单位
            orderEntrust.setPrincipalAmount(BigDecimal.ZERO); // 保证金数量
            orderEntrust.setCreateTime(System.currentTimeMillis()); // 开仓时间
            orderEntrust.setType(ContractOrderType.MARKET_PRICE);
            orderEntrust.setTriggerPrice(BigDecimal.ZERO); // 触发价
            orderEntrust.setEntrustPrice(BigDecimal.ZERO); // 委托价格
            orderEntrust.setEntrustType(ContractOrderEntrustType.CLOSE); // 平仓
            orderEntrust.setCloseOrderType(ContractOrderEntrustCloseType.CLOSE_FORCE);
            orderEntrust.setTriggeringTime(System.currentTimeMillis()); // 触发时间，暂时无效
            orderEntrust.setPatterns(wallet.getUsdtPattern()); // 仓位模式
            orderEntrust.setCloseFee(closeFee);
            orderEntrust.setCurrentPrice(price);
            orderEntrust.setIsBlast(1); // 是爆仓单
            orderEntrust.setStatus(ContractOrderEntrustStatus.ENTRUST_SUCCESS); // 委托状态：委托成功
            boolean result = contractOrderEntrustService.saveOrUpdate(orderEntrust);
            this.subOrder(wallet,orderEntrust);
        }
    }

    // 爆全仓
    public void blastAll(MemberSuperContractWallet wallet, BigDecimal price) {
        logger.info("全仓爆仓，用户ID：{}，执行价：{}", wallet.getMemberId(), price);

        // 平多
        blastBuy(wallet, price);

        // 平空
        blastSell(wallet, price);

    }

    // 撤销订单（委托单）
    public synchronized void cancelContractOrderEntrust(SuperContractOrderEntrust orderEntrust, boolean isBlast) {
        // 查找订单
        LinkedList<SuperContractOrderEntrust> list = null;
        if (orderEntrust.getEntrustType() == ContractOrderEntrustType.OPEN) {
            if (orderEntrust.getType() == ContractOrderType.SPOT_LIMIT) {
                list = this.openOrderSpotList;
                logger.info("取消开仓订单 - 计划委托类型");
            } else {
                logger.info("取消开仓订单 - 非计划委托类型");
                list = this.openOrderList;
            }
        } else {
            if (orderEntrust.getType() == ContractOrderType.SPOT_LIMIT) {
                list = this.closeOrderSpotList;
                logger.info("取消平仓订单 - 计划委托类型");
            } else {
                list = this.closeOrderList;
                logger.info("取消平仓订单 - 非计划委托类型");
            }
        }
        synchronized (list) {
            logger.info("撤销订单列表大小：{}", list.size());
            Iterator<SuperContractOrderEntrust> orderIterator = list.iterator();
            while ((orderIterator.hasNext())) {
                SuperContractOrderEntrust order = orderIterator.next();
                logger.info("撤销订单ID：{}, 对比 开仓订单ID: {}", orderEntrust.getId(), order.getId());
                if (order.getId().longValue() == orderEntrust.getId().longValue()) {
                    logger.info("撤销订单，在引擎中发现订单，予以撤销");
                    contractOrderEntrustService.updateStatus(orderEntrust.getId(), ContractOrderEntrustStatus.ENTRUST_CANCEL);
                    orderIterator.remove();

                    if(order.getEntrustType()==ContractOrderEntrustType.OPEN||order.getEntrustType()==ContractOrderEntrustType.ADD){
                        //如果是开仓或者是加仓，取消订单则需要返还余额
//                        memberWalletService.updateAddBalance(order.getMemberId(),order.getTradedPrice(),order.getContractOrderEntrustId(),BusinessSubType.SUPER_3,"超级杠杆撤销订单");

                        MemberBusinessWallet memberBusinessWallet = memberBusinessWalletService.getMemberBusinessWalletByMemberId(order.getMemberId());
                        memberBusinessWalletService.unfreezeBalance(memberBusinessWallet.getId(),order.getValue(), memberBusinessWallet.getMoney().add(order.getValue()), order.getContractOrderEntrustId(), BusinessSubType.SUPER_OPEN_CANCEL,"平仓返还保证金");
                    }

                }
            }
        }
    }


    /**
     * 初始化Thumb
     */
    public void initializeThumb() {
        this.thumb = new CoinThumb();
        this.thumb.setChg(BigDecimal.ZERO);                 // 变化百分比（例：4%）
        this.thumb.setChange(BigDecimal.ZERO);              // 变化金额
        this.thumb.setOpen(BigDecimal.ZERO);                // 开盘价
        this.thumb.setClose(BigDecimal.ZERO);               // 收盘价
        this.thumb.setHigh(BigDecimal.ZERO);                // 最高价
        this.thumb.setLow(BigDecimal.ZERO);                 // 最低价
        this.thumb.setBaseUsdRate(BigDecimal.valueOf(7.0)); // 基础USDT汇率
        this.thumb.setLastDayClose(BigDecimal.ZERO);        // 前日收盘价
        this.thumb.setSymbol(this.symbol);                  // 交易对符号
        this.thumb.setUsdRate(BigDecimal.valueOf(7.0));     // USDT汇率
        this.thumb.setZone(0);                              // 交易区
        this.thumb.setVolume(BigDecimal.ZERO);              // 成交量
        this.thumb.setTurnover(BigDecimal.ZERO);            // 成交额
    }

    public void handleCoinThumb() {
        for (MarketHandler storage : handlers) {
            storage.handleTrade(symbol, thumb);
        }
    }

    public void handleKLineStorage(KLine kLine) {
        for (MarketHandler storage : handlers) {
            storage.handleKLine(symbol, kLine);
        }
    }

    // 获取交易对符号
    public String getSymbol() {
        return this.symbol;
    }

    // 获取币种符号
    public String getCoinSymbol() {
        return this.coinSymbol;
    }

    // 获取基币符号
    public String getBaseSymbol() {
        return this.baseSymbol;
    }

    // 获取交易对最新报价
    public BigDecimal getNowPrice() {
        return this.nowPrice;
    }

    // 获取交易对最新行情
    public CoinThumb getThumb() {
        return this.thumb;
    }

    // 获取最新成交明细
    public List<ContractTrade> getLastedTradeList() {
        return this.lastedTradeList;
    }

    // 获取盘口数据
    public TradePlate getTradePlate(ContractOrderDirection direction) {
        if (direction == ContractOrderDirection.BUY) {
            return buyTradePlate;
        } else {
            return sellTradePlate;
        }
    }

    // 设置合约币种服务
    public void setContractCoinService(SuperContractCoinService contractCoinService) {
        this.superContractCoinService = contractCoinService;
    }

    // 设置合约订单委托服务
    public void setContractOrderEntrustService(SuperContractOrderEntrustService contractOrderEntrustService) {
        this.contractOrderEntrustService = contractOrderEntrustService;
    }

    // 添加处理者
    public void addHandler(MarketHandler storage) {
        handlers.add(storage);
    }

    public void setExchangePushJob(ExchangePushJob job) {
        this.exchangePushJob = job;
    }

    public void setMemberTransactionService(SuperMemberTransactionService memberTransactionService) {
        this.memberTransactionService = memberTransactionService;
    }

    public void setMemberContractWalletService(MemberSuperContractWalletService memberContractWalletService) {
        this.memberContractWalletService = memberContractWalletService;
    }

    //打码量记录
    public void setMemberRechargeRecordService(MemberRechargeRecordService memberRechargeRecordService) {
        this.memberRechargeRecordService = memberRechargeRecordService;
    }

    /**
     * 更新合约交易对信息
     *
     * @param coin
     */
    public void updateContractCoin(SuperContractCoin coin) {
        synchronized (contractCoin) {
            contractCoin = coin;
        }
    }



    /**
     * 获取引擎中所有用户持仓信息
     *
     * @return
     */
    public List<MemberSuperContractWallet> getMemberContractWalletList() {
        return new ArrayList<>(this.memberContractWalletList.values());
    }

    /**
     * 处理手续费
     */
    public void handleFee(Long memberId, BigDecimal fee) {
        logger.info("处理手续费，用户：{}, 手续费：{}", memberId, fee);
    }

    /**
     * 处理盈亏
     */
    public void handlePl(Long memberId, BigDecimal pL, BigDecimal fee) {

        MemberTransaction memberTransaction = new MemberTransaction();
        memberTransaction.setFee(fee);
        memberTransaction.setAmount(pL);
        memberTransaction.setMemberId(memberId);
        memberTransaction.setSymbol(contractCoin.getSymbol().split("/")[1]);
        memberTransaction.setType(TransactionType.CONTRACT_TRANSACTION);
        memberTransaction.setCreateTime(new Date());
        memberTransaction.setCreateTime2TimeStamp(System.currentTimeMillis());
        memberTransaction.setRealFee(fee.toString());
        memberTransaction.setDiscountFee("0");
        memberTransaction.setAccountWalletType(AccountWalletType.PERPETUAL_ACCOUNT);
        memberTransaction.setAmountType(TransactionAmountTypeEnum.CONTRACT_PROFIT_LOSS);
        memberTransactionService.saveOrUpdate(memberTransaction);
        logger.info("处理盈亏，用户：{}, 实际盈亏：{}", memberId, pL);
    }

    /**
     * 更新钱包
     *
     * @param walletId
     */
    public void memberWalletChange(Long walletId) {
        synchronized (memberContractWalletList) {
            MemberSuperContractWallet wallet = memberContractWalletService.getById(walletId);
            if (wallet != null) {
                logger.info("新增用户钱包，用户：{}, 内容：{}", wallet.getMemberId(), JSON.toJSONString(wallet));
                memberContractWalletList.put(walletId,wallet);
            }
        }
    }
}
