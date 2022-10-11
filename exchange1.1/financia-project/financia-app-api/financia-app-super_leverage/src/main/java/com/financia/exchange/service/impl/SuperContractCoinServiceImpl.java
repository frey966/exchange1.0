package com.financia.exchange.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.financia.exchange.mapper.SuperContractCoinMapper;
import com.financia.exchange.service.SuperContractCoinService;
import com.financia.superleverage.SuperContractCoin;
import com.financia.swap.ContractCoin;
import com.financia.swap.ContractType;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class SuperContractCoinServiceImpl extends ServiceImpl<SuperContractCoinMapper, SuperContractCoin> implements SuperContractCoinService {

    @Override
    public List<SuperContractCoin> findAllEnabled() {
        LambdaQueryWrapper<SuperContractCoin> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SuperContractCoin::getEnable,1);
        queryWrapper.orderByAsc(SuperContractCoin::getSort);
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public List<SuperContractCoin> findAllVisible() {
        LambdaQueryWrapper<SuperContractCoin> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SuperContractCoin::getVisible, 1);
        queryWrapper.eq(SuperContractCoin::getEnable,1);
        queryWrapper.orderByAsc(SuperContractCoin::getSort);
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public List<SuperContractCoin> findAllHostVisible() {
        LambdaQueryWrapper<SuperContractCoin> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SuperContractCoin::getVisible, 1);
        queryWrapper.eq(SuperContractCoin::getEnable,1);
        queryWrapper.eq(SuperContractCoin::getPopular,1);
        queryWrapper.orderByAsc(SuperContractCoin::getSort);
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public SuperContractCoin findBySymbol(String symbol) {
        LambdaQueryWrapper<SuperContractCoin> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SuperContractCoin::getSymbol, symbol);
        return baseMapper.selectOne(queryWrapper);
    }

    @Override
    public SuperContractCoin findBySymbolAndType(String symbol, ContractType type) {
        LambdaQueryWrapper<SuperContractCoin> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SuperContractCoin::getSymbol, symbol);
        queryWrapper.eq(SuperContractCoin::getType, type);
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
    public List<SuperContractCoin> findHomeRecommendSymbolThumb() {
        Page<SuperContractCoin> page = new Page<>();
        page.setCurrent(1);
        page.setSize(3);
        LambdaQueryWrapper<SuperContractCoin> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SuperContractCoin::getVisible, 1);
        queryWrapper.eq(SuperContractCoin::getBaseSymbol,"USDT");
        queryWrapper.orderByAsc(SuperContractCoin::getSort);
        return  baseMapper.selectPage(page,queryWrapper).getRecords();
    }

}
