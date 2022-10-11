package com.financia.business.dataconfiguration.service;

import com.financia.exchange.PCryptoCurrency;

import java.util.List;

/**
 * 公共-加密货币Service接口
 * 
 * @author 花生
 * @date 2022-08-07
 */
public interface IPCryptoCurrencyService 
{
    /**
     * 查询公共-加密货币
     * 
     * @param id 公共-加密货币主键
     * @return 公共-加密货币
     */
    public PCryptoCurrency selectPCryptoCurrencyById(Long id);

    /**
     * 查询公共-加密货币列表
     * 
     * @param pCryptoCurrency 公共-加密货币
     * @return 公共-加密货币集合
     */
    public List<PCryptoCurrency> selectPCryptoCurrencyList(PCryptoCurrency pCryptoCurrency);

    /**
     * 新增公共-加密货币
     * 
     * @param pCryptoCurrency 公共-加密货币
     * @return 结果
     */
    public int insertPCryptoCurrency(PCryptoCurrency pCryptoCurrency);

    /**
     * 修改公共-加密货币
     * 
     * @param pCryptoCurrency 公共-加密货币
     * @return 结果
     */
    public int updatePCryptoCurrency(PCryptoCurrency pCryptoCurrency);

    /**
     * 批量删除公共-加密货币
     * 
     * @param ids 需要删除的公共-加密货币主键集合
     * @return 结果
     */
    public int deletePCryptoCurrencyByIds(Long[] ids);

    /**
     * 删除公共-加密货币信息
     * 
     * @param id 公共-加密货币主键
     * @return 结果
     */
    public int deletePCryptoCurrencyById(Long id);
}
