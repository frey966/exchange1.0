package com.financia.exchange.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.financia.exchange.mapper.CoinFeeMapper;
import com.financia.exchange.service.CoinFeeService;
import com.financia.swap.CoinFee;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CoinFeeServiceImpl  extends ServiceImpl<CoinFeeMapper,CoinFee> implements CoinFeeService {

    @Override
    public List<CoinFee> findAll() {
        LambdaQueryWrapper<CoinFee> queryWrapper=new LambdaQueryWrapper<>();
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public CoinFee getFee(Integer lever) {
        LambdaQueryWrapper<CoinFee> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.gt(CoinFee::getMaxLever,lever);
        queryWrapper.le(CoinFee::getMinLever,lever);
        System.out.println("sql is:"+queryWrapper.getTargetSql());
        return baseMapper.selectOne(queryWrapper);
    }
}
