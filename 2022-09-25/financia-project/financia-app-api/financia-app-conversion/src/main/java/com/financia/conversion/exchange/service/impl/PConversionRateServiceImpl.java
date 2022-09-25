package com.financia.conversion.exchange.service.impl;

import com.financia.conversion.exchange.mapper.PConversionRateServiceMapper;
import com.financia.conversion.exchange.service.PConversionRateService;
import com.financia.exchange.PNationalCurrency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
@Service("pConversionRateServiceMapper")
public class PConversionRateServiceImpl implements PConversionRateService {

    @Autowired
    private PConversionRateServiceMapper pConversionRateServiceMapper;
    @Override
    public List<Map> selectPconversionRate() {

        return pConversionRateServiceMapper.selectPconversionRate();
    }

    @Override
    public List<PNationalCurrency> selectPNationalCurrencyList(PNationalCurrency pNationalCurrency)
    {
        return pConversionRateServiceMapper.selectPNationalCurrencyList(pNationalCurrency);
    }

    @Override
    public List<PNationalCurrency> selectPNationalCurrencyPair(PNationalCurrency pNationalCurrency)
    {
        return pConversionRateServiceMapper.selectPNationalCurrencyPair(pNationalCurrency);
    }


}
