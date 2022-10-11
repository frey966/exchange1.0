package com.financia.exchange.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.financia.superleverage.SuperCoinFee;
import com.financia.swap.CoinFee;

import java.util.List;

public interface SuperCoinFeeService extends IService<SuperCoinFee> {

    /**
     * 查询所有的手续费实体
     * */
    List<SuperCoinFee> findAll();

    /**
     * 根据杠杆倍率找到手续费率
     *
     * */
    SuperCoinFee getFee(Integer lever);
}
