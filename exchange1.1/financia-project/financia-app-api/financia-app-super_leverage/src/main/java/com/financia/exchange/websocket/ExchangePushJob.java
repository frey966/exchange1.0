package com.financia.exchange.websocket;

import cn.hutool.core.collection.IterUtil;
import com.financia.exchange.MemberSuperContractWallet;
import com.financia.exchange.engine.ContractCoinMatchFactory;
import com.financia.exchange.service.MemberSuperContractWalletService;
import com.financia.exchange.service.SuperContractCoinService;
import com.financia.superleverage.SuperContractCoin;
import com.financia.swap.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class ExchangePushJob {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private MemberSuperContractWalletService memberContractWalletService;

    @Autowired
    private ContractCoinMatchFactory contractCoinMatchFactory;

    @Autowired
    private SuperContractCoinService contractCoinService;

    private Map<String,List<ContractTrade>> tradesQueue = new HashMap<>();
    private Map<String,List<TradePlate>> plateQueue = new HashMap<>();
    private Map<String,List<CoinThumb>> thumbQueue = new HashMap<>();


    // 推送消息类型
    // 1、最新成交：
    // 2、单币种行情：{"symbol":"EOS/USDT","open":2.6,"high":2.0,"low":2.9,"close":2.9,"chg":0.9,"change":0.9,"volume":9.9,"turnover":9.9,"lastDayClose":2.9,"usdRate":2.9,"baseUsdRate":1,"zone":0}"
    // 3、最新更新K线：{"openPrice":9.9,"highestPrice":9.9,"lowestPrice":9.9,"closePrice":9.9,"time":1594234140000,"period":"1min","count":15,"volume":57.9,"turnover":9.9}"
    // 4、盘口：{"minAmount":0.00200000,"highestPrice":9411.310000,"symbol":"BTC/USDT","lowestPrice":9000.2,"maxAmount":17.2,"items":[{"price":9318.2,"amount":8.2},{"price":9317.2,"amount":0.2}],"direction":"BUY"}"

    public void addTrades(String symbol, List<ContractTrade> trades){
        List<ContractTrade> list = tradesQueue.get(symbol);
        if(list == null){
            list = new ArrayList<>();
            tradesQueue.put(symbol,list);
        }
        synchronized (list) {
            list.addAll(trades);
        }
    }

    public void addThumb(String symbol, CoinThumb thumb){
        List<CoinThumb> list = thumbQueue.get(symbol);
        if(list == null){
            list = new ArrayList<>();
            thumbQueue.put(symbol,list);
        }
        synchronized (list) {
            list.add(thumb);
        }
    }

    public void addPlates(String symbol, TradePlate plate){
        List<TradePlate> list = plateQueue.get(symbol);
        if(list == null){
            list = new ArrayList<>();
            plateQueue.put(symbol,list);
        }
        synchronized (list) {
            list.add(plate);
        }
    }

    // 推送K线
    public void pushTickKline(String symbol, KLine kline){
        messagingTemplate.convertAndSend("/topic/superleverage/kline/"+symbol, kline);

    }


    @Scheduled(fixedRate = 500)
    public void pushThumb(){
        Iterator<Map.Entry<String,List<CoinThumb>>> entryIterator = thumbQueue.entrySet().iterator();
        while (entryIterator.hasNext()){
            Map.Entry<String,List<CoinThumb>> entry =  entryIterator.next();
            String symbol = entry.getKey();
            List<CoinThumb> thumbs = entry.getValue();
            if(thumbs.size() > 0){
                synchronized (thumbs) {
                    messagingTemplate.convertAndSend("/topic/superleverage/thumb",thumbs.get(thumbs.size() - 1));
                    thumbs.clear();
                }
            }
        }
    }

    /**
     * 推送会员持仓
     */
    @Scheduled(fixedRate = 1000)
    public void pushMemberPosition(){
        // 查询所有持仓不为0的数据
        List<MemberSuperContractWallet> memberContractWallets = memberContractWalletService.findByPositionIsNotZero();
        if(IterUtil.isEmpty(memberContractWallets)) {
            return;
        }
        // 按会员Id分组
        Map<Long, List<MemberSuperContractWallet>> collect = memberContractWallets.stream().collect(Collectors.groupingBy(MemberSuperContractWallet::getMemberId));
        collect.forEach((memberId, wallets)-> {
            // 计算账户权益
            for (MemberSuperContractWallet wallet : wallets) {
                SuperContractCoin contractCoin = contractCoinService.getById(wallet.getContractId());
                BigDecimal currentPrice = contractCoinMatchFactory.getContractCoinMatch(contractCoin.getSymbol()).getNowPrice();
                // 计算金本位权益（多仓 + 空仓）
                BigDecimal usdtTotalProfitAndLoss = BigDecimal.ZERO;
                // 多仓计算方法：当前持仓收益=（当前市价-开仓均价）* 持仓数量
                if (wallet.getUsdtBuyPosition().compareTo(BigDecimal.ZERO) > 0) {
                    usdtTotalProfitAndLoss = (currentPrice.subtract(wallet.getUsdtBuyPrice())).multiply(wallet.getUsdtBuyPosition()).setScale(4,BigDecimal.ROUND_DOWN);
                }
                // 空仓计算方法：当前持仓收益=（开仓均价-当前市价）*持仓数量
                if (wallet.getUsdtBuyPosition().compareTo(BigDecimal.ZERO) < 0) {
                    BigDecimal usdtBuyPosition = wallet.getUsdtBuyPosition().abs();
                    usdtTotalProfitAndLoss = (wallet.getUsdtBuyPrice().subtract(currentPrice)).multiply(usdtBuyPosition).setScale(4,BigDecimal.ROUND_DOWN);
                }
                // 计算持仓收益率：当前持仓收益/持仓本金*100%
                // 多仓
                String usdtTotalProfitAndLossRate = "0%";
                if (wallet.getUsdtBuyPosition().compareTo(BigDecimal.ZERO) > 0) {
                    usdtTotalProfitAndLossRate = usdtTotalProfitAndLoss.divide( (wallet.getUsdtBuyPosition().multiply(wallet.getUsdtBuyPrice())),4,BigDecimal.ROUND_DOWN).multiply(BigDecimal.valueOf(100)) + "%";
                }
                // 空仓
                if (wallet.getUsdtBuyPosition().compareTo(BigDecimal.ZERO) < 0) {
                    BigDecimal usdtBuyPosition = wallet.getUsdtBuyPosition().negate();
                    usdtTotalProfitAndLossRate = usdtTotalProfitAndLoss.divide( (usdtBuyPosition.multiply(wallet.getUsdtBuyPrice())),4,BigDecimal.ROUND_DOWN).multiply(BigDecimal.valueOf(100)) + "%";
                }
                // 设置交易对
                wallet.setSymbol(contractCoin.getSymbol());
                // 设置当前市价
                wallet.setCurrentPrice(currentPrice);
                // 当前持仓收益
                wallet.setUsdtTotalProfitAndLoss(usdtTotalProfitAndLoss);
                // 当前持仓收益率
                wallet.setUsdtTotalProfitAndLossRate(usdtTotalProfitAndLossRate);
            }
            // 推送websocket消息
            messagingTemplate.convertAndSend("/topic/superleverage/position/" + memberId, wallets);
        });
    }
}
