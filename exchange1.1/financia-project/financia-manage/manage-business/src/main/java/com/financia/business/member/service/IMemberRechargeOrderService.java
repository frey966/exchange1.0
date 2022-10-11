package com.financia.business.member.service;

import com.financia.exchange.MemberRechargeOrder;

import java.util.List;

/**
 * 交易所-会员充值记录Service接口
 * 
 * @author 花生
 * @date 2022-08-11
 */
public interface IMemberRechargeOrderService 
{
    /**
     * 查询交易所-会员充值记录
     * 
     * @param id 交易所-会员充值记录主键
     * @return 交易所-会员充值记录
     */
    public MemberRechargeOrder selectMemberRechargeOrderById(Long id);

    /**
     * 查询交易所-会员充值记录列表
     * 
     * @param memberRechargeOrder 交易所-会员充值记录
     * @return 交易所-会员充值记录集合
     */
    public List<MemberRechargeOrder> selectMemberRechargeOrderList(MemberRechargeOrder memberRechargeOrder);

    /**
     * 新增交易所-会员充值记录
     * 
     * @param memberRechargeOrder 交易所-会员充值记录
     * @return 结果
     */
    public int insertMemberRechargeOrder(MemberRechargeOrder memberRechargeOrder);

    /**
     * 修改交易所-会员充值记录
     * 
     * @param memberRechargeOrder 交易所-会员充值记录
     * @return 结果
     */
    public int updateMemberRechargeOrder(MemberRechargeOrder memberRechargeOrder);

    /**
     * 批量删除交易所-会员充值记录
     * 
     * @param ids 需要删除的交易所-会员充值记录主键集合
     * @return 结果
     */
    public int deleteMemberRechargeOrderByIds(Long[] ids);

    /**
     * 删除交易所-会员充值记录信息
     * 
     * @param id 交易所-会员充值记录主键
     * @return 结果
     */
    public int deleteMemberRechargeOrderById(Long id);
}
