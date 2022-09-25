package com.financia.common.mapper;

import com.financia.common.PCompanyTrxTransferOrder;

import java.util.List;

/**
 * 公共-公司钱包TRX转账记录Mapper接口
 * 
 * @author 花生
 * @date 2022-08-28
 */
public interface PCompanyTrxTransferOrderMapper 
{
    /**
     * 查询公共-公司钱包TRX转账记录
     * 
     * @param id 公共-公司钱包TRX转账记录主键
     * @return 公共-公司钱包TRX转账记录
     */
    public PCompanyTrxTransferOrder selectPCompanyTrxTransferOrderById(Long id);

    /**
     * 查询公共-公司钱包TRX转账记录列表
     * 
     * @param pCompanyTrxTransferOrder 公共-公司钱包TRX转账记录
     * @return 公共-公司钱包TRX转账记录集合
     */
    public List<PCompanyTrxTransferOrder> selectPCompanyTrxTransferOrderList(PCompanyTrxTransferOrder pCompanyTrxTransferOrder);

    /**
     * 新增公共-公司钱包TRX转账记录
     * 
     * @param pCompanyTrxTransferOrder 公共-公司钱包TRX转账记录
     * @return 结果
     */
    public int insertPCompanyTrxTransferOrder(PCompanyTrxTransferOrder pCompanyTrxTransferOrder);

    /**
     * 修改公共-公司钱包TRX转账记录
     * 
     * @param pCompanyTrxTransferOrder 公共-公司钱包TRX转账记录
     * @return 结果
     */
    public int updatePCompanyTrxTransferOrder(PCompanyTrxTransferOrder pCompanyTrxTransferOrder);

    /**
     * 删除公共-公司钱包TRX转账记录
     * 
     * @param id 公共-公司钱包TRX转账记录主键
     * @return 结果
     */
    public int deletePCompanyTrxTransferOrderById(Long id);

    /**
     * 批量删除公共-公司钱包TRX转账记录
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePCompanyTrxTransferOrderByIds(Long[] ids);
}
