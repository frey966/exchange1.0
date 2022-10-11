package com.financia.business.member.mapper;

import com.financia.exchange.MemberNationalCurrencyWithdrawOrder;

import java.util.List;

/**
 * 基础业务-会员法币提现记录Mapper接口
 * 
 * @author 花生
 * @date 2022-09-22
 */
public interface MemberNationalCurrencyWithdrawOrderMapper 
{
    /**
     * 查询基础业务-会员法币提现记录
     * 
     * @param id 基础业务-会员法币提现记录主键
     * @return 基础业务-会员法币提现记录
     */
    public MemberNationalCurrencyWithdrawOrder selectMemberNationalCurrencyWithdrawOrderById(Long id);

    /**
     * 查询基础业务-会员法币提现记录列表
     * 
     * @param memberNationalCurrencyWithdrawOrder 基础业务-会员法币提现记录
     * @return 基础业务-会员法币提现记录集合
     */
    public List<MemberNationalCurrencyWithdrawOrder> selectMemberNationalCurrencyWithdrawOrderList(MemberNationalCurrencyWithdrawOrder memberNationalCurrencyWithdrawOrder);

    /**
     * 新增基础业务-会员法币提现记录
     * 
     * @param memberNationalCurrencyWithdrawOrder 基础业务-会员法币提现记录
     * @return 结果
     */
    public int insertMemberNationalCurrencyWithdrawOrder(MemberNationalCurrencyWithdrawOrder memberNationalCurrencyWithdrawOrder);

    /**
     * 修改基础业务-会员法币提现记录
     * 
     * @param memberNationalCurrencyWithdrawOrder 基础业务-会员法币提现记录
     * @return 结果
     */
    public int updateMemberNationalCurrencyWithdrawOrder(MemberNationalCurrencyWithdrawOrder memberNationalCurrencyWithdrawOrder);

    /**
     * 删除基础业务-会员法币提现记录
     * 
     * @param id 基础业务-会员法币提现记录主键
     * @return 结果
     */
    public int deleteMemberNationalCurrencyWithdrawOrderById(Long id);

    /**
     * 批量删除基础业务-会员法币提现记录
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteMemberNationalCurrencyWithdrawOrderByIds(Long[] ids);
}
