package com.financia.business.dataconfiguration.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.financia.business.ExCoin;
import com.financia.business.dataconfiguration.mapper.ExCoinMapper;
import com.financia.business.dataconfiguration.service.ExCoinService;
import org.springframework.stereotype.Service;


@Service("exCoinService")
public class ExCoinServiceImpl extends ServiceImpl<ExCoinMapper, ExCoin> implements ExCoinService {

}
