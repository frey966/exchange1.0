package com.financia.exchange.engine;

import com.financia.common.core.enums.BusinessSubType;
import com.financia.currency.*;
import com.financia.currency.Currency;
import com.financia.exchange.AssetRecordReasonEnum;
import com.financia.exchange.AssetRecordTypeEnum;
import com.financia.exchange.MemberBusinessWallet;
import com.financia.exchange.feign.client.RemoteMemberWalletService;
import com.financia.exchange.service.*;

import com.financia.exchange.util.DateUtil;
import com.financia.exchange.util.GeneratorUtil;
import com.financia.swap.CoinThumb;
import com.financia.swap.ContractCoin;
import com.financia.swap.ContractOrderEntrust;
import com.financia.swap.MemberAssetRecord;
import com.financia.trading.ExchangeTrade;
import org.checkerframework.checker.units.qual.A;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * 合约撮合引擎
 */

public class ContractCoinMatch {

    private Logger logger = LoggerFactory.getLogger(ContractCoinMatch.class);

    private String symbol;                                           // 交易对：BTC/USDT

    private boolean isStarted = false;                                // 是否启动完成（用于初始化时，获取一些数据库未处理的订单的，如果没获取完，不允许处理）
    private boolean isTriggerComplete = true;                        // 价格刷新是否完成，触发委托及爆仓

    private long lastUpdateTime = 0L;                                // 上次价格更新时间（主要用于控制价格刷新周期，因为websokcet获取的价格变化较快）

    private BigDecimal nowPrice=BigDecimal.ZERO;



    @Autowired
    ExchangeOrderService exchangeOrderService;

    public void setExchangeOrderService(ExchangeOrderService exchangeOrderService) {
        this.exchangeOrderService = exchangeOrderService;
    }

    @Autowired
    CurrencyService currencyService;

    public void setContractCoinService(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    private Currency contractCoin;


    RemoteMemberWalletService memberWalletService;

    public void setMemberWalletService(RemoteMemberWalletService memberWalletService) {
        this.memberWalletService = memberWalletService;
    }

    /**
     * 未完成的交易
     * */
    private List<ExchangeOrder> exchangeEntrustOrderList;

    public void setExchangeEntrustOrderList(List<ExchangeOrder> exchangeEntrustOrderList) {
        this.exchangeEntrustOrderList = exchangeEntrustOrderList;
    }

    ExchangeTradeService exchangeTradeService;

    public void setExchangeTradeService(ExchangeTradeService exchangeTradeService) {
        this.exchangeTradeService = exchangeTradeService;
    }

    MemberCurrencyWalletService memberCurrencyWalletService;

    public void setMemberCurrencyWalletService(MemberCurrencyWalletService memberCurrencyWalletService) {
        this.memberCurrencyWalletService = memberCurrencyWalletService;
    }

    private MemberBusinessWalletService memberBusinessWalletService;

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

    }

    private MemberAssetRecordService memberAssetRecordService;

    public void setMemberAssetRecordService(MemberAssetRecordService memberAssetRecordService) {
        this.memberAssetRecordService = memberAssetRecordService;
    }

