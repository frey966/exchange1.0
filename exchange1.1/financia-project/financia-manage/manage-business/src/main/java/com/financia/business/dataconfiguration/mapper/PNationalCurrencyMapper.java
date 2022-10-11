package com.financia.business.dataconfiguration.mapper;

import com.financia.exchange.PNationalCurrency;

import java.util.List;

/**
 * 公共-法币信息Mapper接口
 * 
 * @author 花生
 * @date 2022-08-07
 */
public interface PNationalCurrencyMapper 
{
    /**
     * 查询公共-法币信息
     * 
     * @param id 公共-法币信息主键
     * @return 公共-法币信息
     */
     PNationalCurrency selectPNationalCurrencyById(Long id);

    /**
     * 查询公共-法币信息列表
     * 
     * @param pNationalCurrency 公共-法币信息
     * @return 公共-法币信息集合
     */
     List<PNationalCurrency> selectPNationalCurrencyList(PNationalCurrency pNationalCurrency);

    /**
     * 新增公共-法币信息
     * 
     * @param pNationalCurrency 公共-法币信息
     * @return 结果
     */
     int insertPNationalCurrency(PNationalCurrency pNationalCurrency);

    /**
     * 修改公共-法币信息
     * 
     * @param pNationalCurrency 公共-法币信息
     * @return 结果
     */
     int updatePNationalCurrency(PNationalCurrency pNationalCurrency);

    /**
     * 删除公共-法币信息
     * 
     * @param id 公共-法币信息主键
     * @return 结果
     */
     int deletePNationalCurrencyById(Long id);

    /**
     * 批量删除公共-法币信息
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
     int deletePNationalCurrencyByIds(Long[] ids);
}
