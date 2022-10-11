package com.financia.exchange.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.financia.currency.Currency;

import java.util.List;


/**
 * 币种service
 */
public interface CurrencyService extends IService<Currency> {

    List<Currency> querySymbolList();

    Currency getCurrencyBySymbol(String symbol);

    /**
     * 根据symbol获取交易对信息
     * @param symbol
     * @return
     */
    Currency queryCurrencyInfoBySymbol(String symbol);

}
