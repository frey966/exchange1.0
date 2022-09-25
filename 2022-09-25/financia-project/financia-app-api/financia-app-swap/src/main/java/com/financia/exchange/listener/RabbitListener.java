package com.financia.exchange.listener;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.financia.exchange.engine.ContractCoinMatchFactory;
import com.financia.exchange.service.ContractCoinService;
import com.financia.exchange.service.impl.ContractMarketService;
import com.financia.exchange.util.JSONUtils;
import com.financia.exchange.websocket.ExchangePushJob;
import com.financia.swap.*;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * rabbitmq消息监听器
 */
@Slf4j
@Component
public class RabbitListener {

    private double VOLUME_PERCENT = 1; // 火币成交量的百分比

    @Autowired
    private ContractMarketService contractMarketService;

    @Autowired
    private ExchangePushJob exchangePushJob;

    @Autowired
    private ContractCoinMatchFactory matchFactory;

    @Autowired
    private ContractCoinService contractCoinService;

    /**
     * 接收合约U本位K线数据
     * @param msg
     * @param message
     * @param channel
     * @throws Exception
     */
    @org.springframework.amqp.rabbit.annotation.RabbitListener(queues = {"queue_swap_usdt_inform_kline"})
    public void receiveSwapUsdtKlineMessage(String msg, Message message, Channel channel) throws Exception {

        //log.info("成功接收到合约U本位K线数据消息："+msg);
        JSONObject jsonObject = JSONObject.parseObject(msg);
        String ch = jsonObject.getString("ch");
        // 获取交易对
        StringBuilder sb = new StringBuilder(ch.split("\\.")[1]);
        String symbol = ch.split("\\.")[1];
        symbol = symbol.replace("-", "/");
        // 获取K线时间
        String period = ch.split("\\.")[3];

        String tick = jsonObject.getString("tick");
        if (null != tick && !"".equals(tick) && JSONUtils.isJsonObject(tick)) {

            JSONObject klineObj = JSONObject.parseObject(tick);

            BigDecimal open = klineObj.getBigDecimal("open"); // 开盘价
            BigDecimal close = klineObj.getBigDecimal("close"); // 收盘价
            BigDecimal high = klineObj.getBigDecimal("high"); // 最高价
            BigDecimal low = klineObj.getBigDecimal("low"); // 最低价
            BigDecimal amount = klineObj.getBigDecimal("amount"); // 成交量(币)
            BigDecimal vol = klineObj.getBigDecimal("vol"); // 成交量(张)
            BigDecimal turnover = klineObj.getBigDecimal("trade_turnover"); // 成交额
            int count = klineObj.getIntValue("count"); // 成交笔数
            long time = klineObj.getLongValue("id"); // 时间戳

            KLine kline = new KLine(period);
            kline.setClosePrice(close);
            kline.setCount(count);
            kline.setHighestPrice(high);
            kline.setLowestPrice(low);
            kline.setOpenPrice(open);
            kline.setTime(time);
            kline.setTurnover(turnover.multiply(BigDecimal.valueOf(VOLUME_PERCENT)));
            kline.setVolume(amount.multiply(BigDecimal.valueOf(VOLUME_PERCENT)));
            // 保存k线
            KLine lastKline = contractMarketService.saveKLine(symbol, kline);
            // 推送k线数据
            exchangePushJob.pushTickKline(symbol, lastKline);
        }
    }

