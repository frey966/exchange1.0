package com.financia.exchange.engine;

import com.alibaba.fastjson.JSON;
import com.financia.common.core.enums.BusinessSubType;
import com.financia.exchange.*;
import com.financia.exchange.feign.client.RemoteMemberWalletService;
import com.financia.exchange.handler.MarketHandler;
import com.financia.exchange.service.*;
import com.financia.exchange.util.DateUtil;
import com.financia.exchange.util.GeneratorUtil;
import com.financia.exchange.websocket.ExchangePushJob;
import com.financia.swap.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.ParameterOutOfBoundsException;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.ParseException;
import java.util.*;

import static com.financia.exchange.AssetRecordReasonEnum.*;

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

    private ContractCoinService contractCoinService;                  // 合约币种服务
    private ContractOrderEntrustService contractOrderEntrustService;  // 合约委托单服务
    private MemberTransactionService memberTransactionService;
    private MemberContractWalletService memberContractWalletService;
    private MemberRechargeRecordService memberRechargeRecordService;
    private ContractCoin contractCoin;

    private List<ContractOrderEntrust> contractOrderEntrustList = new ArrayList<>();      // 委托列表(计划委托)
    private Map<Long,MemberContractWallet> memberContractWalletList = new HashMap<>();      // 用户仓位信息


    private List<MarketHandler> handlers;                             // 行情、概要等处理者
    private ExchangePushJob exchangePushJob;                          // 推送任务

    //卖盘盘口信息
    private TradePlate sellTradePlate;
    //买盘盘口信息
    private TradePlate buyTradePlate;

    private boolean isStarted = false;                                // 是否启动完成（用于初始化时，获取一些数据库未处理的订单的，如果没获取完，不允许处理）

    private LinkedList<ContractOrderEntrust> openOrderList = new LinkedList<ContractOrderEntrust>(); // 开仓订单
    private LinkedList<ContractOrderEntrust> closeOrderList = new LinkedList<ContractOrderEntrust>(); // 平仓订单

    private LinkedList<ContractOrderEntrust> openOrderSpotList = new LinkedList<ContractOrderEntrust>(); // 开仓止盈止损订单
    private LinkedList<ContractOrderEntrust> closeOrderSpotList = new LinkedList<ContractOrderEntrust>(); // 平仓止盈止损订单


    MemberAssetRecordService assetRecordService;

    public void setAssetRecordService(MemberAssetRecordService assetRecordService) {
        this.assetRecordService = assetRecordService;
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

    public void trade(ContractOrderEntrust order) throws ParseException {
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
                if ((order.getDirection() == ContractOrderDirection.BUY && order.getEntrustPrice().compareTo(nowPrice) >= 0)
                        || (order.getDirection() == ContractOrderDirection.SELL && order.getEntrustPrice().compareTo(nowPrice) <= 0)) { // 出价高于当前价，直接成交
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
            MemberContractWallet wallet=getWallet(order.getMemberId(), order.getContractId());
            this.addOrder(wallet,order);
        }else if(order.getEntrustType()==ContractOrderEntrustType.SUB){
            MemberContractWallet wallet=getWallet(order.getMemberId(), order.getContractId());
            this.subOrder(wallet,order);
        }else if(order.getEntrustType()==ContractOrderEntrustType.STOP_LOSS||order.getEntrustType()==ContractOrderEntrustType.STOP_PROFIT){
            MemberContractWallet wallet=getWallet(order.getMemberId(), order.getContractId());
            this.setStopLossPrice(wallet,order);
            this.setStopProfitPrice(wallet,order);

            order.setWalletId(wallet.getId());
            order.setCurrentPrice(nowPrice);
            order.setTriggeringTime(System.currentTimeMillis());

            contractOrderEntrustService.saveOrUpdate(order);

        }
    }

    private MemberContractWallet getWallet(Long memberId,Long contractId){
        MemberContractWallet wallet=memberContractWalletService.findByMemberIdAndContractCoin(memberId, contractId);
        memberContractWalletList.put(wallet.getId(),wallet);
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
            Iterator<MemberContractWallet> walletIterator = memberContractWalletList.values().iterator();
            while (walletIterator.hasNext()) {
                MemberContractWallet wallet = walletIterator.next();
                if (wallet == null) {
                    continue;
                }

                if(wallet.getCoinBuyPrincipalAmount().compareTo(BigDecimal.ZERO)<=0){
                    continue;
                }

                BigDecimal feePercent=contractCoin.getFeePercent();
                BigDecimal value=feePercent.multiply(wallet.getUsdtBuyPosition()).multiply(nowPrice);
                ContractOrderEntrust entrust=new ContractOrderEntrust();
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

                entrust.setContractOrderEntrustId(GeneratorUtil.getOrderId("CE"));

                entrust.setStatus(ContractOrderEntrustStatus.ENTRUST_SUCCESS);

                contractOrderEntrustService.saveOrUpdate(entrust);

                addTransferOrder(wallet.getMemberId(),value, contractCoin.getCoinSymbol(), "",AssetRecordTypeEnum.CONTRACT_TRADE,AssetRecordReasonEnum.CONTRACT_ASSET_TRANSFER);

            }
        }
    }

    /**
     * 添加客户资金变动记录
     * */
    public void addTransferOrder(Long memberId,BigDecimal val,String symbol,String order, AssetRecordTypeEnum typeEnum,AssetRecordReasonEnum recordReasonEnum){

        MemberAssetRecord memberAssetRecord=new MemberAssetRecord();

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
     * 注： 加仓的时候 order.volume>0 代表正向买多 order.volume<0 代表买空
     */
    @Transactional
    public void addOrder(MemberContractWallet wallet, ContractOrderEntrust order) {

        logger.info("begin to add order :{} \n wallet is :{}",order,wallet);

        //判断加仓和持仓的方向。加仓只能加同方向的仓
        if((wallet.getUsdtBuyPosition().compareTo(BigDecimal.ZERO)<0&&order.getDirection()==ContractOrderDirection.BUY)
            ||(wallet.getUsdtBuyPosition().compareTo(BigDecimal.ZERO)>0&&order.getDirection()==ContractOrderDirection.SELL)
        ){
            logger.info("加仓方向与持仓方向相反，错误。order:{} ,\n wallet:{}",order,wallet);
//            throw new RuntimeException("加仓方向只能与持仓方向相同");
            return;
        }


        MemberBusinessWallet businessWallet=walletService.getMemberBusinessWalletByMemberId(wallet.getMemberId());
        //扣除冻结金额
        logger.info("decrease money is {} and order pri {}",businessWallet,order.getPrincipalAmount());
        walletService.decreaseFreezeBalance(businessWallet.getId(),order.getPrincipalAmount(), businessWallet.getMoney(), order.getContractOrderEntrustId(), order.getEntrustType()==ContractOrderEntrustType.OPEN?BusinessSubType.SWAP_OPEN_SUCCESS:BusinessSubType.SWAP_ADD_SUCCESS,"扣除保证金");
//        wallet.setUsdtBalance(wallet.getUsdtBalance().subtract(order.getPrincipalAmount()));

        //追加保证金
        memberContractWalletService.increaseUsdtBuyPrincipalAmountWithFrozen(wallet.getId(), order.getPrincipalAmount());
        wallet.setUsdtBuyPrincipalAmount(wallet.getUsdtBuyPrincipalAmount().add(order.getPrincipalAmount()));

        memberContractWalletService.increaseCloseFee(wallet.getId(),order.getCloseFee());
        wallet.setCloseFee(wallet.getCloseFee().add(order.getCloseFee()));


        //开仓费计入盈亏
        memberContractWalletService.increaseUsdtLoss(wallet.getId(),order.getOpenFee());
        wallet.setUsdtLoss(wallet.getUsdtLoss().add(order.getOpenFee()));

        if(order.getEntrustType()==ContractOrderEntrustType.OPEN||wallet.getUsdtBuyPrice().compareTo(BigDecimal.ZERO)<=0){
            //如果是开仓，则更新持仓均价 更新持仓均价
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


        this.addTransferOrder(wallet.getMemberId(),order.getPrincipalAmount(),order.getBaseSymbol(),order.getContractOrderEntrustId(),AssetRecordTypeEnum.CONTRACT_TRADE,entrustType2TransactionType(order.getEntrustType()));

        this.addTransferOrder(wallet.getMemberId(),order.getVolume().abs(),order.getCoinSymbol(),order.getContractOrderEntrustId(),AssetRecordTypeEnum.CONTRACT_TRADE,entrustType2TransactionType(order.getEntrustType()));



        if(order.getOpenFee().compareTo(BigDecimal.ZERO)>0){
            this.addTransferOrder(wallet.getMemberId(),order.getOpenFee(),order.getBaseSymbol(),order.getContractOrderEntrustId(),AssetRecordTypeEnum.CONTRACT_TRADE,AssetRecordReasonEnum.OPEN_FEE);
        }

        //更新订单委托状态
        order.setStatus(ContractOrderEntrustStatus.ENTRUST_SUCCESS); // 委托状态：已成交
        order.setTriggeringTime(System.currentTimeMillis());
        order.setTradedVolume(order.getVolume()); // 设置已交易数量
        order.setTradedPrice(order.getCurrentPrice());
        order.setAvaPrice(wallet.getUsdtBuyPrice());
        order.setForceClosePrice(wallet.getForceClosePrice());
        order.setWalletId(wallet.getId());

        contractOrderEntrustService.saveOrUpdate(order);

        //计算强制平仓价
        this.calcuForceClosePrice(wallet, order);

    }

//    @Autowired
//    RemoteMemberWalletService memberWalletService;

    MemberBusinessWalletService walletService;

    public void setWalletService(MemberBusinessWalletService walletService) {
        this.walletService = walletService;
    }

    /**
     * 减仓
     */
    @Transactional
    public void subOrder(MemberContractWallet wallet, ContractOrderEntrust order) {

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

        subValue=subValue.setScale(6,RoundingMode.DOWN);

        logger.info("current price is {}, buy price is {}, profit is {}",nowPrice,wallet.getUsdtBuyPrice(),subValue);
        order.setProfitAndLoss(subValue);


        BigDecimal subRate = order.getVolume().divide(userHold, 8, RoundingMode.DOWN).abs();

        logger.info("sub rate is {}",subRate);

        BigDecimal subPrincipal = wallet.getUsdtBuyPrincipalAmount().multiply(subRate);
        subPrincipal=subPrincipal.setScale(6,RoundingMode.DOWN);
        order.setPrincipalAmount(subPrincipal);

        logger.info("sub principal is {}",subPrincipal);

        //平仓手续费
        BigDecimal closeFee = wallet.getCloseFee().multiply(subRate);

        logger.info("close fee is {}",closeFee);

        BigDecimal userBackAsset; //归还本金

        userBackAsset = subPrincipal.add(subValue).subtract(closeFee);

        order.setBackAsset(userBackAsset);

        logger.info("user back asset is {}",userBackAsset);

        // 查询钱包余额
        MemberBusinessWallet memberBusinessWallet = walletService.getMemberBusinessWalletByMemberId(wallet.getMemberId());

        //将利润返还给余额
        if(userBackAsset.compareTo(BigDecimal.ZERO)>0){
//            memberContractWalletService.increaseUsdtBalance(wallet.getId(), userBackAsset);
//            wallet.setUsdtBalance(wallet.getUsdtBalance().add(userBackAsset));
            walletService.increaseBalance(memberBusinessWallet.getId(),userBackAsset,memberBusinessWallet.getMoney().add(userBackAsset),order.getContractOrderEntrustId(),order.getEntrustType()==ContractOrderEntrustType.CLOSE?BusinessSubType.SWAP_CLOSE_SUCCESS:BusinessSubType.SWAP_SUB_SUCCESS,"盈利计算");

            this.addTransferOrder(wallet.getMemberId(),userBackAsset,order.getBaseSymbol(),order.getContractOrderEntrustId(),AssetRecordTypeEnum.CONTRACT_TRADE,entrustType2TransactionType(order.getEntrustType()));

        }

        order.setPrincipalAmount(subPrincipal);
        order.setCloseFee(closeFee);
        order.setCurrentPrice(nowPrice);

        //减少持仓数量和保证金
        if(wallet.getUsdtBuyPosition().compareTo(BigDecimal.ZERO)>0){
            //如果是多仓
            memberContractWalletService.decreasePrincipalAmount(wallet.getId(),subPrincipal);
            memberContractWalletService.decreaseUsdtPosition(wallet.getId(),order.getVolume());

            wallet.setUsdtBuyPrincipalAmount(wallet.getUsdtBuyPrincipalAmount().subtract(subPrincipal));
            wallet.setUsdtBuyPosition(wallet.getUsdtBuyPosition().subtract(order.getVolume()));

            this.addTransferOrder(wallet.getMemberId(),subPrincipal.negate(),this.baseSymbol,order.getContractOrderEntrustId(),AssetRecordTypeEnum.CONTRACT_TRADE,AssetRecordReasonEnum.CONTRACT_SUB_PRINCIPLE);
            this.addTransferOrder(wallet.getMemberId(),order.getVolume().negate(),this.coinSymbol,order.getContractOrderEntrustId(),AssetRecordTypeEnum.CONTRACT_TRADE,AssetRecordReasonEnum.CONTRACT_SUB_COIN);

        }else if(wallet.getUsdtBuyPosition().compareTo(BigDecimal.ZERO)<0){
            //如果是空仓
            memberContractWalletService.decreasePrincipalAmount(wallet.getId(),subPrincipal);
            memberContractWalletService.decreaseUsdtPosition(wallet.getId(),order.getVolume().negate());

            wallet.setUsdtBuyPrincipalAmount(wallet.getUsdtBuyPrincipalAmount().subtract(subPrincipal));
            wallet.setUsdtBuyPosition(wallet.getUsdtBuyPosition().subtract(order.getVolume().negate()));


            this.addTransferOrder(wallet.getMemberId(),subPrincipal.negate(),this.baseSymbol,order.getContractOrderEntrustId(),AssetRecordTypeEnum.CONTRACT_TRADE,AssetRecordReasonEnum.CONTRACT_SUB_PRINCIPLE);
            this.addTransferOrder(wallet.getMemberId(),order.getVolume(),this.coinSymbol,order.getContractOrderEntrustId(),AssetRecordTypeEnum.CONTRACT_TRADE,AssetRecordReasonEnum.CONTRACT_SUB_COIN);
        }

        //减少剩余平仓手续费
        memberContractWalletService.decreaseCloseFee(wallet.getId(), order.getCloseFee());
        wallet.setCloseFee(wallet.getCloseFee().subtract(order.getCloseFee()));

        //增加交易收取的平仓手续费
        contractCoinService.increaseTotalCloseFee(wallet.getContractId(), closeFee);
        //将平仓手续费计入亏损
        memberContractWalletService.increaseUsdtLoss(wallet.getId(),closeFee);
        wallet.setUsdtLoss(wallet.getUsdtLoss().add(closeFee));

        //手续费计入资金流
        this.addTransferOrder(wallet.getMemberId(),closeFee.negate(),this.baseSymbol,order.getContractOrderEntrustId(),AssetRecordTypeEnum.CONTRACT_TRADE,AssetRecordReasonEnum.CLOSE_FEE);



        //如果是盈利
        if(subValue.compareTo(BigDecimal.ZERO)>0){
            contractCoinService.increaseTotalProfit(wallet.getContractId(),subValue);
            //统计用户盈利
            memberContractWalletService.increaseUsdtProfit(wallet.getId(),subValue);
            wallet.setUsdtProfit(wallet.getUsdtProfit().add(subValue));

            this.addTransferOrder(wallet.getMemberId(),subValue,this.getBaseSymbol(),order.getContractOrderEntrustId(),AssetRecordTypeEnum.CONTRACT_TRADE,AssetRecordReasonEnum.CONTRACT_PROFIT);

        }else if(subValue.compareTo(BigDecimal.ZERO)<0){

            if(subValue.add(wallet.getUsdtBuyPrincipalAmount()).compareTo(BigDecimal.ZERO)<0){
                //交易所损失的资产
                BigDecimal exchangeLoss=wallet.getUsdtBuyPrincipalAmount().add(subValue).negate();
                subValue=wallet.getUsdtBuyPrincipalAmount().negate();
                memberContractWalletService.increaseExchangeLoss(wallet.getId(),exchangeLoss);

                this.addTransferOrder(0L,exchangeLoss.negate(),order.getBaseSymbol(),order.getContractOrderEntrustId(),AssetRecordTypeEnum.CONTRACT_TRADE,EXCHANGE_LOSS);

            }

            //如果是亏损
            contractCoinService.increaseTotalLoss(wallet.getContractId(),subValue.negate());
            //统计用户损失
            memberContractWalletService.increaseUsdtLoss(wallet.getId(),subValue.negate());
            wallet.setUsdtLoss(wallet.getUsdtLoss().add(subValue.negate()));

            this.addTransferOrder(0L,subValue.negate(),order.getBaseSymbol(),order.getContractOrderEntrustId(),AssetRecordTypeEnum.CONTRACT_TRADE,CONTRACT_CLOSE);
        }

        //更新订单委托状态
        order.setStatus(ContractOrderEntrustStatus.ENTRUST_SUCCESS); // 委托状态：已成交
        order.setTriggeringTime(System.currentTimeMillis());
        order.setTradedVolume(order.getVolume()); // 设置已交易数量
        order.setTradedPrice(order.getCurrentPrice());
        order.setAvaPrice(wallet.getUsdtBuyPrice());
        order.setForceClosePrice(wallet.getForceClosePrice());
        order.setWalletId(wallet.getId());

        if(subPrincipal.compareTo(BigDecimal.ZERO)>0){
            order.setBackAsset(subPrincipal);
        }

        contractOrderEntrustService.saveOrUpdate(order);

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
    public void dealOpenOrder(ContractOrderEntrust order){
        logger.info("成交开仓订单");
        MemberContractWallet memberContractWallet = getWallet(order.getMemberId(), contractCoin.getId());
        //从余额中扣除持仓金额

        logger.error("begin to decrease freeze, wallet is {}",walletService);
        MemberBusinessWallet businessWallet=walletService.getMemberBusinessWalletByMemberId(memberContractWallet.getMemberId());
        walletService.decreaseFreezeBalance(businessWallet.getId(),order.getCloseFee(),businessWallet.getMoney(),order.getContractOrderEntrustId(),BusinessSubType.SWAP_OPEN_SUCCESS,"开仓手续费");
        logger.error("has to decrease freeze");

        //记录新增开仓手续费
        memberContractWalletService.increaseOpenFee(memberContractWallet.getId(),order.getOpenFee());

        //交易对手续费增加
        contractCoinService.increaseTotalOpenFee(contractCoin.getId(),order.getOpenFee());

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

        if(userHold.multiply(order.getVolume()).compareTo(BigDecimal.ZERO)<0){
            //如果交易方向与持仓方向相反，则需要减仓。减仓时需要判断减仓的大小，如果减仓数量超过当前持仓数量，则先平仓到0，再创建空仓
            logger.info("trade direct is negative with add");
            //如果当前持仓数量小于对手仓的数量，则需要先平仓到0 ，再创建对手仓
            if(userHold.abs().compareTo(order.getVolume().abs())<0){

                logger.info("user hold is {}, vol is {}",userHold.abs(),order.getVolume().abs());

                ContractOrderEntrust entrust=new ContractOrderEntrust();
                entrust.setVolume(userHold.abs());
                entrust.setValue(memberContractWallet.getUsdtBuyPrincipalAmount());
                entrust.setOpenFee(BigDecimal.ZERO);
                entrust.setCoinSymbol(order.getCoinSymbol());
                entrust.setEntrustType(ContractOrderEntrustType.SUB_OPPONENT);
                entrust.setCurrentPrice(nowPrice);
                entrust.setCreateTime(System.currentTimeMillis());
                entrust.setContractOrderEntrustId(GeneratorUtil.getOrderId("CE"));


                logger.info("hold is smaller than add. sub price,nowPrice is:{}, entr:{}",nowPrice,entrust);


                this.subOrder(memberContractWallet,entrust);
                BigDecimal subPrincipal=order.getPrincipalAmount().subtract(memberContractWallet.getUsdtBuyPrincipalAmount());
                //更新钱包
                memberContractWallet=getWallet(memberContractWallet.getMemberId(),memberContractWallet.getContractId());

                //同时创建对手仓
                ContractOrderEntrust entrustAdd=new ContractOrderEntrust();
                entrustAdd.setOpenFee(BigDecimal.ZERO);
                entrustAdd.setContractId(order.getContractId());
                entrust.setContractOrderEntrustId(GeneratorUtil.getOrderId("CE"));
                entrustAdd.setEntrustType(ContractOrderEntrustType.ADD_OPPONENT);
                entrustAdd.setCoinSymbol(order.getCoinSymbol());
                entrustAdd.setVolume(order.getVolume().add(userHold).abs());
                entrustAdd.setDirection(order.getDirection());
                entrustAdd.setValue(subPrincipal);
                entrustAdd.setPrincipalAmount(subPrincipal);

                logger.info("hold is smaller than add. add price,nowPrice is:{}, entr:{}",nowPrice,entrustAdd);

                this.addOrder(memberContractWallet,entrustAdd);

//                order.setStatus(ContractOrderEntrustStatus.ENTRUST_SPLIT);
                this.contractOrderEntrustService.saveOrUpdate(order);
            }else{



                ContractOrderEntrust entrust=new ContractOrderEntrust();
                entrust.setValue(userHold.multiply(nowPrice));
                entrust.setOpenFee(BigDecimal.ZERO);
                entrust.setCoinSymbol(order.getCoinSymbol());
                entrust.setEntrustType(ContractOrderEntrustType.SUB);
                entrust.setCurrentPrice(nowPrice);
                entrust.setVolume(order.getVolume());
                entrust.setCreateTime(System.currentTimeMillis());
                entrust.setContractOrderEntrustId(GeneratorUtil.getOrderId("CE"));


                logger.info("hold is larger than add. sub price,nowPrice is:{}, entr:{}",nowPrice,entrust);

                this.subOrder(memberContractWallet,entrust);

                order.setStatus(ContractOrderEntrustStatus.ENTRUST_SPLIT);
                contractOrderEntrustService.saveOrUpdate(order);
            }
        }else{
            this.addOrder(memberContractWallet,order);
        }


        this.setStopLossPrice(memberContractWallet,order);
        this.setStopProfitPrice(memberContractWallet,order);

    }


    /**
     * 设置止盈价
     * */
    @Transactional
    public void setStopProfitPrice(MemberContractWallet wallet,ContractOrderEntrust entrust){
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
    public void setStopLossPrice(MemberContractWallet wallet,ContractOrderEntrust entrust){
        if(entrust.getStopLossPrice().compareTo(BigDecimal.ZERO)>0){
            logger.info("设置止损：{}， wallet is {}",entrust.getStopLossPrice(),wallet);
            wallet.setStopLossPrice(entrust.getStopLossPrice());
            memberContractWalletService.setStopLossPrice(wallet.getId(),entrust.getStopLossPrice());
        }
    }


    public void calcuForceClosePrice(MemberContractWallet memberContractWallet, ContractOrderEntrust order) {
        //计算强平价
        /**
         *
         * 强平价=开仓均价-开仓均价*风险率
         * 风险率=用户拥有的资产/交易所拥有的资产（均指该笔持仓中的资产）
         * 用户拥有的资产=（剩余持仓本金-未扣除平仓手续费）/开仓价
         * 交易所拥有的资产：
         * 交易所拥有的资产=总资产-用户资产
         * */
        logger.info("开始计算强平价, member : {},order {}",memberContractWallet,order);

        BigDecimal userAsset = memberContractWallet.getUsdtBuyPrincipalAmount().subtract(memberContractWallet.getCloseFee());
        BigDecimal exchangeAsset = memberContractWallet.getUsdtBuyPrice().multiply(memberContractWallet.getUsdtBuyPosition()).abs();

        if(exchangeAsset.compareTo(BigDecimal.ZERO)<=0){
            return;
        }

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
    public void dealCloseOrder(ContractOrderEntrust order) {
        logger.info("成交平仓委托");


        MemberContractWallet memberContractWallet = getWallet(order.getMemberId(), contractCoin.getId());

        if(order.getEntrustType()==ContractOrderEntrustType.CLOSE){
            order.setVolume(memberContractWallet.getUsdtBuyPosition());
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
        contractCoin = contractCoinService.findBySymbol(symbol);
        if (contractCoin == null) {
            logger.info(contractCoin.getSymbol() + "引擎启动失败，找不到合约交易对");
            return;
        }
        // 数据库查找订单，加载到列表中
        contractOrderEntrustList = contractOrderEntrustService.loadUnMatchOrders(contractCoin.getId());
        if (contractOrderEntrustList != null && contractOrderEntrustList.size() > 0) {
            logger.info(contractCoin.getSymbol() + "加载订单，共计 " + contractOrderEntrustList.size());
            for (ContractOrderEntrust item : contractOrderEntrustList) {
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

        // 更新买卖盘口数据
        this.buyTradePlate.setItems(new LinkedList<>(buyPlateItems));
        this.sellTradePlate.setItems(new LinkedList<>(sellPlateItems));

        // 推送买卖盘口数据
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
//        if(this.isCheckBlashTest){
//            return;
//        }

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

            logger.info("price is coming {} , symbols is {}",nowPrice,symbol);

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
            // 推送成交明细
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
            Iterator<MemberContractWallet> walletIterator = memberContractWalletList.values().iterator();
            while (walletIterator.hasNext()) {
                MemberContractWallet wallet = walletIterator.next();
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
                        logger.info("多仓止盈 price is:{},wallet is:{}",newPrice,wallet);
                        stopProfit(wallet, newPrice);
                    }else if(wallet.getStopLossPrice().compareTo(newPrice)>=0 && wallet.getStopLossPrice().compareTo(BigDecimal.ZERO)>0){
                        //如果当前价格小于止损价格，则表示止损
                        logger.info("多仓止损 price is:{},wallet is:{}",newPrice,wallet);
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

    public void stopProfit(MemberContractWallet wallet,BigDecimal newPrice){
        //创建止盈止损单
        //更新wallet，防止无限止盈止损

        wallet=getWallet(wallet.getMemberId(),wallet.getContractId());
        if(wallet.getUsdtBuyPosition().compareTo(BigDecimal.ZERO)>0&&wallet.getStopProfitPrice().compareTo(newPrice)>0){
            return;
        }
        if(wallet.getUsdtBuyPosition().compareTo(BigDecimal.ZERO)<0 && wallet.getStopProfitPrice().compareTo(newPrice)<0){
            return;
        }

        logger.info("开始进行止盈 {},price {}",wallet,newPrice);

        ContractOrderEntrust orderEntrust = new ContractOrderEntrust();
        orderEntrust.setContractId(contractCoin.getId()); // 合约ID
        orderEntrust.setMemberId(wallet.getMemberId()); // 用户ID
        orderEntrust.setSymbol(contractCoin.getSymbol()); // 交易对符号
        orderEntrust.setBaseSymbol(contractCoin.getSymbol().split("/")[1]); // 基币/结算币
        orderEntrust.setCoinSymbol(contractCoin.getSymbol().split("/")[0]); // 币种符号
        orderEntrust.setDirection(ContractOrderDirection.SELL); // 平仓方向：平空/平多
        orderEntrust.setContractOrderEntrustId(GeneratorUtil.getOrderId("CE"));
        orderEntrust.setVolume(wallet.getUsdtBuyPosition().add(wallet.getUsdtFrozenBuyPosition())); // 平仓张数
        orderEntrust.setTradedVolume(wallet.getUsdtBuyPosition().add(wallet.getUsdtFrozenBuyPosition())); // 已交易数量
        orderEntrust.setTradedPrice(newPrice); // 成交价格
        orderEntrust.setPrincipalUnit("USDT"); // 保证金单位
        orderEntrust.setCreateTime(DateUtil.getTimeMillis()); // 开仓时间
        orderEntrust.setType(ContractOrderType.MARKET_PRICE);
        orderEntrust.setTriggerPrice(nowPrice); // 触发价
        orderEntrust.setEntrustType(ContractOrderEntrustType.CLOSE); // 止盈
        orderEntrust.setCloseOrderType(ContractOrderEntrustCloseType.CLOSE_STOP_PROFIT);
        orderEntrust.setTriggeringTime(System.currentTimeMillis()); // 触发时间，暂时无效
        orderEntrust.setShareNumber(wallet.getUsdtShareNumber());
        orderEntrust.setPatterns(wallet.getUsdtPattern()); // 仓位模式
        orderEntrust.setCloseFee(wallet.getCloseFee());
        orderEntrust.setCurrentPrice(newPrice);
        orderEntrust.setTriggeringTime(System.currentTimeMillis());
        orderEntrust.setEntrustPrice(nowPrice);

        this.subOrder(wallet,orderEntrust);

    }

    public void stopLoss(MemberContractWallet wallet,BigDecimal newPrice){
        wallet=getWallet(wallet.getMemberId(),wallet.getContractId());
        if(wallet.getUsdtBuyPosition().compareTo(BigDecimal.ZERO)>0&&wallet.getStopLossPrice().compareTo(newPrice)<0){
            return;
        }
        if(wallet.getUsdtBuyPosition().compareTo(BigDecimal.ZERO)<0 && wallet.getStopLossPrice().compareTo(newPrice)>0){
            return;
        }

        logger.info("开始进行止损 {},price {}",wallet,newPrice);


        ContractOrderEntrust orderEntrust = new ContractOrderEntrust();
        orderEntrust.setContractId(contractCoin.getId()); // 合约ID
        orderEntrust.setMemberId(wallet.getMemberId()); // 用户ID
        orderEntrust.setSymbol(contractCoin.getSymbol()); // 交易对符号
        orderEntrust.setBaseSymbol(contractCoin.getSymbol().split("/")[1]); // 基币/结算币
        orderEntrust.setCoinSymbol(contractCoin.getSymbol().split("/")[0]); // 币种符号
        orderEntrust.setDirection(ContractOrderDirection.SELL); // 平仓方向：平空/平多
        orderEntrust.setContractOrderEntrustId(GeneratorUtil.getOrderId("CE"));
        orderEntrust.setVolume(wallet.getUsdtBuyPosition().add(wallet.getUsdtFrozenBuyPosition())); // 平仓张数
        orderEntrust.setTradedVolume(wallet.getUsdtBuyPosition().add(wallet.getUsdtFrozenBuyPosition())); // 已交易数量
        orderEntrust.setTradedPrice(newPrice); // 成交价格
        orderEntrust.setPrincipalUnit("USDT"); // 保证金单位
        orderEntrust.setCreateTime(DateUtil.getTimeMillis()); // 开仓时间
        orderEntrust.setType(ContractOrderType.MARKET_PRICE);
        orderEntrust.setTriggerPrice(nowPrice); // 触发价

        orderEntrust.setEntrustType(ContractOrderEntrustType.CLOSE); // 止损
        orderEntrust.setCloseOrderType(ContractOrderEntrustCloseType.CLOSE_STOP_LOSS);
        orderEntrust.setTriggeringTime(System.currentTimeMillis()); // 触发时间，暂时无效
        orderEntrust.setShareNumber(wallet.getUsdtShareNumber());
        orderEntrust.setPatterns(wallet.getUsdtPattern()); // 仓位模式
        orderEntrust.setCloseFee(wallet.getCloseFee());
        orderEntrust.setCurrentPrice(newPrice);
        orderEntrust.setTriggeringTime(System.currentTimeMillis());
        orderEntrust.setStatus(ContractOrderEntrustStatus.ENTRUST_SUCCESS);

        orderEntrust.setEntrustPrice(nowPrice);

        this.subOrder(wallet,orderEntrust);
    }


    /**
     * 处理开仓限价委托订单
     *
     * @param newPrice
     */
    public void processOpenEntrustList(BigDecimal newPrice) {
        synchronized (openOrderList) {
            Iterator<ContractOrderEntrust> orderIterator = openOrderList.iterator();
            while ((orderIterator.hasNext())) {
                ContractOrderEntrust order = orderIterator.next();
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
            Iterator<ContractOrderEntrust> orderIterator = closeOrderList.iterator();
            while ((orderIterator.hasNext())) {
                ContractOrderEntrust order = orderIterator.next();
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
            Iterator<ContractOrderEntrust> orderIterator = openOrderSpotList.iterator();
            while ((orderIterator.hasNext())) {
                ContractOrderEntrust order = orderIterator.next();
                // 这里分为两种可能性计划委托
                // 1、用户委托时，委托的触发价格大于当时的价格，到现在这个时候，触发价格小于行情价，说明价格涨到触发价，该触发了 （开多时相当于止盈，开空时相当于止损）
                // 2、用户委托时，委托的触发价格小于当时的价格，到现在这个时候，触发价格大于行情价，说明价格跌到触发价，该触发了 （开多时相当于止损，开空时相当于止盈）
                if ((order.getEntrustPrice().compareTo(order.getCurrentPrice()) >= 0 && order.getEntrustPrice().compareTo(newPrice) <= 0)
                        || (order.getEntrustPrice().compareTo(order.getCurrentPrice()) <= 0 && order.getEntrustPrice().compareTo(newPrice) >= 0)) {

                    MemberContractWallet wallet = getWallet(order.getMemberId(), contractCoin.getId());

                    BigDecimal principalAmount = order.getPrincipalAmount();

                    // 1、计算开仓手续费(合约张数 * 合约面值 * 开仓费率）
                    BigDecimal openFee = order.getOpenFee();

                    MemberBusinessWallet businessWallet=walletService.getMemberBusinessWalletByMemberId(order.getMemberId());

                    //判断当前余额是否足够创建开仓
                    if (principalAmount.add(openFee).compareTo(businessWallet.getFreezeMoney()) > 0) {
                        logger.info("计划委托失败，余额不足：" + order.getMemberId() + " - " + order.getId() + " - " + order.getContractOrderEntrustId());
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
//        logger.info("计划委托平仓处理开始：{}", newPrice);
        synchronized (closeOrderSpotList) {
            Iterator<ContractOrderEntrust> orderIterator = closeOrderSpotList.iterator();

//            logger.info("计划委托平仓处理订单列表, size: {}", closeOrderSpotList.size());
            while ((orderIterator.hasNext())) {
                ContractOrderEntrust order = orderIterator.next();
                // 这里分为两种可能性计划委托
                // 1、用户委托时，委托的触发价格大于当时的价格，到现在这个时候，触发价格小于行情价，说明价格涨到触发价，该触发了 （买入平空时相当于止损，卖出平多时相当于止盈）
                // 2、用户委托时，委托的触发价格小于当时的价格，到现在这个时候，触发价格大于行情价，说明价格跌到触发价，该触发了 （买入平空时相当于止盈，卖出平多时相当于止损）
                logger.info("计划委托平仓判断处理. {} - {} - {}", order.getEntrustPrice(), order.getCurrentPrice(), newPrice);
                if ((order.getEntrustPrice().compareTo(order.getCurrentPrice()) >= 0 && order.getEntrustPrice().compareTo(newPrice) <= 0)
                        || (order.getEntrustPrice().compareTo(order.getCurrentPrice()) <= 0 && order.getEntrustPrice().compareTo(newPrice) >= 0)) {
                    logger.info("触发计划：计划委托平仓处理. {} - {} - {}", order.getEntrustPrice(), order.getCurrentPrice(), newPrice);
                    MemberContractWallet wallet = getWallet(order.getMemberId(), contractCoin.getId());
                    // 触发委托
                    if (order.getDirection() == ContractOrderDirection.BUY) { // 买入平空，检查空单持仓量是否足够
                        // 检查空单持仓量是否足够
                        if (wallet.getUsdtBuyPosition().abs().compareTo(order.getVolume()) < 0) {
                            logger.info("计划委托失败,空单持仓不足：" + order.getMemberId() + " - " + order.getId() + " - " + order.getContractOrderEntrustId());
                            continue;
                        } else {
                            // 冻结空仓持仓
                            memberContractWalletService.freezeUsdtSellPosition(wallet.getId(), order.getVolume());
                        }
                    }
                    // 触发委托
                    if (order.getEntrustPrice().compareTo(BigDecimal.ZERO) == 0) { // 市价委托
                        order.setType(ContractOrderType.MARKET_PRICE);
                    } else {
                        order.setType(ContractOrderType.LIMIT_PRICE);
                    }
                    //计算平仓应该扣除多少保证金(平仓量/（可平仓量+冻结平仓量） *  保证金总量）
                    if (order.getDirection() == ContractOrderDirection.BUY) { // 买入平空
                        BigDecimal mPrinc = order.getVolume().divide(wallet.getUsdtSellPosition().add(wallet.getUsdtFrozenSellPosition()), 8, RoundingMode.HALF_UP).multiply(wallet.getUsdtSellPrincipalAmount());
                        order.setPrincipalAmount(mPrinc);
                    } else {
                        BigDecimal mPrinc = order.getVolume().divide(wallet.getUsdtBuyPosition().add(wallet.getUsdtFrozenBuyPosition()), 8, RoundingMode.HALF_UP).multiply(wallet.getUsdtBuyPrincipalAmount());
                        order.setPrincipalAmount(mPrinc);
                    }
                    contractOrderEntrustService.saveOrUpdate(order);
                    try {
                        this.trade(order);
                        orderIterator.remove();
                    } catch (ParseException e) {
                        logger.info("计划委托失败，请检查交易引擎");
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 同步仓位信息
     */
    public void syncMemberPosition() {
        // 同步最新用户仓位
        synchronized (memberContractWalletList) {

            List<MemberContractWallet> list = memberContractWalletService.findAllNeedSync(contractCoin);
            for(MemberContractWallet wallet:list){
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
            Iterator<MemberContractWallet> walletIterator = memberContractWalletList.values().iterator();
            while (walletIterator.hasNext()) {
                MemberContractWallet wallet = walletIterator.next();
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
    public void blastBuy(MemberContractWallet wallet, BigDecimal price) {
        if (wallet.getUsdtBuyPosition().add(wallet.getUsdtFrozenBuyPosition()).compareTo(BigDecimal.ZERO) > 0 && wallet.getUsdtBuyPrice().compareTo(BigDecimal.ZERO) > 0) {
            logger.info("多仓爆仓，用户ID：{}，爆仓执行价：{}", wallet.getMemberId(), price);
            // 计算收益
            BigDecimal closeFee = wallet.getCloseFee();
            // 新建合约委托单
            ContractOrderEntrust orderEntrust = new ContractOrderEntrust();
            orderEntrust.setContractId(contractCoin.getId()); // 合约ID
            orderEntrust.setMemberId(wallet.getMemberId()); // 用户ID
            orderEntrust.setSymbol(contractCoin.getSymbol()); // 交易对符号
            orderEntrust.setBaseSymbol(contractCoin.getSymbol().split("/")[1]); // 基币/结算币
            orderEntrust.setCoinSymbol(contractCoin.getSymbol().split("/")[0]); // 币种符号
            orderEntrust.setDirection(ContractOrderDirection.SELL); // 平仓方向：平空/平多
            orderEntrust.setContractOrderEntrustId(GeneratorUtil.getOrderId("CE"));
            orderEntrust.setVolume(wallet.getUsdtBuyPosition().add(wallet.getUsdtFrozenBuyPosition())); // 平仓张数
            orderEntrust.setTradedVolume(wallet.getUsdtBuyPosition().add(wallet.getUsdtFrozenBuyPosition())); // 已交易数量
            orderEntrust.setTradedPrice(price); // 成交价格
            orderEntrust.setPrincipalUnit("USDT"); // 保证金单位
            orderEntrust.setPrincipalAmount(BigDecimal.ZERO); // 保证金数量
            orderEntrust.setCreateTime(DateUtil.getTimeMillis()); // 开仓时间
            orderEntrust.setType(ContractOrderType.MARKET_PRICE);
            orderEntrust.setTriggerPrice(BigDecimal.ZERO); // 触发价
            orderEntrust.setEntrustPrice(BigDecimal.ZERO); // 委托价格
            orderEntrust.setEntrustType(ContractOrderEntrustType.CLOSE); // 平仓
            orderEntrust.setTriggeringTime(System.currentTimeMillis()); // 触发时间，暂时无效
            orderEntrust.setShareNumber(wallet.getUsdtShareNumber());
            orderEntrust.setPatterns(wallet.getUsdtPattern()); // 仓位模式
            orderEntrust.setCloseFee(closeFee);
            orderEntrust.setCurrentPrice(price);
            orderEntrust.setIsBlast(1); // 是爆仓单
            orderEntrust.setTriggeringTime(System.currentTimeMillis());
            orderEntrust.setStatus(ContractOrderEntrustStatus.ENTRUST_SUCCESS);
            boolean result = contractOrderEntrustService.saveOrUpdate(orderEntrust);
            this.subOrder(wallet,orderEntrust);
        }
    }

    // 爆空单
    @Transactional
    public void blastSell(MemberContractWallet wallet, BigDecimal price) {

        if (wallet.getUsdtBuyPosition().add(wallet.getUsdtFrozenBuyPosition()).compareTo(BigDecimal.ZERO) < 0 && wallet.getUsdtBuyPrice().compareTo(BigDecimal.ZERO) > 0) {
            logger.info("空仓爆仓，用户ID：{}，爆仓执行价：{}", wallet.getMemberId(), price);
            BigDecimal closeFee = wallet.getCloseFee();
            // 新建合约委托单
            ContractOrderEntrust orderEntrust = new ContractOrderEntrust();
            orderEntrust.setContractId(contractCoin.getId()); // 合约ID
            orderEntrust.setMemberId(wallet.getMemberId()); // 用户ID
            orderEntrust.setSymbol(contractCoin.getSymbol()); // 交易对符号
            orderEntrust.setBaseSymbol(contractCoin.getSymbol().split("/")[1]); // 基币/结算币
            orderEntrust.setCoinSymbol(contractCoin.getSymbol().split("/")[0]); // 币种符号
            orderEntrust.setDirection(ContractOrderDirection.BUY); // 平仓方向：平空/平多
            orderEntrust.setContractOrderEntrustId(GeneratorUtil.getOrderId("CE"));
            orderEntrust.setVolume(wallet.getUsdtBuyPosition().add(wallet.getUsdtFrozenSellPosition())); // 平仓张数
            orderEntrust.setTradedVolume(wallet.getUsdtSellPosition().add(wallet.getUsdtFrozenSellPosition())); // 已交易数量
            orderEntrust.setTradedPrice(price); // 成交价格
            orderEntrust.setPrincipalUnit("USDT"); // 保证金单位
            orderEntrust.setPrincipalAmount(BigDecimal.ZERO); // 保证金数量
            orderEntrust.setCreateTime(DateUtil.getTimeMillis()); // 开仓时间
            orderEntrust.setType(ContractOrderType.MARKET_PRICE);
            orderEntrust.setTriggerPrice(BigDecimal.ZERO); // 触发价
            orderEntrust.setEntrustPrice(BigDecimal.ZERO); // 委托价格
            orderEntrust.setEntrustType(ContractOrderEntrustType.CLOSE); // 平仓
            orderEntrust.setTriggeringTime(System.currentTimeMillis()); // 触发时间，暂时无效
            orderEntrust.setShareNumber(wallet.getUsdtShareNumber());
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
    public void blastAll(MemberContractWallet wallet, BigDecimal price) {
        logger.info("全仓爆仓，用户ID：{}，执行价：{}", wallet.getMemberId(), price);

        // 平多
        blastBuy(wallet, price);

        // 平空
        blastSell(wallet, price);

    }

    // 撤销订单（委托单）
    public synchronized void cancelContractOrderEntrust(ContractOrderEntrust orderEntrust, boolean isBlast) {
        // 查找订单
        LinkedList<ContractOrderEntrust> list = null;
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
            logger.info("撤销订单列表大小：{} ,list : {}", list.size(),list);
            Iterator<ContractOrderEntrust> orderIterator = list.iterator();
            while ((orderIterator.hasNext())) {
                ContractOrderEntrust order = orderIterator.next();
                logger.info("撤销订单ID：{}, 对比 开仓订单ID: {}", orderEntrust.getId(), order.getId());
                if (order.getId().longValue() == orderEntrust.getId().longValue()) {
                    logger.info("撤销订单，在引擎中发现订单，予以撤销");
                    contractOrderEntrustService.updateStatus(orderEntrust.getId(), ContractOrderEntrustStatus.ENTRUST_CANCEL, System.currentTimeMillis());
                    orderIterator.remove();

                    if(order.getEntrustType()==ContractOrderEntrustType.OPEN||order.getEntrustType()==ContractOrderEntrustType.ADD){
                        //如果是开仓或者是加仓，取消订单则需要返还余额
                        MemberBusinessWallet memberBusinessWallet = walletService.getMemberBusinessWalletByMemberId(order.getMemberId());
                        walletService.unfreezeBalance(memberBusinessWallet.getId(),order.getValue(), memberBusinessWallet.getMoney().add(order.getValue()), order.getContractOrderEntrustId(), BusinessSubType.SWAP_OPEN_CANCEL,"");
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
    public void setContractCoinService(ContractCoinService contractCoinService) {
        this.contractCoinService = contractCoinService;
    }

    // 设置合约订单委托服务
    public void setContractOrderEntrustService(ContractOrderEntrustService contractOrderEntrustService) {
        this.contractOrderEntrustService = contractOrderEntrustService;
    }

    // 添加处理者
    public void addHandler(MarketHandler storage) {
        handlers.add(storage);
    }

    public void setExchangePushJob(ExchangePushJob job) {
        this.exchangePushJob = job;
    }

    public void setMemberTransactionService(MemberTransactionService memberTransactionService) {
        this.memberTransactionService = memberTransactionService;
    }

    public void setMemberContractWalletService(MemberContractWalletService memberContractWalletService) {
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
    public void updateContractCoin(ContractCoin coin) {
        synchronized (contractCoin) {
            contractCoin = coin;
        }
    }



    /**
     * 获取引擎中所有用户持仓信息
     *
     * @return
     */
    public List<MemberContractWallet> getMemberContractWalletList() {
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
            MemberContractWallet wallet = memberContractWalletService.getById(walletId);
            if (wallet != null) {
                logger.info("新增用户钱包，用户：{}, 内容：{}", wallet.getMemberId(), JSON.toJSONString(wallet));
                memberContractWalletList.put(walletId,wallet);
            }
        }
    }
}
