package com.financia.common.service.impl;

import java.util.List;

import com.financia.common.PCompanyTrxTransferOrder;
import com.financia.common.core.utils.DateUtils;
import com.financia.common.mapper.PCompanyTrxTransferOrderMapper;
import com.financia.common.service.IPCompanyTrxTransferOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 公共-公司钱包TRX转账记录Service业务层处理
 * 
 * @author 花生
 * @date 2022-08-28
 */
@Service
public class PCompanyTrxTransferOrderServiceImpl implements IPCompanyTrxTransferOrderService
{
    @Autowired
    private PCompanyTrxTransferOrderMapper pCompanyTrxTransferOrderMapper;

    /**
     * 查询公共-公司钱包TRX转账记录
     * 
     * @param id 公共-公司钱包TRX转账记录主键
     * @return 公共-公司钱包TRX转账记录
     */
    @Override
    public PCompanyTrxTransferOrder selectPCompanyTrxTransferOrderById(Long id)
    {
        return pCompanyTrxTransferOrderMapper.selectPCompanyTrxTransferOrderById(id);
    }

    /**
     * 查询公共-公司钱包TRX转账记录列表
     * 
     * @param pCompanyTrxTransferOrder 公共-公司钱包TRX转账记录
     * @return 公共-公司钱包TRX转账记录
     */
    @Override
    public List<PCompanyTrxTransferOrder> selectPCompanyTrxTransferOrderList(PCompanyTrxTransferOrder pCompanyTrxTransferOrder)
    {
        return pCompanyTrxTransferOrderMapper.selectPCompanyTrxTransferOrderList(pCompanyTrxTransferOrder);
    }

    /**
     * 新增公共-公司钱包TRX转账记录
     * 
     * @param pCompanyTrxTransferOrder 公共-公司钱包TRX转账记录
     * @return 结果
     */
    @Override
    public int insertPCompanyTrxTransferOrder(PCompanyTrxTransferOrder pCompanyTrxTransferOrder)
    {
        return pCompanyTrxTransferOrderMapper.insertPCompanyTrxTransferOrder(pCompanyTrxTransferOrder);
    }

    /**
     * 修改公共-公司钱包TRX转账记录
     * 
     * @param pCompanyTrxTransferOrder 公共-公司钱包TRX转账记录
     * @return 结果
     */
    @Override
    public int updatePCompanyTrxTransferOrder(PCompanyTrxTransferOrder pCompanyTrxTransferOrder)
    {
        return pCompanyTrxTransferOrderMapper.updatePCompanyTrxTransferOrder(pCompanyTrxTransferOrder);
    }

    /**
     * 批量删除公共-公司钱包TRX转账记录
     * 
     * @param ids 需要删除的公共-公司钱包TRX转账记录主键
     * @return 结果
     */
    @Override
    public int deletePCompanyTrxTransferOrderByIds(Long[] ids)
    {
        return pCompanyTrxTransferOrderMapper.deletePCompanyTrxTransferOrderByIds(ids);
    }

    /**
     * 删除公共-公司钱包TRX转账记录信息
     * 
     * @param id 公共-公司钱包TRX转账记录主键
     * @return 结果
     */
    @Override
    public int deletePCompanyTrxTransferOrderById(Long id)
    {
        return pCompanyTrxTransferOrderMapper.deletePCompanyTrxTransferOrderById(id);
    }
}
