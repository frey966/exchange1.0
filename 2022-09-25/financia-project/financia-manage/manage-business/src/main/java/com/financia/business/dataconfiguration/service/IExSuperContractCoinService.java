package com.financia.business.dataconfiguration.service;

import java.util.List;
import com.financia.superleverage.SuperContractCoin;

/**
 * 超级杠杆-币种列Service接口
 * 
 * @author 花生
 * @date 2022-09-07
 */
public interface IExSuperContractCoinService 
{
    /**
     * 查询超级杠杆-币种列
     * 
     * @param id 超级杠杆-币种列主键
     * @return 超级杠杆-币种列
     */
    public SuperContractCoin selectExSuperContractCoinById(Long id);

    /**
     * 查询超级杠杆-币种列列表
     * 
     * @param exSuperContractCoin 超级杠杆-币种列
     * @return 超级杠杆-币种列集合
     */
    public List<SuperContractCoin> selectExSuperContractCoinList(SuperContractCoin exSuperContractCoin);

    /**
     * 新增超级杠杆-币种列
     * 
     * @param exSuperContractCoin 超级杠杆-币种列
     * @return 结果
     */
    public int insertExSuperContractCoin(SuperContractCoin exSuperContractCoin);

    /**
     * 修改超级杠杆-币种列
     * 
     * @param exSuperContractCoin 超级杠杆-币种列
     * @return 结果
     */
    public int updateExSuperContractCoin(SuperContractCoin exSuperContractCoin);

    /**
     * 批量删除超级杠杆-币种列
     * 
     * @param ids 需要删除的超级杠杆-币种列主键集合
     * @return 结果
     */
    public int deleteExSuperContractCoinByIds(Long[] ids);

    /**
     * 删除超级杠杆-币种列信息
     * 
     * @param id 超级杠杆-币种列主键
     * @return 结果
     */
    public int deleteExSuperContractCoinById(Long id);
}
