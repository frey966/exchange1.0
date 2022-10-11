package com.financia.exchange.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.financia.exchange.mapper.ContractCoinMapper;
import com.financia.exchange.service.ContractCoinService;
import com.financia.swap.ContractCoin;
import com.financia.swap.ContractType;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ContractCoinServiceImpl extends ServiceImpl<ContractCoinMapper, ContractCoin> implements ContractCoinService {

    @Override
    public List<ContractCoin> findAllEnabled() {
        LambdaQueryWrapper<ContractCoin> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ContractCoin::getEnable,1);
        queryWrapper.orderByAsc(ContractCoin::getSort);
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public List<ContractCoin> findAllVisible() {
        LambdaQueryWrapper<ContractCoin> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ContractCoin::getVisible, 1);
        queryWrapper.eq(ContractCoin::getEnable,1);
        queryWrapper.orderByAsc(ContractCoin::getSort);
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public ContractCoin findBySymbol(String symbol) {
        LambdaQueryWrapper<ContractCoin> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ContractCoin::getSymbol, symbol);
        return baseMapper.selectOne(queryWrapper);
    }

    @Override
    public ContractCoin findBySymbolAndType(String symbol, ContractType type) {
        LambdaQueryWrapper<ContractCoin> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ContractCoin::getSymbol, symbol);
        queryWrapper.eq(ContractCoin::getType, type);
        return baseMapper.selectOne(queryWrapper);
    }

    @Override
    public void increaseTotalOpenFee(Long id, BigDecimal amount) {
        baseMapper.increaseOpenFee(id, amount);
    }

    @Override
    public void increaseTotalCloseFee(Long id, BigDecimal amount) {
        baseMapper.increaseCloseFee(id, amount);
    }

    public void increaseTotalLoss(Long id, BigDecimal amount) {
        baseMapper.increaseTotalLoss(id, amount);
    }

    public void increaseTotalProfit(Long id, BigDecimal amount) {
        baseMapper.increaseTotalProfit(id, amount);
    }

    @Override
    public List<ContractCoin> findHomeRecommendSymbolThumb() {
        Page<ContractCoin> page = new Page<>();
        page.setCurrent(1);
        page.setSize(3);
        LambdaQueryWrapper<ContractCoin> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ContractCoin::getVisible, 1);
        queryWrapper.eq(ContractCoin::getBaseSymbol,"USDT");
        queryWrapper.orderByAsc(ContractCoin::getSort);
        return  baseMapper.selectPage(page,queryWrapper).getRecords();
    }

}
