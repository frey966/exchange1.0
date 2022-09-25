package com.financia.business.dataconfiguration.service.impl;

import com.financia.business.dataconfiguration.mapper.ExExchangeOrderMapper;
import com.financia.business.dataconfiguration.service.ExExchangeOrderService;
import com.financia.currency.ExchangeOrder;
import icu.mhb.mybatisplus.plugln.base.service.impl.JoinServiceImpl;
import org.springframework.stereotype.Service;


@Service("exExchangeOrderService")
public class ExExchangeOrderServiceImpl extends JoinServiceImpl<ExExchangeOrderMapper, ExchangeOrder> implements ExExchangeOrderService {

}
