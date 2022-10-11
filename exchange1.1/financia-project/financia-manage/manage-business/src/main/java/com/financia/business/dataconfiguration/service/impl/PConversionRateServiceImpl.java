package com.financia.business.dataconfiguration.service.impl;

import java.util.List;

import com.financia.business.dataconfiguration.mapper.PConversionRateMapper;
import com.financia.business.dataconfiguration.service.IPConversionRateService;
import com.financia.exchange.PConversionRate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 法币汇率Service业务层处理
 * 
 * @author 花生
 * @date 2022-08-08
 */
@Service
public class PConversionRateServiceImpl implements IPConversionRateService
{
    @Autowired
    private PConversionRateMapper pConversionRateMapper;

    /**
     * 查询法币汇率
     * 
     * @param id 法币汇率主键
     * @return 法币汇率
     */
    @Override
    public PConversionRate selectPConversionRateById(Long id)
    {
        return pConversionRateMapper.selectPConversionRateById(id);
    }

    /**
     * 查询法币汇率列表
     * 
     * @param pConversionRate 法币汇率
     * @return 法币汇率
     */
    @Override
    public List<PConversionRate> selectPConversionRateList(PConversionRate pConversionRate)
    {
        return pConversionRateMapper.selectPConversionRateList(pConversionRate);
    }

    /**
     * 新增法币汇率
     * 
     * @param pConversionRate 法币汇率
     * @return 结果
     */
    @Override
    public int insertPConversionRate(PConversionRate pConversionRate)
    {
        return pConversionRateMapper.insertPConversionRate(pConversionRate);
    }

    /**
     * 修改法币汇率
     * 
     * @param pConversionRate 法币汇率
     * @return 结果
     */
    @Override
    public int updatePConversionRate(PConversionRate pConversionRate)
    {
        return pConversionRateMapper.updatePConversionRate(pConversionRate);
    }

    /**
     * 批量删除法币汇率
     * 
     * @param ids 需要删除的法币汇率主键
     * @return 结果
     */
    @Override
    public int deletePConversionRateByIds(Long[] ids)
    {
        return pConversionRateMapper.deletePConversionRateByIds(ids);
    }

    /**
     * 删除法币汇率信息
     * 
     * @param id 法币汇率主键
     * @return 结果
     */
    @Override
    public int deletePConversionRateById(Long id)
    {
        return pConversionRateMapper.deletePConversionRateById(id);
    }
}
