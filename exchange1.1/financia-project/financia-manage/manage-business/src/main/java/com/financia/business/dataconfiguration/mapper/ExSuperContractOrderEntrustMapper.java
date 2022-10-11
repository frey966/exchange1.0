package com.financia.business.dataconfiguration.mapper;

import java.util.List;
import com.financia.superleverage.SuperContractOrderEntrust;

/**
 * 超级杠杆-委托订单Mapper接口
 * 
 * @author 花生
 * @date 2022-09-07
 */
public interface ExSuperContractOrderEntrustMapper 
{
    /**
     * 查询超级杠杆-委托订单
     * 
     * @param id 超级杠杆-委托订单主键
     * @return 超级杠杆-委托订单
     */
    public SuperContractOrderEntrust selectExSuperContractOrderEntrustById(Long id);

    /**
     * 查询超级杠杆-委托订单列表
     * 
     * @param exSuperContractOrderEntrust 超级杠杆-委托订单
     * @return 超级杠杆-委托订单集合
     */
    public List<SuperContractOrderEntrust> selectExSuperContractOrderEntrustList(SuperContractOrderEntrust exSuperContractOrderEntrust);

    /**
     * 新增超级杠杆-委托订单
     * 
     * @param exSuperContractOrderEntrust 超级杠杆-委托订单
     * @return 结果
     */
    public int insertExSuperContractOrderEntrust(SuperContractOrderEntrust exSuperContractOrderEntrust);

    /**
     * 修改超级杠杆-委托订单
     * 
     * @param exSuperContractOrderEntrust 超级杠杆-委托订单
     * @return 结果
     */
    public int updateExSuperContractOrderEntrust(SuperContractOrderEntrust exSuperContractOrderEntrust);

    /**
     * 删除超级杠杆-委托订单
     * 
     * @param id 超级杠杆-委托订单主键
     * @return 结果
     */
    public int deleteExSuperContractOrderEntrustById(Long id);

    /**
     * 批量删除超级杠杆-委托订单
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteExSuperContractOrderEntrustByIds(Long[] ids);
}
