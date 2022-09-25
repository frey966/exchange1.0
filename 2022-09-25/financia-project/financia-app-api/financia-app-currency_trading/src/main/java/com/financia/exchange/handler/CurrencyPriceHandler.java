package com.financia.exchange.handler;

import com.alibaba.fastjson.JSONObject;
import com.financia.currency.Currency;
import com.financia.exchange.engine.ContractCoinMatchFactory;
import com.financia.exchange.service.CurrencyService;
import com.financia.swap.CoinThumb;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@EnableScheduling
@Component
public class CurrencyPriceHandler {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private CurrencyService currencyService;

    @Autowired
    private ContractCoinMatchFactory matchFactory;


    /**
     * 更新撮合引擎币币交易对最新价格
     */
    @Scheduled(fixedRate = 1000)
    private void queryDetailNewPrice() {
        List<Currency> currencyList = currencyService.querySymbolList();
        currencyList.forEach(currency -> {
            String TICKER_KEY = "ticker:%s";
            Object ticker = redisTemplate.opsForValue().get(String.format(TICKER_KEY, currency.getSymbol()));
            if(ObjectUtils.isNotEmpty(ticker)) {
                CoinThumb coinThumb = JSONObject.parseObject((String)ticker, CoinThumb.class);
                // 委托触发
                if(this.matchFactory.getContractCoinMatch(currency.getPair())==null){
                    return;
                }
                this.matchFactory.getContractCoinMatch(currency.getPair()).refreshPrice(coinThumb.getClose());
            }
        });
    }
}
