package com.financia.system.service;

import com.financia.exchange.MemberNationalCurrencyWithdrawOrder;

import java.util.List;

/**
 * 基础业务-会员法币提现记录Service接口
 * 
 * @author 花生
 * @date 2022-09-22
 */
public interface IMemberNationalCurrencyWithdrawOrderService 
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


}