    /**
     * 接收合约U本位深度数据
     * @param msg
     * @param message
     * @param channel
     * @throws Exception
     */
    @org.springframework.amqp.rabbit.annotation.RabbitListener(queues = {"queue_swap_usdt_inform_depth"})
    public void receiveSwapUsdtDepthMessage(String msg, Message message, Channel channel) throws Exception {
        //log.info("成功接收到合约U本位深度数据消息："+msg);
        JSONObject jsonObject = JSONObject.parseObject(msg);
        String ch = jsonObject.getString("ch");
        // 获取交易对
        StringBuilder sb = new StringBuilder(ch.split("\\.")[1]);
        String symbol = ch.split("\\.")[1];
        symbol = symbol.replace("-", "/");

        String tick = jsonObject.getString("tick");
        if (null != tick && !"".equals(tick) && JSONUtils.isJsonObject(tick)) {

            JSONObject plateObj = JSONObject.parseObject(tick);

            // 买盘深度
            JSONArray bids = plateObj.getJSONArray("bids");
            List<TradePlateItem> buyItems = new ArrayList<>();
            if(bids!=null) {
                for(int i = 0; i < bids.size(); i++) {
                    TradePlateItem item = new TradePlateItem();
                    JSONArray itemObj = bids.getJSONArray(i);
                    item.setPrice(itemObj.getBigDecimal(0));
                    item.setAmount(itemObj.getBigDecimal(1).divide(BigDecimal.valueOf(1000)).setScale(4, BigDecimal.ROUND_UP));
                    buyItems.add(item);
                }
            }


            // 卖盘深度
            JSONArray asks = plateObj.getJSONArray("asks");
            List<TradePlateItem> sellItems = new ArrayList<>();
            if(asks!=null) {
                for(int i = 0; i < asks.size(); i++) {
                    TradePlateItem item = new TradePlateItem();
                    JSONArray itemObj = asks.getJSONArray(i);
                    item.setPrice(itemObj.getBigDecimal(0));
                    item.setAmount(itemObj.getBigDecimal(1).divide(BigDecimal.valueOf(1000)).setScale(4, BigDecimal.ROUND_UP));
                    sellItems.add(item);
                }
            }

            // 刷新盘口数据
            if(this.matchFactory!=null&&this.matchFactory.getContractCoinMatch(symbol)!=null){
                this.matchFactory.getContractCoinMatch(symbol).refreshPlate(buyItems, sellItems);
            }

            // 保存最新数据到redis中(目前不用做保存)


            //logger.info("[WebSocketHuobi] 盘口更新：bids共 {} 条，asks共 {} 条", bids.size(), asks.size());
        }
    }

    /**
     * 接收合约U本位市场行情概要数据
     * @param msg
     * @param message
     * @param channel
     * @throws Exception
     */
    @org.springframework.amqp.rabbit.annotation.RabbitListener(queues = {"queue_swap_usdt_inform_detail"})
    public void receiveSwapUsdtDetailMessage(String msg, Message message, Channel channel) throws Exception {

        //log.info("成功接收到合约U本位市场行情概要数据消息："+msg);
        JSONObject jsonObject = JSONObject.parseObject(msg);

        String ch = jsonObject.getString("ch");
        // 获取交易对
        StringBuilder sb = new StringBuilder(ch.split("\\.")[1]);
        String symbol = ch.split("\\.")[1];
        symbol = symbol.replace("-", "/");

        String tick = jsonObject.getString("tick");
        if (null != tick && !"".equals(tick) && JSONUtils.isJsonObject(tick)) {
            JSONObject detailObj = JSONObject.parseObject(tick);

            BigDecimal amount = detailObj.getBigDecimal("amount"); // 成交量(币)
            BigDecimal open = detailObj.getBigDecimal("open"); //开盘价
            BigDecimal close = detailObj.getBigDecimal("close"); // 收盘价
            BigDecimal high = detailObj.getBigDecimal("high"); //最高价
            BigDecimal count = detailObj.getBigDecimal("count"); // 成交笔数
            BigDecimal low = detailObj.getBigDecimal("low"); // 最低价
            BigDecimal vol = detailObj.getBigDecimal("vol"); // 成交量(张)
            BigDecimal turnover = detailObj.getBigDecimal("trade_turnover"); // 成交额

            CoinThumb thumb = new CoinThumb();
            thumb.setOpen(open);
            thumb.setClose(close);
            thumb.setHigh(high);
            thumb.setLow(low);
            thumb.setVolume(amount.multiply(BigDecimal.valueOf(VOLUME_PERCENT))); // 成交量
            thumb.setTurnover(turnover.multiply(BigDecimal.valueOf(VOLUME_PERCENT))); // 成交额
            if(this.matchFactory!=null&&this.matchFactory.getContractCoinMatch(symbol)!=null){

                // 刷新行情数据
                this.matchFactory.getContractCoinMatch(symbol).refreshThumb(thumb);

                // 委托触发 or 爆仓
                this.matchFactory.getContractCoinMatch(symbol).refreshPrice(close);
                //logger.info("[WebSocketHuobi] 价格更新：{}", close);
            }

        }
    }

