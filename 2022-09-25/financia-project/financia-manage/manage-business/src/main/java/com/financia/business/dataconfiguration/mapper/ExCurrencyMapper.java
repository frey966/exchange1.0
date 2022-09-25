package com.financia.business.dataconfiguration.mapper;

import com.financia.business.ExCurrency;

import java.util.List;

/**
 * 货币Mapper接口
 * 
 * @author 花生
 * @date 2022-08-02
 */
public interface ExCurrencyMapper 
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
     * 删除货币
     * 
     * @param id 货币主键
     * @return 结果
     */
    public int deleteExCurrencyById(Long id);

    /**
     * 批量删除货币
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteExCurrencyByIds(Long[] ids);
}
