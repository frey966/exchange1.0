package com.financia.exchange.config;

import com.financia.exchange.engine.ContractCoinMatch;
import com.financia.exchange.engine.ContractCoinMatchFactory;
import com.financia.exchange.handler.MongoMarketHandler;
import com.financia.exchange.handler.WebsocketMarketHandler;
import com.financia.exchange.service.*;
import com.financia.exchange.websocket.ExchangePushJob;
import com.financia.swap.ContractCoin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ContractCoinMatchStarter implements ApplicationListener<ContextRefreshedEvent> {

    private Logger log = LoggerFactory.getLogger(ContractCoinMatchStarter.class);

    @Autowired
    private ContractCoinService contractCoinService;

    @Autowired
    private ContractOrderEntrustService contractOrderEntrusdtService;

    @Autowired
    private MemberTransactionService memberTransactionService;
    @Autowired
    private MemberContractWalletService memberContractWalletService;

    @Autowired
    private MemberRechargeRecordService memberRechargeRecordService;

    @Autowired
    MongoMarketHandler mongoMarketHandler;

    @Autowired
    WebsocketMarketHandler wsHandler;

    @Autowired
    ExchangePushJob exchangePushJob;

//    @Autowired
//    NettyHandler nettyHandler;

    @Autowired
    private ContractCoinMatchFactory factory;

    @Autowired
    private MemberBusinessWalletService walletService;

    @Autowired
    private MemberAssetRecordService assetRecordService;

    @Autowired
    private MemberBusinessWalletRecordService memberBusinessWalletRecordService;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {

        log.info("=================== 合约应用启动初始化 ===================");
        List<ContractCoin> contractCoinList = contractCoinService.findAllEnabled();

        for(ContractCoin coin : contractCoinList) {
            ContractCoinMatch match = new ContractCoinMatch(coin.getSymbol());
            match.setContractCoinService(contractCoinService);
            match.setContractOrderEntrustService(contractOrderEntrusdtService);
            match.setMemberTransactionService(memberTransactionService);
            match.setMemberContractWalletService(memberContractWalletService);

            match.setMemberRechargeRecordService(memberRechargeRecordService);
            match.setExchangePushJob(exchangePushJob);
            match.addHandler(mongoMarketHandler);
            match.addHandler(wsHandler);
            match.setWalletService(walletService);
            match.setAssetRecordService(assetRecordService);
//            match.addHandler(nettyHandler);
            match.run();
            factory.addContractCoinMatch(coin.getSymbol(), match);
        }

        log.info("========== 交易对数量：{}", factory.getMatchMap().size());
    }
}
