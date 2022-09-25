package com.financia.exchange.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.financia.exchange.common.Constant;
import com.financia.exchange.config.RabbitConfig;
import com.financia.exchange.dto.*;
import com.financia.exchange.service.DepthService;
import com.financia.exchange.service.KlineService;
import com.financia.swap.KLine;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Future;

@Slf4j
@Component
public class ReceiveHandler {

    @Resource
    private KlineService klineService;

    @Resource
    private DepthService depthService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Resource
    private RedisTemplate redisTemplate;

    private String topicOfSymbol = "SYMBOL_THUMB";

    private double VOLUME_PERCENT = 0.13; // 火币成交量的百分比

    /**
     * 接收并处理K线数据(存储在mongodb中)
     * @param msg
     * @param message
     * @param channel
     * @throws Exception
     */
    @RabbitListener(queues = {RabbitConfig.QUEUE_INFORM_KLINE})
    public void receiveKline(String msg, Message message, Channel channel) throws Exception {
        JSONObject json=JSONObject.parseObject(msg);
        String ch = json.getString("ch");
        String[] str = ch.split("\\.");
        String symbol = str[1];
        String kType = str[3];
        String pair = symbol.split("usdt")[0].toUpperCase() + "/USDT";
        KlineDO klineDO = JSON.parseObject(json.getString("tick"), KlineDO.class);
        Long time=Long.parseLong(klineDO.getId());
        klineDO.setSymbol(symbol);
        klineDO.setkType(kType);
        klineDO.setType("kline");
        klineDO.setPair(pair);
        klineDO.setkTime(Integer.valueOf(klineDO.getId()));
        klineDO.setId(symbol + klineDO.getId() + kType);

        KLine kLine=new KLine();
        kLine.setVolume(BigDecimal.valueOf(klineDO.getVol()).multiply(BigDecimal.valueOf(VOLUME_PERCENT)));
        kLine.setCount(klineDO.getCount());
        kLine.setClosePrice(BigDecimal.valueOf(klineDO.getClose()).setScale(4,BigDecimal.ROUND_HALF_DOWN));
        kLine.setCount(klineDO.getCount());
        kLine.setHighestPrice(BigDecimal.valueOf(klineDO.getHigh()).setScale(4,BigDecimal.ROUND_HALF_DOWN));
        kLine.setLowestPrice(BigDecimal.valueOf(klineDO.getLow()).setScale(4,BigDecimal.ROUND_HALF_DOWN));
        kLine.setOpenPrice(BigDecimal.valueOf(klineDO.getOpen()).setScale(4,BigDecimal.ROUND_HALF_DOWN));
        kLine.setPeriod(klineDO.getkType());
        kLine.setTime(time);
        kLine.setTurnover(BigDecimal.valueOf(klineDO.getClose()).setScale(4,BigDecimal.ROUND_HALF_DOWN));
        messagingTemplate.convertAndSend("/topic/market/kline/"+pair,kLine);

        klineHandle(klineDO);
    }

    /**
     * 接收并处理交易深度数据(存储在mongodb中)
     * @param msg
     * @param message
     * @param channel
     */
    @RabbitListener(queues = {RabbitConfig.QUEUE_INFORM_DEPTH})
    public void receiveDepth(String msg, Message message, Channel channel) {
        depthHandle(JSONObject.parseObject(msg));
    }

    /**
     * 接收并处理 买一卖一价 数据 （存储在mongodb中）
     * @param msg
     * @param message
     * @param channel
     */
    @RabbitListener(queues = {RabbitConfig.QUEUE_INFORM_BBO})
    public void receiveBbo(String msg, Message message, Channel channel) {
        // bboHandle(JSONObject.parseObject(msg));
        return;
    }

    /**
     * 接收并处理交易行情数据 （只做了推送，未保存）
     * @param msg
     * @param message
     * @param channel
     */
    @RabbitListener(queues = {RabbitConfig.QUEUE_INFORM_TRADE})
    public void receiveTrade(String msg, Message message, Channel channel) {
        tradeHandle(JSONObject.parseObject(msg));
    }

    /**
     * 接收并处理详情数据（存储在redis中）
     * @param msg
     * @param message
     * @param channel
     */
    @RabbitListener(queues = {RabbitConfig.QUEUE_INFORM_DETAIL})
    public void receiveDetail(String msg, Message message, Channel channel) {
        detailHandle(JSONObject.parseObject(msg));
    }

    /**
     * K线处理器
     *
     * @param klineDO
     */
    @Async("taskExecutor")
    Future<String> klineHandle(KlineDO klineDO) {
        klineService.updateKline(klineDO);
        return new AsyncResult<>("klineHandle Task1");

    }

