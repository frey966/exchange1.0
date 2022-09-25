package com.financia.business.dataconfiguration.service.impl;

import java.util.List;

import com.financia.business.dataconfiguration.mapper.ExCurrencyMapper;
import com.financia.business.dataconfiguration.service.IExCurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.financia.business.ExCurrency;

/**
 * 货币Service业务层处理
 * 
 * @author 花生
 * @date 2022-08-02
 */
@Service
public class ExCurrencyServiceImpl implements IExCurrencyService
{
    @Autowired
    private ExCurrencyMapper exCurrencyMapper;

    /**
     * 查询货币
     * 
     * @param id 货币主键
     * @return 货币
     */
    @Override
    public ExCurrency selectExCurrencyById(Long id)
    {
        return exCurrencyMapper.selectExCurrencyById(id);
    }

    /**
     * 查询货币列表
     * 
     * @param exCurrency 货币
     * @return 货币
     */
    @Override
    public List<ExCurrency> selectExCurrencyList(ExCurrency exCurrency)
    {
        return exCurrencyMapper.selectExCurrencyList(exCurrency);
    }

    /**
     * 新增货币
     * 
     * @param exCurrency 货币
     * @return 结果
     */
    @Override
    public int insertExCurrency(ExCurrency exCurrency)
    {
        return exCurrencyMapper.insertExCurrency(exCurrency);
    }

    /**
     * 修改货币
     * 
     * @param exCurrency 货币
     * @return 结果
     */
    @Override
    public int updateExCurrency(ExCurrency exCurrency)
    {
        return exCurrencyMapper.updateExCurrency(exCurrency);
    }

    /**
     * 批量删除货币
     * 
     * @param ids 需要删除的货币主键
     * @return 结果
     */
    @Override
    public int deleteExCurrencyByIds(Long[] ids)
    {
        return exCurrencyMapper.deleteExCurrencyByIds(ids);
    }

    /**
     * 删除货币信息
     * 
     * @param id 货币主键
     * @return 结果
     */
    @Override
    public int deleteExCurrencyById(Long id)
    {
        return exCurrencyMapper.deleteExCurrencyById(id);
    }
}
