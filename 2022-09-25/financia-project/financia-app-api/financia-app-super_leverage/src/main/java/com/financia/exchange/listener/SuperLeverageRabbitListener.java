package com.financia.exchange.listener;

import com.alibaba.fastjson.JSONObject;
import com.financia.exchange.config.RabbitConfig;
import com.financia.exchange.engine.ContractCoinMatchFactory;
import com.financia.exchange.service.impl.ContractMarketService;
import com.financia.exchange.util.JSONUtils;
import com.financia.exchange.websocket.ExchangePushJob;
import com.financia.swap.*;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.Random;


/**
 * rabbitmq消息监听器
 */
@Slf4j
@Component
public class SuperLeverageRabbitListener {

    private double VOLUME_PERCENT = 0.13; // 火币成交量的百分比

    @Autowired
    private ContractMarketService contractMarketService;

    @Autowired
    private ExchangePushJob exchangePushJob;

    @Autowired
    private ContractCoinMatchFactory matchFactory;

    /**
     * 接收超级杠杆K线数据
     * @param msg
     * @param message
     * @param channel
     * @throws Exception
     */
    @RabbitListener(queues = {RabbitConfig.QUEUE_INFORM_KLINE})
    public void receiveSuperLeverageKlineMessage(String msg, Message message, Channel channel) throws Exception {

        //log.info("成功接收到合约U本位K线数据消息："+msg);
        JSONObject jsonObject = JSONObject.parseObject(msg);
        String id = jsonObject.getString("ch");

        StringBuilder sb = new StringBuilder(id.split("\\.")[1]);
        String symbol = sb.insert(sb.indexOf("usdt"), "/").toString().toUpperCase();

        String period = id.split("\\.")[3];

        String tick = jsonObject.getString("tick");
        if (null != tick && !"".equals(tick) && JSONUtils.isJsonObject(tick)) {

            JSONObject klineObj = JSONObject.parseObject(tick);

            BigDecimal open = klineObj.getBigDecimal("open"); // 收盘价
            BigDecimal close = klineObj.getBigDecimal("close"); // 收盘价
            BigDecimal high = klineObj.getBigDecimal("high"); // 收盘价
            BigDecimal low = klineObj.getBigDecimal("low"); // 收盘价
            BigDecimal amount = klineObj.getBigDecimal("amount"); // 收盘价
            BigDecimal vol = klineObj.getBigDecimal("vol"); // 收盘价
            int count = klineObj.getIntValue("count"); // 收盘价
            long time = klineObj.getLongValue("id");

            KLine kline = new KLine(period);
            kline.setClosePrice(close);
            kline.setCount(count);
            kline.setHighestPrice(high);
            kline.setLowestPrice(low);
            kline.setOpenPrice(open);
            kline.setTime(time);
            kline.setTurnover(amount.multiply(BigDecimal.valueOf(VOLUME_PERCENT)));
            kline.setVolume(vol.multiply(BigDecimal.valueOf(VOLUME_PERCENT)));
            if(kline.getVolume().compareTo(BigDecimal.ZERO) == 0) {
                //产生一个1-9的数
                int random = getRandom(1, 10);
                kline.setVolume(BigDecimal.valueOf(random));
            }
            KLine lastKline = contractMarketService.saveKLine(symbol, kline);

            exchangePushJob.pushTickKline(symbol, lastKline);
        }
    }

    private int getRandom(int min, int max){
        return (int)(Math.random() * (max - min)) + min;
    }

    /**
     * 接收超级杠杆市场行情概要数据
     * @param msg
     * @param message
     * @param channel
     * @throws Exception
     */
    @RabbitListener(queues = {RabbitConfig.QUEUE_INFORM_DETAIL})
    public void receiveSuperLeverageDetailMessage(String msg, Message message, Channel channel) throws Exception {

        //log.info("成功接收到合约U本位市场行情概要数据消息："+msg);
        JSONObject jsonObject = JSONObject.parseObject(msg);

        String id = "";
        if(jsonObject.containsKey("ch")) {
            id = jsonObject.getString("ch");
        }else if(jsonObject.containsKey("rep")) {
            id = jsonObject.getString("rep");
        }
        StringBuilder sb = new StringBuilder(id.split("\\.")[1]);
        String symbol = sb.insert(sb.indexOf("usdt"), "/").toString().toUpperCase();

        String tick = jsonObject.getString("tick");
        if (null != tick && !"".equals(tick) && JSONUtils.isJsonObject(tick)) {
            JSONObject detailObj = JSONObject.parseObject(tick);

            BigDecimal amount = detailObj.getBigDecimal("amount");
            BigDecimal open = detailObj.getBigDecimal("open");
            BigDecimal close = detailObj.getBigDecimal("close");
            BigDecimal high = detailObj.getBigDecimal("high");
            BigDecimal count = detailObj.getBigDecimal("count");
            BigDecimal low = detailObj.getBigDecimal("low");
            BigDecimal vol = detailObj.getBigDecimal("vol");

            CoinThumb thumb = new CoinThumb();
            thumb.setOpen(open);
            thumb.setClose(close);
            thumb.setHigh(high);
            thumb.setLow(low);
            thumb.setVolume(amount.multiply(BigDecimal.valueOf(VOLUME_PERCENT))); // 成交量
            thumb.setTurnover(vol.multiply(BigDecimal.valueOf(VOLUME_PERCENT))); // 成交额
            if(this.matchFactory!=null&&this.matchFactory.getContractCoinMatch(symbol)!=null){
                this.matchFactory.getContractCoinMatch(symbol).refreshThumb(thumb);

                // 委托触发 or 爆仓
                this.matchFactory.getContractCoinMatch(symbol).refreshPrice(close);
                //logger.info("[WebSocketHuobi] 价格更新：{}", close);
            }

        }
    }


    /**
     * 接收资金费率mq消息
     * @param msg
     * @param message
     * @param channel
     * @throws Exception
     */
    @RabbitListener(queues = {"queue_exchange_capital_rate"})
    public void receiveSwapCapitalRateMessage(String msg, Message message, Channel channel) throws Exception {
        //log.info("成功接收到合约U本位成交明细数据消息："+msg);
        JSONObject jsonObject = JSONObject.parseObject(msg);
        String utcTime = jsonObject.getString("utc_time");
        log.info("接收到资金费率mq消息：[{}]", utcTime);
        // 执行计算资金费率方法
//        if(utcTime.contains("00:00") || utcTime.contains("08:00") || utcTime.contains("16:00")) {
//            List<ContractCoin> contractCoins = contractCoinService.findAllEnabled();
//            for (ContractCoin contractCoin:contractCoins) {
//                String symbol = contractCoin.getSymbol();
//                this.matchFactory.getContractCoinMatch(symbol).transferAsset();
//            }
//        }

    }

}