    /**
     * 深度处理器
     * @param json
     * @return
     */
    @Async("taskExecutor")
    Future<String> depthHandle(JSONObject json) {
        String ch = json.getString("ch");
        String[] str = ch.split("\\.");
        String symbol = str[1];
        String kType = str[3];
        String pair = symbol.split("usdt")[0].toUpperCase() + "/USDT";

        JSONObject jsonObject = (JSONObject) JSONObject.parse(json.getString("tick"));
        DepthDO depthDO = new DepthDO();
        depthDO.setSymbol(symbol);
        depthDO.setType("depth");
        depthDO.setPair(pair);
        depthDO.setKType(kType);
        depthDO.setAsks(JSON.parseObject(JSON.toJSONString(jsonObject.get("asks")), float[][].class));
        depthDO.setBids(JSON.parseObject(JSON.toJSONString(jsonObject.get("bids")), float[][].class));
        depthDO.setTs((Long) jsonObject.get("ts"));
        depthDO.setId(symbol.concat(kType));
        //重新组装下
        TradePlate sellTradePlate=new TradePlate();
        TradePlate buyTradePlate=new TradePlate();
        sellTradePlate.setDirection(ContractOptionOrderDirection.SELL);
        float[][] asks = depthDO.getAsks();
        ArrayList<TradePlateItem> asksList = new ArrayList<>(asks.length);
        for (float[] foo : asks) {
            TradePlateItem tradePlateItem=new TradePlateItem();
            tradePlateItem.setPrice(BigDecimal.valueOf(foo[0]).setScale(4,BigDecimal.ROUND_HALF_DOWN));
            tradePlateItem.setAmount(BigDecimal.valueOf(foo[1]).setScale(4,BigDecimal.ROUND_HALF_DOWN));
            asksList.add(tradePlateItem);
        }
        sellTradePlate.setItems(asksList);
        sellTradePlate.setSymbol(pair);
        sellTradePlate.setItems(asksList);

        float[][] bids = depthDO.getBids();
        ArrayList<TradePlateItem> bidsList = new ArrayList<>(bids.length);
        for (float[] foo : bids) {
            TradePlateItem tradePlateItem=new TradePlateItem();
            tradePlateItem.setPrice(BigDecimal.valueOf(foo[0]).setScale(4,BigDecimal.ROUND_HALF_DOWN));
            tradePlateItem.setAmount(BigDecimal.valueOf(foo[1]).setScale(4,BigDecimal.ROUND_HALF_DOWN));
            bidsList.add(tradePlateItem);
        }
        buyTradePlate.setDirection(ContractOptionOrderDirection.BUY);
        buyTradePlate.setItems(bidsList);
        buyTradePlate.setSymbol(pair);
        messagingTemplate.convertAndSend("/topic/market/trade-plate/"+pair,sellTradePlate.toJSON(20));
        messagingTemplate.convertAndSend("/topic/market/trade-plate/"+pair,buyTradePlate.toJSON(20));
        messagingTemplate.convertAndSend("/topic/market/trade-depth/" + symbol, sellTradePlate.toJSON(50));
        messagingTemplate.convertAndSend("/topic/market/trade-depth/" + symbol, buyTradePlate.toJSON(50));
//        //推送盘口
//        hawkPushService.pushMsg(NettyCacheUtils.getChannel(pair),NettyCommand.PUSH_EXCHANGE_PLATE, sellTradePlate.toJSON(24).toJSONString().getBytes());
//        hawkPushService.pushMsg(NettyCacheUtils.getChannel(pair),NettyCommand.PUSH_EXCHANGE_PLATE, buyTradePlate.toJSON(24).toJSONString().getBytes());
//        //推送深度
//        hawkPushService.pushMsg(NettyCacheUtils.getChannel(pair),NettyCommand.PUSH_EXCHANGE_DEPTH, sellTradePlate.toJSON(50).toJSONString().getBytes());
//        hawkPushService.pushMsg(NettyCacheUtils.getChannel(pair),NettyCommand.PUSH_EXCHANGE_DEPTH, buyTradePlate.toJSON(50).toJSONString().getBytes());

        depthService.updateDepth(depthDO);


        return new AsyncResult<>("klineHandle Task2");
    }


