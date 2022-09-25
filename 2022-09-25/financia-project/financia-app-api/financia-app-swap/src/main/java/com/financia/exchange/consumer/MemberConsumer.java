package com.financia.exchange.consumer;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import com.financia.swap.MemberContractWallet;
import com.financia.exchange.MemberSuperContractWallet;
import com.financia.exchange.engine.ContractCoinMatchFactory;
import com.financia.exchange.service.ContractCoinService;
import com.financia.exchange.service.MemberContractWalletService;
import com.financia.exchange.service.MemberSuperContractWalletService;
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
    private MemberContractWalletService memberContractWalletService;

//    @Autowired
//    private MemberSuperContractWalletService memberSuperContractWalletService;

    @Autowired
    private ContractCoinMatchFactory contractCoinMatchFactory; // 合约引擎工厂

    @Autowired
    private ContractCoinService contractCoinService;

    /**
     * 用户注册成功，创建合约账户（此消息发送自Ucenter）
     * @param content
     */
    @KafkaListener(topics = {"member-register-swap"})
    public void handle(String content) {
        logger.info("handle member-register,data={}", content);
        if (StringUtils.isEmpty(content)) {
            return;
        }
        JSONObject json = JSON.parseObject(content);
        if(json == null) {
            return ;
        }
        //获取所有支持的币种
        List<ContractCoin> coins =  contractCoinService.findAllEnabled();
        for(ContractCoin coin:coins) {
            logger.info("memberId:{},symbol:{}",json.getLong("uid"), coin.getSymbol());
            MemberContractWallet wallet = new MemberContractWallet();
            wallet.setUsdtPattern(ContractOrderPattern.FIXED);
            wallet.setUsdtBuyPosition(BigDecimal.ZERO);
            wallet.setUsdtTotalProfitAndLoss(BigDecimal.ZERO);
            wallet.setCoinBalance(BigDecimal.ZERO);
            wallet.setCoinBuyLeverage(BigDecimal.TEN); // 10倍杠杆
            wallet.setCoinBuyPosition(BigDecimal.ZERO);
            wallet.setCoinBuyPrice(BigDecimal.ZERO);
            wallet.setCoinBuyPrincipalAmount(BigDecimal.ZERO);
            wallet.setCoinFrozenBalance(BigDecimal.ZERO);
            wallet.setCoinFrozenBuyPosition(BigDecimal.ZERO);
            wallet.setCoinFrozenSellPosition(BigDecimal.ZERO);
            wallet.setCoinPattern(ContractOrderPattern.FIXED);
            wallet.setCoinSellLeverage(BigDecimal.TEN);
            wallet.setCoinSellPosition(BigDecimal.ZERO);
            wallet.setCoinSellPrice(BigDecimal.ZERO);
            wallet.setCoinSellPrincipalAmount(BigDecimal.ZERO);
            wallet.setCoinShareNumber(coin.getShareNumber());
            wallet.setCoinTotalProfitAndLoss(BigDecimal.ZERO);
            wallet.setContractId(coin.getId());

            wallet.setMemberId(json.getLong("uid"));
            wallet.setUsdtBalance(BigDecimal.ZERO);
            wallet.setUsdtBuyLeverage(BigDecimal.TEN);
            wallet.setUsdtBuyPrice(BigDecimal.ZERO);
            wallet.setUsdtBuyPrincipalAmount(BigDecimal.ZERO);
            wallet.setUsdtFrozenBalance(BigDecimal.ZERO);
            wallet.setUsdtFrozenBuyPosition(BigDecimal.ZERO);
            wallet.setUsdtFrozenSellPosition(BigDecimal.ZERO);
            wallet.setUsdtSellLeverage(BigDecimal.TEN);
            wallet.setUsdtSellPosition(BigDecimal.ZERO);
            wallet.setUsdtSellPrice(BigDecimal.ZERO);
            wallet.setUsdtSellPrincipalAmount(BigDecimal.ZERO);
            wallet.setUsdtShareNumber(coin.getShareNumber());
            wallet.setUsdtProfit(BigDecimal.ZERO);
            wallet.setUsdtLoss(BigDecimal.ZERO);

            memberContractWalletService.save(wallet);
        }



        for(ContractCoin coin:coins) {
            logger.info("super wallet memberId:{},symbol:{}",json.getLong("uid"), coin.getSymbol());
            MemberContractWallet wallet = new MemberContractWallet();
            wallet.setUsdtPattern(ContractOrderPattern.FIXED);
            wallet.setUsdtBuyPosition(BigDecimal.ZERO);
            wallet.setUsdtTotalProfitAndLoss(BigDecimal.ZERO);
            wallet.setCoinBalance(BigDecimal.ZERO);
            wallet.setCoinBuyLeverage(BigDecimal.TEN); // 10倍杠杆
            wallet.setCoinBuyPosition(BigDecimal.ZERO);
            wallet.setCoinBuyPrice(BigDecimal.ZERO);
            wallet.setCoinBuyPrincipalAmount(BigDecimal.ZERO);
            wallet.setCoinFrozenBalance(BigDecimal.ZERO);
            wallet.setCoinFrozenBuyPosition(BigDecimal.ZERO);
            wallet.setCoinFrozenSellPosition(BigDecimal.ZERO);
            wallet.setCoinPattern(ContractOrderPattern.FIXED);
            wallet.setCoinSellLeverage(BigDecimal.TEN);
            wallet.setCoinSellPosition(BigDecimal.ZERO);
            wallet.setCoinSellPrice(BigDecimal.ZERO);
            wallet.setCoinSellPrincipalAmount(BigDecimal.ZERO);
            wallet.setCoinShareNumber(coin.getShareNumber());
            wallet.setCoinTotalProfitAndLoss(BigDecimal.ZERO);
            wallet.setContractId(coin.getId());

            wallet.setMemberId(json.getLong("uid"));
            wallet.setUsdtBalance(BigDecimal.ZERO);
            wallet.setUsdtBuyLeverage(BigDecimal.TEN);
            wallet.setUsdtBuyPrice(BigDecimal.ZERO);
            wallet.setUsdtBuyPrincipalAmount(BigDecimal.ZERO);
            wallet.setUsdtFrozenBalance(BigDecimal.ZERO);
            wallet.setUsdtFrozenBuyPosition(BigDecimal.ZERO);
            wallet.setUsdtFrozenSellPosition(BigDecimal.ZERO);
            wallet.setUsdtSellLeverage(BigDecimal.TEN);
            wallet.setUsdtSellPosition(BigDecimal.ZERO);
            wallet.setUsdtSellPrice(BigDecimal.ZERO);
            wallet.setUsdtSellPrincipalAmount(BigDecimal.ZERO);
            wallet.setUsdtShareNumber(coin.getShareNumber());
            wallet.setUsdtProfit(BigDecimal.ZERO);
            wallet.setUsdtLoss(BigDecimal.ZERO);

//            memberSuperContractWalletService.save(wallet);
        }




    }

    @KafkaListener(topics = {"member-wallet-change-1"})
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
