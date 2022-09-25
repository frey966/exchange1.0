package com.financia.business.dataconfiguration.service.impl;

import com.financia.business.dataconfiguration.mapper.ExContractOrderEntrustMapper;
import com.financia.business.dataconfiguration.service.ExContractOrderEntrustService;
import com.financia.swap.ExContractOrderEntrust;
import icu.mhb.mybatisplus.plugln.base.service.impl.JoinServiceImpl;
import org.springframework.stereotype.Service;


@Service("exContractOrderEntrustService")
public class ExContractOrderEntrustServiceImpl extends JoinServiceImpl<ExContractOrderEntrustMapper, ExContractOrderEntrust> implements ExContractOrderEntrustService {

}
