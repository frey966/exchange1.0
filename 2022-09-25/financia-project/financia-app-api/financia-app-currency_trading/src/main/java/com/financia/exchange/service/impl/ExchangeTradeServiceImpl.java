package com.financia.exchange.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.financia.exchange.mapper.ExchangeTradeMapper;
import com.financia.exchange.service.ExchangeTradeService;
import com.financia.trading.ExchangeTrade;
import org.springframework.stereotype.Service;

@Service
public class ExchangeTradeServiceImpl extends ServiceImpl<ExchangeTradeMapper, ExchangeTrade> implements ExchangeTradeService {
}
