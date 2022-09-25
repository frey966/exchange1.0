package com.financia.business.member.mapper;

import com.financia.exchange.MemberWithdrawOrder;

import java.util.List;

/**
 * 交易所-会员提现申请Mapper接口
 * 
 * @author 花生
 * @date 2022-08-11
 */
public interface MemberWithdrawOrderMapper 
{
    /**
     * 查询交易所-会员提现申请
     * 
     * @param id 交易所-会员提现申请主键
     * @return 交易所-会员提现申请
     */
    public MemberWithdrawOrder selectMemberWithdrawOrderById(Long id);

    /**
     * 查询交易所-会员提现申请列表
     * 
     * @param memberWithdrawOrder 交易所-会员提现申请
     * @return 交易所-会员提现申请集合
     */
    public List<MemberWithdrawOrder> selectMemberWithdrawOrderList(MemberWithdrawOrder memberWithdrawOrder);

    /**
     * 新增交易所-会员提现申请
     * 
     * @param memberWithdrawOrder 交易所-会员提现申请
     * @return 结果
     */
    public int insertMemberWithdrawOrder(MemberWithdrawOrder memberWithdrawOrder);

    /**
     * 修改交易所-会员提现申请
     * 
     * @param memberWithdrawOrder 交易所-会员提现申请
     * @return 结果
     */
    public int updateMemberWithdrawOrder(MemberWithdrawOrder memberWithdrawOrder);

    /**
     * 删除交易所-会员提现申请
     * 
     * @param id 交易所-会员提现申请主键
     * @return 结果
     */
    public int deleteMemberWithdrawOrderById(Long id);

    /**
     * 批量删除交易所-会员提现申请
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteMemberWithdrawOrderByIds(Long[] ids);
}
