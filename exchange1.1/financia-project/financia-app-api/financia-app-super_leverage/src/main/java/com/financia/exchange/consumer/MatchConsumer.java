package com.financia.exchange.consumer;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import com.financia.common.redis.service.RedisService;
import com.financia.exchange.engine.ContractCoinMatch;
import com.financia.exchange.engine.ContractCoinMatchFactory;
import com.financia.exchange.handler.MongoMarketHandler;
import com.financia.exchange.handler.WebsocketMarketHandler;
import com.financia.exchange.service.*;
import com.financia.exchange.service.impl.ContractMarketService;
import com.financia.exchange.util.DateUtil;
import com.financia.exchange.websocket.ExchangePushJob;
import com.financia.superleverage.SuperContractCoin;
import com.financia.superleverage.SuperContractOrderEntrust;
import com.financia.swap.ContractCoin;
import com.financia.swap.ContractOrderEntrust;
import com.financia.swap.MemberContractWallet;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class MatchConsumer {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ContractCoinMatchFactory contractCoinMatchFactory; // 合约引擎工厂

    @Autowired
    private SuperContractCoinService contractCoinService;

    @Autowired
    private ContractMarketService marketService;

    @Autowired
    private ExchangePushJob exchangePushJob;

    @Autowired
    private SuperContractOrderEntrustService contractOrderEntrusdtService;

    @Autowired
    private SuperMemberTransactionService memberTransactionService;
    @Autowired
    private MemberSuperContractWalletService memberContractWalletService;
    @Autowired
    private MemberRechargeRecordService memberRechargeRecordService;

    @Autowired
    private MemberAssetRecordService recordService;
    @Autowired
    private MemberBusinessWalletService walletService;

    @Autowired
    MongoMarketHandler mongoMarketHandler;

    @Autowired
    WebsocketMarketHandler wsHandler;


    public MatchConsumer() {
    }

    /**
     * 开仓
     *
     * @param records
     */
    @KafkaListener(topics = "super-swap-order-open", containerFactory = "kafkaListenerContainerFactory")
    public void onOrderOpenSubmitted(List<ConsumerRecord<String, String>> records) {
        for (int i = 0; i < records.size(); i++) {
            ConsumerRecord<String, String> record = records.get(i);
            log.info("【开仓订单-1】接收订单>>topic={},value={},size={}", record.topic(), record.value(), records.size());
            SuperContractOrderEntrust order = JSON.parseObject(record.value(), SuperContractOrderEntrust.class);
            if (order == null) {
                log.info("开仓订单转化失败");
                continue;
            }
            if (checkOrderHasRecived(order)) {
                continue;
            }
            // 加入处理引擎
            try {
                contractCoinMatchFactory.getContractCoinMatch(order.getSymbol()).trade(order);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 加仓
     *
     * @param records
     */
    @KafkaListener(topics = "super-swap-order-add1", containerFactory = "kafkaListenerContainerFactory")
    public void onOrderAddSubmitted(List<ConsumerRecord<String, String>> records) {
        for (int i = 0; i < records.size(); i++) {
            ConsumerRecord<String, String> record = records.get(i);
            log.info("【加仓订单】接收订单>>topic={},value={},size={}", record.topic(), record.value(), records.size());
            SuperContractOrderEntrust order = JSON.parseObject(record.value(), SuperContractOrderEntrust.class);
            if (order == null) {
                log.info("开仓订单转化失败");
                continue;
            }
            if (checkOrderHasRecived(order)) {
                continue;
            }
            // 加入处理引擎
            try {
                contractCoinMatchFactory.getContractCoinMatch(order.getSymbol()).trade(order);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 平仓
     *
     * @param records
     */
    @KafkaListener(topics = "super-swap-order-close25", containerFactory = "kafkaListenerContainerFactory")
    public void onOrderCloseSubmitted(List<ConsumerRecord<String, String>> records) {
        log.info("kafka接收到消息---当前时间是"+ DateUtil.getDateTime());
        for (int i = 0; i < records.size(); i++) {
            ConsumerRecord<String, String> record = records.get(i);
            log.info("【平仓订单】接收订单>>topic={},value={},size={}", record.topic(), record.value(), records.size());
            SuperContractOrderEntrust order = JSON.parseObject(record.value(), SuperContractOrderEntrust.class);
            if (order == null) {
                log.info("平仓订单转化失败");
                continue;
            }
            if (checkOrderHasRecived(order)) {
                continue;
            }
            // 加入处理引擎
            try {
                contractCoinMatchFactory.getContractCoinMatch(order.getSymbol()).trade(order);
                log.info("kafka处理消息结束---当前时间是"+ DateUtil.getDateTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 减仓
     *
     * @param records
     */
    @KafkaListener(topics = "super-swap-order-sub", containerFactory = "kafkaListenerContainerFactory")
    public void onOrderSubSubmitted(List<ConsumerRecord<String, String>> records) {
        for (int i = 0; i < records.size(); i++) {
            ConsumerRecord<String, String> record = records.get(i);
            log.info("【减仓订单】接收订单>>topic={},value={},size={}", record.topic(), record.value(), records.size());
            SuperContractOrderEntrust order = JSON.parseObject(record.value(), SuperContractOrderEntrust.class);
            if (order == null) {
                log.info("开仓订单转化失败");
                continue;
            }
            if (checkOrderHasRecived(order)) {
                continue;
            }
            // 加入处理引擎
            try {
                contractCoinMatchFactory.getContractCoinMatch(order.getSymbol()).trade(order);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    @Autowired
    RedisService redisService;

    private boolean checkOrderHasRecived(SuperContractOrderEntrust order) {
        if (redisService.hasKey("CG_SUPER_ORDER_" + order.getContractOrderEntrustId())) {
            log.info("订单已经接受过，阻止接受");
            return true;
        }
        redisService.setCacheObject("CG_SUPER_ORDER_" + order.getContractOrderEntrustId(), "1", 2L, TimeUnit.SECONDS);
        return false;
    }

    /**
     * 设置止盈止损
     *
     * @param records
     */
    @KafkaListener(topics = "super-swap-order-set-stop", containerFactory = "kafkaListenerContainerFactory")
    public void onOrderSetStopSubmitted(List<ConsumerRecord<String, String>> records) {
        for (int i = 0; i < records.size(); i++) {
            ConsumerRecord<String, String> record = records.get(i);
            log.info("【止盈订单】接收订单>>topic={},value={},size={}", record.topic(), record.value(), records.size());
            SuperContractOrderEntrust order = JSON.parseObject(record.value(), SuperContractOrderEntrust.class);

            if (order == null) {
                log.info("开仓订单转化失败");
                continue;
            }

            if (checkOrderHasRecived(order)) {
                continue;
            }

            // 加入处理引擎
            try {
                contractCoinMatchFactory.getContractCoinMatch(order.getSymbol()).trade(order);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }


    @KafkaListener(topics = "super-swap-order-check-blast", containerFactory = "kafkaListenerContainerFactory")
    public void onCheckBlast(List<ConsumerRecord<String, String>> records) {
        for (int i = 0; i < records.size(); i++) {
            ConsumerRecord<String, String> record = records.get(i);
            log.info("【检测爆单】接收订单>>topic={},value={},size={}", record.topic(), record.value(), records.size());
            MemberContractWallet order = JSON.parseObject(record.value(), MemberContractWallet.class);
            if (order == null) {
                log.info("开仓订单转化失败");
                continue;
            }

            // 加入处理引擎

            contractCoinMatchFactory.getContractCoinMatch(order.getSymbol()).checkBlashTest(order);

        }
    }

    /**
     * 撤销委托
     *
     * @param records
     */
    @KafkaListener(topics = "super-swap-order-cancel", containerFactory = "kafkaListenerContainerFactory")
    public void onOrderEntrustCancel(List<ConsumerRecord<String, String>> records) {
        for (int i = 0; i < records.size(); i++) {
            ConsumerRecord<String, String> record = records.get(i);
            log.info("【撤销订单】接收订单>>topic={},value={},size={}", record.topic(), record.value(), records.size());
            SuperContractOrderEntrust order = JSON.parseObject(record.value(), SuperContractOrderEntrust.class);
            if (order == null) {
                log.info("取消订单转化失败");
                continue;
            }
//            if(checkOrderHasRecived(order)){
//                continue;
//            }
            // 加入处理引擎
            contractCoinMatchFactory.getContractCoinMatch(order.getSymbol()).cancelContractOrderEntrust(order, false);
        }
    }

    /**
     * 更新合约交易对信息
     *
     * @param content
     */
    @KafkaListener(topics = {"super-update-contract-coin"})
    public void onUpdateContractCoin(String content) {
        if (StringUtils.isEmpty(content)) {
            return;
        }
        JSONObject json = JSON.parseObject(content);
        if (json == null) {
            return;
        }
        Long contractCoinId = json.getLong("cid");

        SuperContractCoin coin = contractCoinService.getById(contractCoinId);
        if (coin != null) {
            contractCoinMatchFactory.getContractCoinMatch(coin.getSymbol()).updateContractCoin(coin);
        }
    }

    /**
     * 新增合约交易对
     *
     * @param content
     */
    @KafkaListener(topics = {"super-add-contract-coin"})
    public void onAddContractCoin(String content) {
        if (StringUtils.isEmpty(content)) {
            return;
        }
        JSONObject json = JSON.parseObject(content);
        if (json == null) {
            return;
        }
        Long contractCoinId = json.getLong("id");

        SuperContractCoin coin = contractCoinService.getById(contractCoinId);
        if (coin != null) {
            // 添加引擎
            ContractCoinMatch match = new ContractCoinMatch(coin.getSymbol());
            match.setContractCoinService(contractCoinService);
            match.setContractOrderEntrustService(contractOrderEntrusdtService);
            match.setMemberTransactionService(memberTransactionService);
            match.setMemberRechargeRecordService(memberRechargeRecordService);
            match.setMemberContractWalletService(memberContractWalletService);
            match.addHandler(mongoMarketHandler);
            match.addHandler(wsHandler);
//            match.addHandler(nettyHandler);
            match.setExchangePushJob(exchangePushJob);
            match.setAssetRecordService(recordService);
            match.setMemberBusinessWalletService(walletService);
            match.run();
            contractCoinMatchFactory.addContractCoinMatch(coin.getSymbol(), match);

            // 行情同步
//            WebSocketConnectionManage.getWebSocket().subNewCoin(coin.getSymbol());
        }
    }


}
