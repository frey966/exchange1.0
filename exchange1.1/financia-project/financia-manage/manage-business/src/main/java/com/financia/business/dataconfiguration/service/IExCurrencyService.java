package com.financia.business.dataconfiguration.service;

import com.financia.business.ExCurrency;

import java.util.List;

/**
 * 货币Service接口
 * 
 * @author 花生
 * @date 2022-08-02
 */
public interface IExCurrencyService 
{
    /**
     * 查询货币
     * 
     * @param id 货币主键
     * @return 货币
     */
    public ExCurrency selectExCurrencyById(Long id);

    /**
     * 查询货币列表
     * 
     * @param exCurrency 货币
     * @return 货币集合
     */
    public List<ExCurrency> selectExCurrencyList(ExCurrency exCurrency);

    /**
     * 新增货币
     * 
     * @param exCurrency 货币
     * @return 结果
     */
    public int insertExCurrency(ExCurrency exCurrency);

    /**
     * 修改货币
     * 
     * @param exCurrency 货币
     * @return 结果
     */
    public int updateExCurrency(ExCurrency exCurrency);

    /**
     * 批量删除货币
     * 
     * @param ids 需要删除的货币主键集合
     * @return 结果
     */
    public int deleteExCurrencyByIds(Long[] ids);

    /**
     * 删除货币信息
     * 
     * @param id 货币主键
     * @return 结果
     */
    public int deleteExCurrencyById(Long id);
}
