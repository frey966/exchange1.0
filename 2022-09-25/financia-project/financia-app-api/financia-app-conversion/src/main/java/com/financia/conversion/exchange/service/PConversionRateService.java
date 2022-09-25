package com.financia.conversion.exchange.service;


import com.financia.exchange.PNationalCurrency;

import java.util.List;
import java.util.Map;

public interface PConversionRateService {

    /**
     * 一键汇兑
     *
     * @param
     * @return
     */
    List<Map> selectPconversionRate();

    List<PNationalCurrency> selectPNationalCurrencyList(PNationalCurrency pNationalCurrency);

    List<PNationalCurrency> selectPNationalCurrencyPair(PNationalCurrency pNationalCurrency);
}
