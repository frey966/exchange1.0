package com.financia.business.dataconfiguration.service;

import com.financia.business.ExCoinFee;

import java.util.List;

/**
 * 合约-杠杆手续费率Service接口
 *
 * @author 花生
 * @date 2022-08-11
 */
public interface IExCoinFeeService
{
    /**
     * 查询合约-杠杆手续费率
     *
     * @param id 合约-杠杆手续费率主键
     * @return 合约-杠杆手续费率
     */
    public ExCoinFee selectExCoinFeeById(Long id);

    /**
     * 查询合约-杠杆手续费率列表
     *
     * @param exCoinFee 合约-杠杆手续费率
     * @return 合约-杠杆手续费率集合
     */
    public List<ExCoinFee> selectExCoinFeeList(ExCoinFee exCoinFee);

    /**
     * 新增合约-杠杆手续费率
     *
     * @param exCoinFee 合约-杠杆手续费率
     * @return 结果
     */
    public int insertExCoinFee(ExCoinFee exCoinFee);

    /**
     * 修改合约-杠杆手续费率
     *
     * @param exCoinFee 合约-杠杆手续费率
     * @return 结果
     */
    public int updateExCoinFee(ExCoinFee exCoinFee);

    /**
     * 批量删除合约-杠杆手续费率
     *
     * @param ids 需要删除的合约-杠杆手续费率主键集合
     * @return 结果
     */
    public int deleteExCoinFeeByIds(Long[] ids);

    /**
     * 删除合约-杠杆手续费率信息
     *
     * @param id 合约-杠杆手续费率主键
     * @return 结果
     */
    public int deleteExCoinFeeById(Long id);
}