    /**
     * 24小时成交详情处理器
     *
     * @param json
     */
    @Async("taskExecutor")
    void detailHandle(JSONObject json) {
        String ch = json.getString("ch");
        String[] str = ch.split("\\.");
        String symbol = str[1];
        String pair = symbol.split("usdt")[0].toUpperCase() + "/USDT";
        DetailDO detailDO = JSON.parseObject(json.getString("tick"), DetailDO.class);
        float open=detailDO.getOpen();
        if(Objects.nonNull(detailDO)) {

            float diff = detailDO.getClose() - open;
            float percent = diff / detailDO.getOpen() *100;
            CoinThumb coinThumb=new CoinThumb();
            coinThumb.setSymbol(pair);
            coinThumb.setChange(BigDecimal.valueOf(diff));
            coinThumb.setVolume(BigDecimal.valueOf(detailDO.getVol()).multiply(BigDecimal.valueOf(VOLUME_PERCENT)));
            coinThumb.setChg(BigDecimal.valueOf(percent).setScale(2, BigDecimal.ROUND_HALF_DOWN));
            coinThumb.setHigh(BigDecimal.valueOf(detailDO.getHigh()).setScale(4, BigDecimal.ROUND_HALF_DOWN));
            coinThumb.setLow(BigDecimal.valueOf(detailDO.getLow()).setScale(4, BigDecimal.ROUND_HALF_DOWN));
            coinThumb.setOpen(BigDecimal.valueOf(open).setScale(4, BigDecimal.ROUND_HALF_DOWN));
            coinThumb.setClose(BigDecimal.valueOf(detailDO.getClose()).setScale(4, BigDecimal.ROUND_HALF_DOWN));
            coinThumb.setBaseUsdRate(BigDecimal.ZERO);
            redisTemplate.opsForValue().set(String.format(Constant.TICKER_KEY, symbol), JSON.toJSONString(coinThumb));
            messagingTemplate.convertAndSend("/topic/market/thumb",coinThumb);
        }
    }

    /**
     * 买一卖一处理器
     *
     * @param json
     */
    @Async("taskExecutor")
    void bboHandle(JSONObject json) {
        String ch = json.getString("ch");
        String[] str = ch.split("\\.");
        String symbol = str[1];
        String kType = str[2];
        String pair = symbol.split("usdt")[0].toUpperCase() + "/USDT";
        JSONObject jsonObject = (JSONObject) JSONObject.parse(json.getString("tick"));
        BboDO bboDO = new BboDO();
        bboDO.setSymbol(symbol);
        bboDO.setType("bbo");
        bboDO.setPair(pair);
        bboDO.setAsk(Float.valueOf(jsonObject.get("ask").toString()));
        bboDO.setBid(Float.valueOf(jsonObject.get("bid").toString()));
        bboDO.setId(symbol.concat(kType));
        depthService.updateBbo(bboDO);
    }

    /**
     * 成交记录处理器
     * @param json
     */
    @Async("taskExecutor")
    void tradeHandle(JSONObject json) {
        String ch = json.getString("ch");
        String[] str = ch.split("\\.");
        String symbol = str[1];
        String pair = symbol.split("usdt")[0].toUpperCase() + "/USDT";
        JSONObject jsonObjectTick = (JSONObject) JSONObject.parse(json.getString("tick"));
        JSONObject jsonObject= (JSONObject) JSON.parseArray(jsonObjectTick.get("data").toString()).get(0);
        ExchangeTrade exchangeTrade=new ExchangeTrade();
        exchangeTrade.setDirection(jsonObject.get("direction").toString().toUpperCase());
        exchangeTrade.setAmount(BigDecimal.valueOf(Float.valueOf(jsonObject.get("amount").toString())));
        BigDecimal price=BigDecimal.valueOf(Float.valueOf(jsonObject.get("price").toString()));
        if(exchangeTrade.getDirection().equals("BUY")) {
            exchangeTrade.setBuyOrderId(jsonObject.get("tradeId").toString());
            exchangeTrade.setBuyTurnover(price);
        }else {
            exchangeTrade.setSellOrderId(jsonObject.get("tradeId").toString());
            exchangeTrade.setSellTurnover(price);
        }
        exchangeTrade.setSymbol(pair);
        exchangeTrade.setPrice(price.setScale(4, BigDecimal.ROUND_HALF_DOWN));
        exchangeTrade.setTime(Long.parseLong(jsonObject.get("ts").toString()));
        List<ExchangeTrade> exchangeTrades=new ArrayList<>();
        exchangeTrades.add(exchangeTrade);
        messagingTemplate.convertAndSend("/topic/market/trade/"+pair,exchangeTrades);
        //hawkPushService.pushMsg(NettyCacheUtils.getChannel(symbol),NettyCommand.PUSH_EXCHANGE_TRADE,JSONObject.toJSONString(exchangeTrade).getBytes());


    }


}
