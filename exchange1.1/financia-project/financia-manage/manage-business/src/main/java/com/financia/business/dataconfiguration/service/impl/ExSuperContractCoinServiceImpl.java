package com.financia.business.dataconfiguration.service.impl;

import java.util.List;

import com.financia.business.dataconfiguration.mapper.ExSuperContractCoinMapper;
import com.financia.business.dataconfiguration.service.IExSuperContractCoinService;
import com.financia.common.core.utils.DateUtils;
import com.financia.superleverage.SuperContractCoin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 超级杠杆-币种列Service业务层处理
 * 
 * @author 花生
 * @date 2022-09-07
 */
@Service
public class ExSuperContractCoinServiceImpl implements IExSuperContractCoinService
{
    @Autowired
    private ExSuperContractCoinMapper exSuperContractCoinMapper;

    /**
     * 查询超级杠杆-币种列
     * 
     * @param id 超级杠杆-币种列主键
     * @return 超级杠杆-币种列
     */
    @Override
    public SuperContractCoin selectExSuperContractCoinById(Long id)
    {
        return exSuperContractCoinMapper.selectExSuperContractCoinById(id);
    }

    /**
     * 查询超级杠杆-币种列列表
     * 
     * @param exSuperContractCoin 超级杠杆-币种列
     * @return 超级杠杆-币种列
     */
    @Override
    public List<SuperContractCoin> selectExSuperContractCoinList(SuperContractCoin exSuperContractCoin)
    {
        return exSuperContractCoinMapper.selectExSuperContractCoinList(exSuperContractCoin);
    }

    /**
     * 新增超级杠杆-币种列
     * 
     * @param exSuperContractCoin 超级杠杆-币种列
     * @return 结果
     */
    @Override
    public int insertExSuperContractCoin(SuperContractCoin exSuperContractCoin)
    {
        return exSuperContractCoinMapper.insertExSuperContractCoin(exSuperContractCoin);
    }

    /**
     * 修改超级杠杆-币种列
     * 
     * @param exSuperContractCoin 超级杠杆-币种列
     * @return 结果
     */
    @Override
    public int updateExSuperContractCoin(SuperContractCoin exSuperContractCoin)
    {
        return exSuperContractCoinMapper.updateExSuperContractCoin(exSuperContractCoin);
    }

    /**
     * 批量删除超级杠杆-币种列
     * 
     * @param ids 需要删除的超级杠杆-币种列主键
     * @return 结果
     */
    @Override
    public int deleteExSuperContractCoinByIds(Long[] ids)
    {
        return exSuperContractCoinMapper.deleteExSuperContractCoinByIds(ids);
    }

    /**
     * 删除超级杠杆-币种列信息
     * 
     * @param id 超级杠杆-币种列主键
     * @return 结果
     */
    @Override
    public int deleteExSuperContractCoinById(Long id)
    {
        return exSuperContractCoinMapper.deleteExSuperContractCoinById(id);
    }
}
