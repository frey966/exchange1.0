package com.financia.business.dataconfiguration.service.impl;

import java.util.List;

import com.financia.business.dataconfiguration.mapper.PNationalCurrencyMapper;
import com.financia.business.dataconfiguration.service.IPNationalCurrencyService;
import com.financia.exchange.PNationalCurrency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 公共-法币信息Service业务层处理
 *
 * @author 花生
 * @date 2022-08-07
 */
@Service
public class IPNationalCurrencyServiceImpl implements IPNationalCurrencyService
{
    @Autowired
    private PNationalCurrencyMapper pNationalCurrencyMapper;

    /**
     * 查询公共-法币信息
     *
     * @param id 公共-法币信息主键
     * @return 公共-法币信息
     */
    @Override
    public PNationalCurrency selectPNationalCurrencyById(Long id)
    {
        return pNationalCurrencyMapper.selectPNationalCurrencyById(id);
    }

    /**
     * 查询公共-法币信息列表
     *
     * @param pNationalCurrency 公共-法币信息
     * @return 公共-法币信息
     */
    @Override
    public List<PNationalCurrency> selectPNationalCurrencyList(PNationalCurrency pNationalCurrency)
    {
        return pNationalCurrencyMapper.selectPNationalCurrencyList(pNationalCurrency);
    }

    /**
     * 新增公共-法币信息
     *
     * @param pNationalCurrency 公共-法币信息
     * @return 结果
     */
    @Override
    public int insertPNationalCurrency(PNationalCurrency pNationalCurrency)
    {
        return pNationalCurrencyMapper.insertPNationalCurrency(pNationalCurrency);
    }

    /**
     * 修改公共-法币信息
     *
     * @param pNationalCurrency 公共-法币信息
     * @return 结果
     */
    @Override
    public int updatePNationalCurrency(PNationalCurrency pNationalCurrency)
    {
        return pNationalCurrencyMapper.updatePNationalCurrency(pNationalCurrency);
    }

    /**
     * 批量删除公共-法币信息
     *
     * @param ids 需要删除的公共-法币信息主键
     * @return 结果
     */
    @Override
    public int deletePNationalCurrencyByIds(Long[] ids)
    {
        return pNationalCurrencyMapper.deletePNationalCurrencyByIds(ids);
    }

    /**
     * 删除公共-法币信息信息
     *
     * @param id 公共-法币信息主键
     * @return 结果
     */
    @Override
    public int deletePNationalCurrencyById(Long id)
    {
        return pNationalCurrencyMapper.deletePNationalCurrencyById(id);
    }
}
