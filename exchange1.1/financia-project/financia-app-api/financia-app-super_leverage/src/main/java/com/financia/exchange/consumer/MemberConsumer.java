package com.financia.exchange.consumer;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import com.financia.exchange.service.MemberSuperContractWalletService;
import com.financia.superleverage.SuperContractCoin;
import com.financia.swap.MemberContractWallet;
import com.financia.exchange.MemberSuperContractWallet;
import com.financia.exchange.engine.ContractCoinMatchFactory;
import com.financia.exchange.service.SuperContractCoinService;
import com.financia.swap.ContractCoin;
import com.financia.swap.ContractOrderPattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class MemberConsumer {
    private Logger logger = LoggerFactory.getLogger(MemberConsumer.class);

    @Autowired
    private MemberSuperContractWalletService memberContractWalletService;

//    @Autowired
//    private MemberSuperContractWalletService memberSuperContractWalletService;

    @Autowired
    private ContractCoinMatchFactory contractCoinMatchFactory; // 合约引擎工厂

    @Autowired
    private SuperContractCoinService contractCoinService;

    /**
     * 用户注册成功，创建合约账户（此消息发送自Ucenter）
     * @param content
     */
    @KafkaListener(topics = {"member-register-swap"})
    public void handle(String content) {
        logger.info("handle member-register,data={}", content);

    }

    @KafkaListener(topics = {"member-wallet-change"})
    public void memberPatternModify(String content) {
        logger.info("handle member-wallet-change={}", content);
        if (StringUtils.isEmpty(content)) {
            return;
        }
        JSONObject json = JSON.parseObject(content);
        if (json == null) {
            return;
        }
        // 更新用户持仓基础信息（仓位，杠杆等）
        contractCoinMatchFactory.getContractCoinMatch(json.getString("symbol")).memberWalletChange(json.getLong("walletId"));
    }
}
