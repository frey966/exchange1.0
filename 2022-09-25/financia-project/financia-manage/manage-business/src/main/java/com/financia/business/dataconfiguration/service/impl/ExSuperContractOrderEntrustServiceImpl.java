package com.financia.business.dataconfiguration.service.impl;

import java.util.List;

import com.financia.business.dataconfiguration.mapper.ExSuperContractOrderEntrustMapper;
import com.financia.business.dataconfiguration.service.IExSuperContractOrderEntrustService;
import com.financia.common.core.utils.DateUtils;
import com.financia.superleverage.SuperContractOrderEntrust;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 超级杠杆-委托订单Service业务层处理
 * 
 * @author 花生
 * @date 2022-09-07
 */
@Service
public class ExSuperContractOrderEntrustServiceImpl implements IExSuperContractOrderEntrustService
{
    @Autowired
    private ExSuperContractOrderEntrustMapper exSuperContractOrderEntrustMapper;

    /**
     * 查询超级杠杆-委托订单
     * 
     * @param id 超级杠杆-委托订单主键
     * @return 超级杠杆-委托订单
     */
    @Override
    public SuperContractOrderEntrust selectExSuperContractOrderEntrustById(Long id)
    {
        return exSuperContractOrderEntrustMapper.selectExSuperContractOrderEntrustById(id);
    }

    /**
     * 查询超级杠杆-委托订单列表
     * 
     * @param exSuperContractOrderEntrust 超级杠杆-委托订单
     * @return 超级杠杆-委托订单
     */
    @Override
    public List<SuperContractOrderEntrust> selectExSuperContractOrderEntrustList(SuperContractOrderEntrust exSuperContractOrderEntrust)
    {
        return exSuperContractOrderEntrustMapper.selectExSuperContractOrderEntrustList(exSuperContractOrderEntrust);
    }

    /**
     * 新增超级杠杆-委托订单
     * 
     * @param exSuperContractOrderEntrust 超级杠杆-委托订单
     * @return 结果
     */
    @Override
    public int insertExSuperContractOrderEntrust(SuperContractOrderEntrust exSuperContractOrderEntrust)
    {
        return exSuperContractOrderEntrustMapper.insertExSuperContractOrderEntrust(exSuperContractOrderEntrust);
    }

    /**
     * 修改超级杠杆-委托订单
     * 
     * @param exSuperContractOrderEntrust 超级杠杆-委托订单
     * @return 结果
     */
    @Override
    public int updateExSuperContractOrderEntrust(SuperContractOrderEntrust exSuperContractOrderEntrust)
    {
        return exSuperContractOrderEntrustMapper.updateExSuperContractOrderEntrust(exSuperContractOrderEntrust);
    }

    /**
     * 批量删除超级杠杆-委托订单
     * 
     * @param ids 需要删除的超级杠杆-委托订单主键
     * @return 结果
     */
    @Override
    public int deleteExSuperContractOrderEntrustByIds(Long[] ids)
    {
        return exSuperContractOrderEntrustMapper.deleteExSuperContractOrderEntrustByIds(ids);
    }

    /**
     * 删除超级杠杆-委托订单信息
     * 
     * @param id 超级杠杆-委托订单主键
     * @return 结果
     */
    @Override
    public int deleteExSuperContractOrderEntrustById(Long id)
    {
        return exSuperContractOrderEntrustMapper.deleteExSuperContractOrderEntrustById(id);
    }
}
