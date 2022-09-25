package com.financia.exchange.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.financia.swap.ContractCoin;
import com.financia.swap.ContractType;

import java.math.BigDecimal;
import java.util.List;

public interface ContractCoinService extends IService<ContractCoin> {

    /**
     * 查询所有启用的合约交易币
     * @return
     */
    List<ContractCoin> findAllEnabled();

    /**
     * 获取所有可显示币种
     */
    List<ContractCoin> findAllVisible();

    /**
     * 获取合约交易对，如果引入多类型合约，使用该函数的地方都需要更改为：findBySymbolAndCategory
     * @param symbol
     * @return
     */
    ContractCoin findBySymbol(String symbol);

    ContractCoin findBySymbolAndType(String symbol, ContractType type);

    /**
     * 增加平台开仓手续费
     * @param id
     * @param amount
     */
    void increaseTotalOpenFee(Long id, BigDecimal amount);

    /**
     * 增加平台平仓手续费
     * @param id
     * @param amount
     */
    void increaseTotalCloseFee(Long id, BigDecimal amount);

    /**
     * 增加平台亏损
     * @param id
     * @param amount
     */
    void increaseTotalLoss(Long id, BigDecimal amount);

    /**
     * 增加平台盈利
     * @param id
     * @param amount
     */
    void increaseTotalProfit(Long id, BigDecimal amount);

    /**
     * 获取首页推荐的币种
     * @return
     */
    List<ContractCoin> findHomeRecommendSymbolThumb();

}
