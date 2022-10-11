package com.financia.business.dataconfiguration.service;

import com.financia.exchange.PConversionRate;

import java.util.List;

/**
 * 法币汇率Service接口
 * 
 * @author 花生
 * @date 2022-08-08
 */
public interface IPConversionRateService 
{
    /**
     * 查询法币汇率
     * 
     * @param id 法币汇率主键
     * @return 法币汇率
     */
    public PConversionRate selectPConversionRateById(Long id);

    /**
     * 查询法币汇率列表
     * 
     * @param pConversionRate 法币汇率
     * @return 法币汇率集合
     */
    public List<PConversionRate> selectPConversionRateList(PConversionRate pConversionRate);

    /**
     * 新增法币汇率
     * 
     * @param pConversionRate 法币汇率
     * @return 结果
     */
    public int insertPConversionRate(PConversionRate pConversionRate);

    /**
     * 修改法币汇率
     * 
     * @param pConversionRate 法币汇率
     * @return 结果
     */
    public int updatePConversionRate(PConversionRate pConversionRate);

    /**
     * 批量删除法币汇率
     * 
     * @param ids 需要删除的法币汇率主键集合
     * @return 结果
     */
    public int deletePConversionRateByIds(Long[] ids);

    /**
     * 删除法币汇率信息
     * 
     * @param id 法币汇率主键
     * @return 结果
     */
    public int deletePConversionRateById(Long id);
}
