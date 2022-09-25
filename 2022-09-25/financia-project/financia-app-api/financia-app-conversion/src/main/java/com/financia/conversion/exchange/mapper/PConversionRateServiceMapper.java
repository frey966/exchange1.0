package com.financia.conversion.exchange.mapper;


import com.financia.exchange.PNationalCurrency;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface PConversionRateServiceMapper {
    /**
     *
     *
     * @param
     * @return
     */
    public List<Map> selectPconversionRate();

    List<PNationalCurrency> selectPNationalCurrencyList(PNationalCurrency pNationalCurrency);

    List<PNationalCurrency> selectPNationalCurrencyPair(PNationalCurrency pNationalCurrency);
}
