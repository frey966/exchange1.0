package com.financia.exchange.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.financia.swap.CoinFee;

import java.util.List;

public interface CoinFeeService extends IService<CoinFee> {

    /**
     * 查询所有的手续费实体
     * */
    List<CoinFee> findAll();

    /**
     * 根据杠杆倍率找到手续费率
     *
     * */
    CoinFee getFee(Integer lever);
}
