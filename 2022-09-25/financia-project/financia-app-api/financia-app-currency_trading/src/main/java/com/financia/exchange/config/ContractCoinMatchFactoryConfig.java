package com.financia.exchange.config;

import com.financia.exchange.engine.ContractCoinMatchFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class ContractCoinMatchFactoryConfig {
    @Bean
    public ContractCoinMatchFactory getContractCoinMatchFactory() {

        ContractCoinMatchFactory factory = new ContractCoinMatchFactory();
        return factory;

    }
}