    /**
     * 启动引擎，加载未处理订单
     */
    public void run() {
        logger.info(symbol + " 币币引擎启动，加载数据库订单....");
        contractCoin = currencyService.getCurrencyBySymbol(symbol);
        if (contractCoin == null) {
            logger.info(contractCoin.getPair() + "引擎启动失败，找不到币币交易对");
            return;
        }
        // 数据库查找订单，加载到列表中
        exchangeEntrustOrderList = exchangeOrderService.loadUnmatchedOrders(contractCoin.getId());
        if (exchangeEntrustOrderList != null && exchangeEntrustOrderList.size() > 0) {
            logger.info(contractCoin.getPair() + "加载订单，共计 " + exchangeEntrustOrderList.size());
            Iterator<ExchangeOrder> orderIterator = exchangeEntrustOrderList.iterator();
            while ((orderIterator.hasNext())) {
                ExchangeOrder exchangeOrder = orderIterator.next();



                if(exchangeOrder.getDirection().equals(com.financia.currency.ExchangeOrderDirection.BUY)&&exchangeOrder.getPrice().compareTo(nowPrice)>=0&&nowPrice.compareTo(BigDecimal.ZERO)>0){
                    // 10u 5u
                    this.doTrade(exchangeOrder);
                    orderIterator.remove();
                }else if(exchangeOrder.getDirection().equals(com.financia.currency.ExchangeOrderDirection.SELL)&&exchangeOrder.getPrice().compareTo(nowPrice)<=0&&nowPrice.compareTo(BigDecimal.ZERO)>0){
                    //如果是卖出，并且当前价格比委托价格小，则表示价格还没有到

                    //5u 10u

                    this.doTrade(exchangeOrder);
                    orderIterator.remove();
                }
            }
        }
        // 加载用户持仓信息
        this.isStarted = true;
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

    public void process(BigDecimal newPrice){


        Iterator<ExchangeOrder> orderIterator = exchangeEntrustOrderList.iterator();
        while ((orderIterator.hasNext())) {
            ExchangeOrder exchangeOrder = orderIterator.next();
            if(exchangeOrder.getDirection().equals(com.financia.currency.ExchangeOrderDirection.BUY)&&exchangeOrder.getPrice().compareTo(nowPrice)>=0&&nowPrice.compareTo(BigDecimal.ZERO)>0){

                //10u 5u

                logger.info("币币买单订单价格到达 {} , now price : {}",exchangeOrder,nowPrice);
                this.doTrade(exchangeOrder);
                orderIterator.remove();
            }else if(exchangeOrder.getDirection().equals(com.financia.currency.ExchangeOrderDirection.SELL)&&exchangeOrder.getPrice().compareTo(nowPrice)<=0&&nowPrice.compareTo(BigDecimal.ZERO)>0){

                //5u 10u

                //如果是卖出，并且当前价格比委托价格小，则表示价格还没有到
                logger.info("币币卖单订单加入委托 {} , now price : {}",exchangeOrder,nowPrice);
                this.doTrade(exchangeOrder);
                orderIterator.remove();
            }
        }
        this.isTriggerComplete = true;
    }


    public void doTrade(ExchangeOrder exchangeOrder){
        //如果是限价，则判断当前币的价格是否达到限价，如果否则放入委托单中
        if(exchangeOrder.getType().equals(com.financia.currency.ExchangeOrderType.LIMIT_PRICE)){

            if(exchangeOrder.getDirection().equals(com.financia.currency.ExchangeOrderDirection.BUY)&&exchangeOrder.getPrice().compareTo(nowPrice)<0){
                //如果是买入，并且当前价格比委托的价格大，则表示价格还没有到
                return;
            }else if(exchangeOrder.getDirection().equals(com.financia.currency.ExchangeOrderDirection.SELL)&&exchangeOrder.getPrice().compareTo(nowPrice)>0){
                //如果是卖出，并且当前价格比委托价格小，则表示价格还没有到
                return;
            }
        }


        //创建系统对手单
        ExchangeOrder matchOrder=new ExchangeOrder();
        matchOrder.setOrderNo(GeneratorUtil.getOrderId("M"));
        matchOrder.setAmount(exchangeOrder.getAmount());
        matchOrder.setNum(exchangeOrder.getNum());
        matchOrder.setCoinSymbol(exchangeOrder.getCoinSymbol());
        matchOrder.setBaseSymbol(exchangeOrder.getBaseSymbol());
        matchOrder.setCreateTime(DateUtil.getTimeMillis());
        matchOrder.setPrice(exchangeOrder.getPrice());
        matchOrder.setType(exchangeOrder.getType());
        matchOrder.setCoinId(exchangeOrder.getCoinId());
        matchOrder.setSymbol(exchangeOrder.getSymbol());
        matchOrder.setOrderResource(ExchangeOrderResource.ROBOT);
        matchOrder.setMemberId(0L);

        matchOrder.setDirection(exchangeOrder.getDirection().equals(com.financia.currency.ExchangeOrderDirection.BUY)? com.financia.currency.ExchangeOrderDirection.SELL: com.financia.currency.ExchangeOrderDirection.BUY);

        matchOrder.setCurrencyWalletId(exchangeOrder.getCurrencyWalletId());

        processMatch(exchangeOrder,matchOrder);
    }

    /**
     * 只与机器人做交易
     * 所以匹配的时候只需要考虑开仓即可
     * */
    @Transactional
    public void trade(ExchangeOrder exchangeOrder){
        //如果是限价，则判断当前币的价格是否达到限价，如果否则放入委托单中
        if(exchangeOrder.getType().equals(com.financia.currency.ExchangeOrderType.LIMIT_PRICE)){

            if(exchangeOrder.getDirection().equals(com.financia.currency.ExchangeOrderDirection.BUY)&&exchangeOrder.getPrice().compareTo(nowPrice)<0){
                //如果是买入，并且当前价格比委托的价格大，则表示价格还没有到
                logger.info("币币买单订单加入委托 {} , now price : {}",exchangeOrder,nowPrice);
                exchangeEntrustOrderList.add(exchangeOrder);
                return;
            }else if(exchangeOrder.getDirection().equals(com.financia.currency.ExchangeOrderDirection.SELL)&&exchangeOrder.getPrice().compareTo(nowPrice)>0){
                //如果是卖出，并且当前价格比委托价格小，则表示价格还没有到
                logger.info("币币卖单订单加入委托 {} , now price : {}",exchangeOrder,nowPrice);
                exchangeEntrustOrderList.add(exchangeOrder);
                return;
            }

        }

        doTrade(exchangeOrder);


    }

    /**
     * 添加客户资金变动记录
     * */
    public void addTransferOrder(Long memberId, BigDecimal val, String symbol, String order, AssetRecordTypeEnum typeEnum, AssetRecordReasonEnum recordReasonEnum){


        MemberAssetRecord memberAssetRecord=new MemberAssetRecord();
//
        memberAssetRecord.setMemberId(memberId);
        memberAssetRecord.setAssetType(typeEnum);
        memberAssetRecord.setComment(recordReasonEnum);
        memberAssetRecord.setAmount(val);
        memberAssetRecord.setSymbol(symbol);
        memberAssetRecord.setEntrustOrder(order);

        boolean res= memberAssetRecordService.save(memberAssetRecord);

        logger.info("save res is {}",res);

    }

    @Transactional
    public synchronized void cancel(ExchangeOrder exchangeOrder){
        Iterator<ExchangeOrder> orderIterator = exchangeEntrustOrderList.iterator();
        while ((orderIterator.hasNext())) {
            ExchangeOrder order = orderIterator.next();
            if(order.getOrderNo().equals(exchangeOrder.getOrderNo())){
                orderIterator.remove();
                break;
            }
        }
        exchangeOrderService.cancelOrder(exchangeOrder.getId());
}

    /**
     * 处理两个匹配的委托订单
     * @param focusedOrder 焦点单
     * @param matchOrder 匹配单
     * @return
     */
    private synchronized void processMatch(ExchangeOrder focusedOrder, ExchangeOrder matchOrder){
        //需要交易的数量，成交量,成交价，可用数量
        BigDecimal needNum,dealPrice,availNum;
        //如果匹配单是限价单，则以其价格为成交价
        if(matchOrder.getType().equals(com.financia.currency.ExchangeOrderType.LIMIT_PRICE)){
            dealPrice = matchOrder.getPrice();
        }
        else {
            dealPrice = focusedOrder.getPrice();
        }
        //成交价必须大于0
        if(dealPrice.compareTo(BigDecimal.ZERO) <= 0){
            return;
        }
        needNum = calculateTradedNum(focusedOrder,dealPrice);
        availNum = calculateTradedNum(matchOrder,dealPrice);
        //计算成交量
        BigDecimal tradedNum = (availNum.compareTo(needNum) >= 0 ? needNum : availNum);
        logger.info("dealPrice={},amount={}",dealPrice,tradedNum);
        //如果成交额为0说明剩余额度无法成交，退出
        if(tradedNum.compareTo(BigDecimal.ZERO) == 0){
            return;
        }

        //计算成交额,成交额要保留足够精度
        BigDecimal tradedAmount = tradedNum.multiply(dealPrice);
        matchOrder.setTradedAmount(matchOrder.getTradedAmount().add(tradedAmount));
        matchOrder.setTurnover(matchOrder.getTurnover().add(tradedNum));
        matchOrder.setCompletedTime(DateUtil.getTimeMillis());
        focusedOrder.setTradedAmount(focusedOrder.getTradedAmount().add(tradedAmount));
        focusedOrder.setTurnover(focusedOrder.getTurnover().add(tradedNum));
        focusedOrder.setCompletedTime(DateUtil.getTimeMillis());

        //创建成交记录
        ExchangeTrade exchangeTrade = new ExchangeTrade();
        exchangeTrade.setSymbol(symbol);
        exchangeTrade.setAmount(tradedAmount);
        exchangeTrade.setDirection(focusedOrder.getDirection());
        exchangeTrade.setPrice(dealPrice);
        exchangeTrade.setBuyTurnover(tradedNum);
        exchangeTrade.setSellTurnover(tradedNum);
        //校正市价单剩余成交额
        if(ExchangeOrderType.MARKET_PRICE == focusedOrder.getType() && focusedOrder.getDirection().equals(ExchangeOrderDirection.BUY)){
            BigDecimal adjustTurnover = adjustMarketOrderTurnover(focusedOrder,dealPrice);
            exchangeTrade.setBuyTurnover(tradedNum.add(adjustTurnover));
        }
        else if(ExchangeOrderType.MARKET_PRICE == matchOrder.getType() && matchOrder.getDirection().equals(ExchangeOrderDirection.BUY)){
            BigDecimal adjustTurnover = adjustMarketOrderTurnover(matchOrder,dealPrice);
            exchangeTrade.setBuyTurnover(tradedNum.add(adjustTurnover));
        }

        if (focusedOrder.getDirection().equals(ExchangeOrderDirection.BUY)) {
            exchangeTrade.setBuyOrderId(focusedOrder.getOrderNo());
            exchangeTrade.setSellOrderId(matchOrder.getOrderNo());
        } else {
            exchangeTrade.setBuyOrderId(matchOrder.getOrderNo());
            exchangeTrade.setSellOrderId(focusedOrder.getOrderNo());
        }

        exchangeTrade.setCreateTime(new Date());


        if(matchOrder.getAmount().compareTo(matchOrder.getTradedAmount())>=0){
            matchOrder.setStatus(ExchangeOrderStatus.COMPLETED);
        }

        if(focusedOrder.getAmount().compareTo(focusedOrder.getTradedAmount())>=0){
            focusedOrder.setStatus(ExchangeOrderStatus.COMPLETED);
        }

        if(matchOrder.getTurnover().compareTo(BigDecimal.ZERO)>0){
            matchOrder.setTradedPrice(matchOrder.getTradedAmount().divide(matchOrder.getTurnover(), RoundingMode.DOWN));
        }

        if(focusedOrder.getTurnover().compareTo(BigDecimal.ZERO)>0){
            focusedOrder.setTradedPrice(focusedOrder.getTradedAmount().divide(focusedOrder.getTurnover(),RoundingMode.DOWN));
        }

        exchangeOrderService.saveOrUpdate(focusedOrder);
        exchangeOrderService.saveOrUpdate(matchOrder);

        //成交之后更新余额

        if(focusedOrder.getDirection().equals(ExchangeOrderDirection.BUY)){
            //买入，则扣减主钱包冻结余额，增加币币钱包余额
            int ret = memberCurrencyWalletService.increaseBalanceByBuy(focusedOrder.getCurrencyWalletId(), focusedOrder);
        }else{
            //如果是卖出，则减少币币钱包冻结余额，增加主钱包可用余额
            MemberBusinessWallet memberBusinessWallet = memberBusinessWalletService.getMemberBusinessWalletByMemberId(focusedOrder.getMemberId());
            memberCurrencyWalletService.decreaseFreezeBalanceBySell(memberBusinessWallet.getId(),focusedOrder.getCurrencyWalletId(),focusedOrder);
        }

        exchangeTradeService.saveOrUpdate(exchangeTrade);

        this.addTransferOrder(focusedOrder.getMemberId(),focusedOrder.getTradedAmount(),focusedOrder.getSymbol(),focusedOrder.getOrderNo(),AssetRecordTypeEnum.MARKET_TRADE,focusedOrder.getDirection()==ExchangeOrderDirection.BUY?AssetRecordReasonEnum.MARKET_MACH_BUY:AssetRecordReasonEnum.MARKET_MACH_SELL);


        this.addTransferOrder(matchOrder.getMemberId(),matchOrder.getTradedAmount(),matchOrder.getSymbol(),matchOrder.getOrderNo(),AssetRecordTypeEnum.MARKET_TRADE,matchOrder.getDirection()==ExchangeOrderDirection.BUY?AssetRecordReasonEnum.MARKET_MACH_BUY:AssetRecordReasonEnum.MARKET_MACH_SELL);

    }


    /**
     * 计算委托单剩余可成交的数量
     * @param order 委托单
     * @param dealPrice 成交价
     * @return
     */
    private BigDecimal calculateTradedNum(ExchangeOrder order, BigDecimal dealPrice){
            return  order.getNum().subtract(order.getTurnover());
    }

    /**
     * 调整市价单剩余成交额，当剩余成交额不足时设置订单完成
     * @param order
     * @param dealPrice
     * @return
     */
    private BigDecimal adjustMarketOrderTurnover(ExchangeOrder order, BigDecimal dealPrice){
        if(order.getDirection().equals(ExchangeOrderDirection.BUY) && order.getType() == ExchangeOrderType.MARKET_PRICE){
            BigDecimal leftTurnover = order.getAmount().subtract(order.getTurnover());
            if(leftTurnover.divide(dealPrice,8,BigDecimal.ROUND_DOWN)
                    .compareTo(BigDecimal.ZERO)==0){
                order.setTurnover(order.getAmount());
                return leftTurnover;
            }
        }
        return BigDecimal.ZERO;
    }

    public BigDecimal getNowPrice() {
        return nowPrice;
    }
}
