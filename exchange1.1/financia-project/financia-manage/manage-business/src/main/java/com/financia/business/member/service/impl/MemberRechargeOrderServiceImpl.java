package com.financia.business.member.service.impl;

import com.financia.business.member.mapper.MemberRechargeOrderMapper;
import com.financia.business.member.service.IMemberRechargeOrderService;
import com.financia.common.core.utils.DateUtils;
import com.financia.exchange.MemberRechargeOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 交易所-会员充值记录Service业务层处理
 * 
 * @author 花生
 * @date 2022-08-11
 */
@Service
public class MemberRechargeOrderServiceImpl implements IMemberRechargeOrderService
{
    @Autowired
    private MemberRechargeOrderMapper memberRechargeOrderMapper;

    /**
     * 查询交易所-会员充值记录
     * 
     * @param id 交易所-会员充值记录主键
     * @return 交易所-会员充值记录
     */
    @Override
    public MemberRechargeOrder selectMemberRechargeOrderById(Long id)
    {
        return memberRechargeOrderMapper.selectMemberRechargeOrderById(id);
    }

    /**
     * 查询交易所-会员充值记录列表
     * 
     * @param memberRechargeOrder 交易所-会员充值记录
     * @return 交易所-会员充值记录
     */
    @Override
    public List<MemberRechargeOrder> selectMemberRechargeOrderList(MemberRechargeOrder memberRechargeOrder)
    {
        return memberRechargeOrderMapper.selectMemberRechargeOrderList(memberRechargeOrder);
    }

    /**
     * 新增交易所-会员充值记录
     * 
     * @param memberRechargeOrder 交易所-会员充值记录
     * @return 结果
     */
    @Override
    public int insertMemberRechargeOrder(MemberRechargeOrder memberRechargeOrder)
    {
        return memberRechargeOrderMapper.insertMemberRechargeOrder(memberRechargeOrder);
    }

    /**
     * 修改交易所-会员充值记录
     * 
     * @param memberRechargeOrder 交易所-会员充值记录
     * @return 结果
     */
    @Override
    public int updateMemberRechargeOrder(MemberRechargeOrder memberRechargeOrder)
    {
        return memberRechargeOrderMapper.updateMemberRechargeOrder(memberRechargeOrder);
    }

    /**
     * 批量删除交易所-会员充值记录
     * 
     * @param ids 需要删除的交易所-会员充值记录主键
     * @return 结果
     */
    @Override
    public int deleteMemberRechargeOrderByIds(Long[] ids)
    {
        return memberRechargeOrderMapper.deleteMemberRechargeOrderByIds(ids);
    }

    /**
     * 删除交易所-会员充值记录信息
     * 
     * @param id 交易所-会员充值记录主键
     * @return 结果
     */
    @Override
    public int deleteMemberRechargeOrderById(Long id)
    {
        return memberRechargeOrderMapper.deleteMemberRechargeOrderById(id);
    }
}
