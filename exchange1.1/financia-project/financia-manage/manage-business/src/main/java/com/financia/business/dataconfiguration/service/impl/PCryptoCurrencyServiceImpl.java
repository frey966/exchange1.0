package com.financia.business.dataconfiguration.service.impl;

import java.util.List;

import com.financia.business.dataconfiguration.mapper.PCryptoCurrencyMapper;
import com.financia.business.dataconfiguration.service.IPCryptoCurrencyService;
import com.financia.exchange.PCryptoCurrency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 公共-加密货币Service业务层处理
 * 
 * @author 花生
 * @date 2022-08-07
 */
@Service
public class PCryptoCurrencyServiceImpl implements IPCryptoCurrencyService
{
    @Autowired
    private PCryptoCurrencyMapper pCryptoCurrencyMapper;

    /**
     * 查询公共-加密货币
     * 
     * @param id 公共-加密货币主键
     * @return 公共-加密货币
     */
    @Override
    public PCryptoCurrency selectPCryptoCurrencyById(Long id)
    {
        return pCryptoCurrencyMapper.selectPCryptoCurrencyById(id);
    }

    /**
     * 查询公共-加密货币列表
     * 
     * @param pCryptoCurrency 公共-加密货币
     * @return 公共-加密货币
     */
    @Override
    public List<PCryptoCurrency> selectPCryptoCurrencyList(PCryptoCurrency pCryptoCurrency)
    {
        return pCryptoCurrencyMapper.selectPCryptoCurrencyList(pCryptoCurrency);
    }

    /**
     * 新增公共-加密货币
     * 
     * @param pCryptoCurrency 公共-加密货币
     * @return 结果
     */
    @Override
    public int insertPCryptoCurrency(PCryptoCurrency pCryptoCurrency)
    {
        return pCryptoCurrencyMapper.insertPCryptoCurrency(pCryptoCurrency);
    }

    /**
     * 修改公共-加密货币
     * 
     * @param pCryptoCurrency 公共-加密货币
     * @return 结果
     */
    @Override
    public int updatePCryptoCurrency(PCryptoCurrency pCryptoCurrency)
    {
        return pCryptoCurrencyMapper.updatePCryptoCurrency(pCryptoCurrency);
    }

    /**
     * 批量删除公共-加密货币
     * 
     * @param ids 需要删除的公共-加密货币主键
     * @return 结果
     */
    @Override
    public int deletePCryptoCurrencyByIds(Long[] ids)
    {
        return pCryptoCurrencyMapper.deletePCryptoCurrencyByIds(ids);
    }

    /**
     * 删除公共-加密货币信息
     * 
     * @param id 公共-加密货币主键
     * @return 结果
     */
    @Override
    public int deletePCryptoCurrencyById(Long id)
    {
        return pCryptoCurrencyMapper.deletePCryptoCurrencyById(id);
    }
}
