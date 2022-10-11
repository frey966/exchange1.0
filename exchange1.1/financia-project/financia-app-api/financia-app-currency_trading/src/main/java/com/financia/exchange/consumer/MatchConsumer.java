package com.financia.exchange.consumer;

import com.alibaba.fastjson.JSON;
import com.financia.common.redis.service.RedisService;
import com.financia.currency.ExchangeOrder;
import com.financia.exchange.engine.ContractCoinMatchFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class MatchConsumer {

    @Autowired
    private ContractCoinMatchFactory contractCoinMatchFactory; // 合约引擎工厂


    public MatchConsumer() {
    }


    /**
     * 开仓
     * @param records
     */
    @KafkaListener(topics = "trade-order-buy",containerFactory = "kafkaListenerContainerFactory")
    public void onOrderOpenSubmitted(List<ConsumerRecord<String, String>> records){
        for (int i = 0; i < records.size(); i++) {
            ConsumerRecord<String, String> record = records.get(i);
            log.info("【币币-购买订单】接收订单>>topic={},value={},size={}", record.topic(), record.value(), records.size());
            ExchangeOrder order = JSON.parseObject(record.value(), ExchangeOrder.class);
            if (order == null) {
                log.info("购买订单转化失败");
                continue;
            }
            if(checkOrderHasRecived(order)){
                continue;
            }
            // 加入处理引擎
            try {
                contractCoinMatchFactory.getContractCoinMatch(order.getSymbol()).trade(order);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }






    @Autowired
    RedisService redisService;

    private boolean checkOrderHasRecived(ExchangeOrder order){
        if(redisService.hasKey("CG_ORDER_"+order.getOrderNo())){
            log.info("订单已经接受过，阻止接受");
            return true;
        }
        redisService.setCacheObject("TRADE_ORDER_"+order.getOrderNo(),"1",60L, TimeUnit.SECONDS);
        return false;
    }


    /**
     * 撤销委托
     * @param records
     */
    @KafkaListener(topics = "trade-order-cancel",containerFactory = "kafkaListenerContainerFactory")
    public void onOrderEntrustCancel(List<ConsumerRecord<String, String>> records){
        for (int i = 0; i < records.size(); i++) {
            ConsumerRecord<String, String> record = records.get(i);
            log.info("【撤销订单】接收订单>>topic={},value={},size={}", record.topic(), record.value(), records.size());
            ExchangeOrder order = JSON.parseObject(record.value(), ExchangeOrder.class);
            if (order == null) {
                log.info("取消订单转化失败");
                continue;
            }

            // 加入处理引擎
            contractCoinMatchFactory.getContractCoinMatch(order.getSymbol()).cancel(order);
        }
    }

    /**
     * 更新合约交易对信息
     * @param content
     */
    @KafkaListener(topics = {"update-contract-coin"})
    public void onUpdateContractCoin(String content) {

    }

    /**
     * 新增合约交易对
     * @param content
     */
    @KafkaListener(topics = {"add-contract-coin"})
    public void onAddContractCoin(String content) {


            // 行情同步
//            WebSocketConnectionManage.getWebSocket().subNewCoin(coin.getSymbol());

    }



}