    /**
     * 接收合约U本位成交明细数据
     * @param msg
     * @param message
     * @param channel
     * @throws Exception
     */
    @org.springframework.amqp.rabbit.annotation.RabbitListener(queues = {"queue_swap_usdt_inform_trade"})
    public void receiveSwapUsdtTradeMessage(String msg, Message message, Channel channel) throws Exception {
        //log.info("成功接收到合约U本位成交明细数据消息："+msg);
        JSONObject jsonObject = JSONObject.parseObject(msg);

        String ch = jsonObject.getString("ch");
        // 获取交易对
        StringBuilder sb = new StringBuilder(ch.split("\\.")[1]);
        String symbol = ch.split("\\.")[1];
        symbol = symbol.replace("-", "/");

        String tick = jsonObject.getString("tick");
        if (null != tick && !"".equals(tick) && JSONUtils.isJsonObject(tick)) {
            JSONObject detailObj = JSONObject.parseObject(tick);
            JSONArray tradeList = detailObj.getJSONArray("data");
            List<ContractTrade> tradeArrayList = new ArrayList<ContractTrade>();
            for(int i = 0; i < tradeList.size(); i++) {
                BigDecimal amount = tradeList.getJSONObject(i).getBigDecimal("amount"); // 交易量
                BigDecimal price = tradeList.getJSONObject(i).getBigDecimal("price"); // 交易金额
                String direction = tradeList.getJSONObject(i).getString("direction"); // 买/卖
                long time = tradeList.getJSONObject(i).getLongValue("ts"); // 时间戳
                String tradeId = tradeList.getJSONObject(i).getString("tradeId"); // 交易Id

                // 创建交易
                ContractTrade trade = new ContractTrade();
                trade.setAmount(amount);
                trade.setPrice(price);
                if(direction.equals("buy")) {
                    trade.setDirection(ContractOrderDirection.BUY);
                    trade.setBuyOrderId(tradeId);
                    trade.setBuyTurnover(amount.multiply(price));
                }else{
                    trade.setDirection(ContractOrderDirection.SELL);
                    trade.setSellOrderId(tradeId);
                    trade.setSellTurnover(amount.multiply(price));
                }
                trade.setSymbol(symbol);
                trade.setTime(time);

                tradeArrayList.add(trade);

                // 刷新成交记录
                if(this.matchFactory!=null&&this.matchFactory.getContractCoinMatch(symbol)!=null){
                    this.matchFactory.getContractCoinMatch(symbol).refreshLastedTrade(tradeArrayList);
                }
            }

            //logger.info("[WebSocketHuobi] 成交明细更新：共 {} 条", tradeArrayList.size());
        }
    }


    /**
     * 接收资金费率mq消息
     * @param msg
     * @param message
     * @param channel
     * @throws Exception
     */
    @org.springframework.amqp.rabbit.annotation.RabbitListener(queues = {"queue_exchange_capital_rate"})
    public void receiveSwapCapitalRateMessage(String msg, Message message, Channel channel) throws Exception {
        JSONObject jsonObject = JSONObject.parseObject(msg);
        String utcTime = jsonObject.getString("utc_time");
        log.info("接收到资金费率mq消息：[{}]", utcTime);
        // 执行计算资金费率方法
        if(utcTime.contains("00:00") || utcTime.contains("08:00") || utcTime.contains("16:00")) {
            List<ContractCoin> contractCoins = contractCoinService.findAllEnabled();
            for (ContractCoin contractCoin:contractCoins) {
                String symbol = contractCoin.getSymbol();
                try{
                    this.matchFactory.getContractCoinMatch(symbol).transferAsset();
                } catch (Exception e) {
                    log.info("处理[{}]资金费率出现异常", symbol);
                }

            }
        }

    }

}
