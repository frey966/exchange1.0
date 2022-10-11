package com.financia.exchange.config;

import com.financia.currency.Currency;
import com.financia.exchange.engine.ContractCoinMatch;
import com.financia.exchange.engine.ContractCoinMatchFactory;
import com.financia.exchange.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ContractCoinMatchStarter implements ApplicationRunner {

    private Logger log = LoggerFactory.getLogger(ContractCoinMatchStarter.class);

    @Autowired
    private CurrencyService currencyService;

    @Autowired
    private ContractCoinMatchFactory factory;


    @Autowired
    private MemberBusinessWalletService memberBusinessWalletService;

    @Autowired
    ExchangeOrderService exchangeOrderService;
    @Autowired
    ExchangeTradeService exchangeTradeService;
    @Autowired
    MemberCurrencyWalletService memberCurrencyWalletService;

    @Autowired
    MemberAssetRecordService memberAssetRecordService;



    @Override
    public void run(ApplicationArguments args) throws Exception {

        log.info("=================== 币币应用启动初始化 ===================");
        List<Currency> contractCoinList = currencyService.querySymbolList();

        for(Currency coin : contractCoinList) {
            ContractCoinMatch match = new ContractCoinMatch(coin.getSymbol());
            match.setContractCoinService(currencyService);
            match.setExchangeOrderService(exchangeOrderService);
            match.setExchangeTradeService(exchangeTradeService);
            match.setMemberBusinessWalletService(memberBusinessWalletService);
            match.setMemberCurrencyWalletService(memberCurrencyWalletService);
            match.setMemberAssetRecordService(memberAssetRecordService);
            match.run();
            factory.addContractCoinMatch(coin.getPair(), match);
        }

        log.info("========== 交易对数量：{}", factory.getMatchMap().size());
    }
}
