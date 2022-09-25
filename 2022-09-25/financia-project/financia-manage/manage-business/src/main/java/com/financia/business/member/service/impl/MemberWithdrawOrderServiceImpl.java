package com.financia.business.member.service.impl;

import com.financia.business.member.mapper.MemberWithdrawOrderMapper;
import com.financia.business.member.service.IMemberWithdrawOrderService;
import com.financia.common.core.utils.DateUtils;
import com.financia.exchange.MemberWithdrawOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 交易所-会员提现申请Service业务层处理
 * 
 * @author 花生
 * @date 2022-08-11
 */
@Service
public class MemberWithdrawOrderServiceImpl implements IMemberWithdrawOrderService
{
    @Autowired
    private MemberWithdrawOrderMapper memberWithdrawOrderMapper;

    /**
     * 查询交易所-会员提现申请
     * 
     * @param id 交易所-会员提现申请主键
     * @return 交易所-会员提现申请
     */
    @Override
    public MemberWithdrawOrder selectMemberWithdrawOrderById(Long id)
    {
        return memberWithdrawOrderMapper.selectMemberWithdrawOrderById(id);
    }

    /**
     * 查询交易所-会员提现申请列表
     * 
     * @param memberWithdrawOrder 交易所-会员提现申请
     * @return 交易所-会员提现申请
     */
    @Override
    public List<MemberWithdrawOrder> selectMemberWithdrawOrderList(MemberWithdrawOrder memberWithdrawOrder)
    {
        return memberWithdrawOrderMapper.selectMemberWithdrawOrderList(memberWithdrawOrder);
    }

    /**
     * 新增交易所-会员提现申请
     * 
     * @param memberWithdrawOrder 交易所-会员提现申请
     * @return 结果
     */
    @Override
    public int insertMemberWithdrawOrder(MemberWithdrawOrder memberWithdrawOrder)
    {
        memberWithdrawOrder.setCreateTime(DateUtils.getNowDate()+"");
        return memberWithdrawOrderMapper.insertMemberWithdrawOrder(memberWithdrawOrder);
    }

    /**
     * 修改交易所-会员提现申请
     * 
     * @param memberWithdrawOrder 交易所-会员提现申请
     * @return 结果
     */
    @Override
    public int updateMemberWithdrawOrder(MemberWithdrawOrder memberWithdrawOrder)
    {
        memberWithdrawOrder.setUpdateTime(DateUtils.getTime());
        return memberWithdrawOrderMapper.updateMemberWithdrawOrder(memberWithdrawOrder);
    }

    /**
     * 批量删除交易所-会员提现申请
     * 
     * @param ids 需要删除的交易所-会员提现申请主键
     * @return 结果
     */
    @Override
    public int deleteMemberWithdrawOrderByIds(Long[] ids)
    {
        return memberWithdrawOrderMapper.deleteMemberWithdrawOrderByIds(ids);
    }

    /**
     * 删除交易所-会员提现申请信息
     * 
     * @param id 交易所-会员提现申请主键
     * @return 结果
     */
    @Override
    public int deleteMemberWithdrawOrderById(Long id)
    {
        return memberWithdrawOrderMapper.deleteMemberWithdrawOrderById(id);
    }
}
