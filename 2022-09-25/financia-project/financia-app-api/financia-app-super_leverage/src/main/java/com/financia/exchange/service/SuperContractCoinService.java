package com.financia.exchange.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.financia.superleverage.SuperContractCoin;
import com.financia.swap.ContractCoin;
import com.financia.swap.ContractType;

import java.math.BigDecimal;
import java.util.List;

public interface SuperContractCoinService extends IService<SuperContractCoin> {

    /**
     * 查询所有启用的合约交易币
     * @return
     */
    List<SuperContractCoin> findAllEnabled();

    /**
     * 获取所有可显示币种
     */
    List<SuperContractCoin> findAllVisible();

    List<SuperContractCoin> findAllHostVisible();

    /**
     * 获取合约交易对，如果引入多类型合约，使用该函数的地方都需要更改为：findBySymbolAndCategory
     * @param symbol
     * @return
     */
    SuperContractCoin findBySymbol(String symbol);

    SuperContractCoin findBySymbolAndType(String symbol, ContractType type);

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
    List<SuperContractCoin> findHomeRecommendSymbolThumb();

}
