package com.financia.exchange.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.financia.exchange.mapper.SuperCoinFeeMapper;
import com.financia.exchange.service.SuperCoinFeeService;
import com.financia.superleverage.SuperCoinFee;
import com.financia.swap.CoinFee;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CoinFeeServiceImpl  extends ServiceImpl<SuperCoinFeeMapper, SuperCoinFee> implements SuperCoinFeeService {

    @Override
    public List<SuperCoinFee> findAll() {
        LambdaQueryWrapper<SuperCoinFee> queryWrapper=new LambdaQueryWrapper<>();
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public SuperCoinFee getFee(Integer lever) {
        LambdaQueryWrapper<SuperCoinFee> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.gt(SuperCoinFee::getMaxLever,lever);
        queryWrapper.le(SuperCoinFee::getMinLever,lever);
        System.out.println("sql is:"+queryWrapper.getTargetSql());
        return baseMapper.selectOne(queryWrapper);
    }
}
