package com.financia.exchange.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.financia.currency.Currency;
import com.financia.exchange.mapper.CurrencyMapper;
import com.financia.exchange.service.CurrencyService;
import com.github.benmanes.caffeine.cache.Cache;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;


@Service
public class CurrencyServiceImpl extends ServiceImpl<CurrencyMapper, Currency> implements CurrencyService {

    @Resource
    Cache<String, Object> caffeineCache;

    @Override
    public List<Currency> querySymbolList() {
        return baseMapper.querySymbolList();
    }


    public Currency getCurrencyBySymbol(String symbol){
        LambdaQueryWrapper<Currency> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Currency::getSymbol,symbol);
        lambdaQueryWrapper.eq(Currency::getStatus,1);
        List<Currency> currencies=baseMapper.selectList(lambdaQueryWrapper);
        if(currencies.size()>0){
            return currencies.get(0);
        }else{
            return null;
        }
    }

    @Override
    public Currency queryCurrencyInfoBySymbol(String symbol) {
        // 先从缓存读取
        caffeineCache.getIfPresent(symbol);
        Currency currency = (Currency) caffeineCache.asMap().get(symbol);
        if(Objects.nonNull(currency)){
            return currency;
        }
        Currency currencyInfo =baseMapper.queryCurrencyInfoBySymbol(symbol);
        if (currencyInfo != null){
            caffeineCache.put(currencyInfo.getSymbol(), currencyInfo);
        }
        return currencyInfo;
    }
}
