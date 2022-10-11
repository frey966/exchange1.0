package com.financia.business.member.service.impl;

import java.util.List;

import com.financia.business.member.mapper.MemberNationalCurrencyWithdrawOrderMapper;
import com.financia.business.member.service.IMemberNationalCurrencyWithdrawOrderService;
import com.financia.business.member.service.MemberWalletNationalCurrencyService;
import com.financia.common.core.utils.DateUtils;
import com.financia.common.core.web.domain.AjaxResult;
import com.financia.exchange.MemberNationalCurrencyRechargeOrder;
import com.financia.exchange.MemberNationalCurrencyWithdrawOrder;
import com.financia.exchange.MemberWalletNationalCurrency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.ObjectUtils;

/**
 * 基础业务-会员法币提现记录Service业务层处理
 * 
 * @author 花生
 * @date 2022-09-22
 */
@Service
public class MemberNationalCurrencyWithdrawOrderServiceImpl implements IMemberNationalCurrencyWithdrawOrderService
{
    @Autowired
    private MemberNationalCurrencyWithdrawOrderMapper memberNationalCurrencyWithdrawOrderMapper;
    @Autowired
    private MemberWalletNationalCurrencyService memberWalletNationalCurrencyService;
    /**
     * 查询基础业务-会员法币提现记录
     * 
     * @param id 基础业务-会员法币提现记录主键
     * @return 基础业务-会员法币提现记录
     */
    @Override
    public MemberNationalCurrencyWithdrawOrder selectMemberNationalCurrencyWithdrawOrderById(Long id)
    {
        return memberNationalCurrencyWithdrawOrderMapper.selectMemberNationalCurrencyWithdrawOrderById(id);
    }

    /**
     * 查询基础业务-会员法币提现记录列表
     * 
     * @param memberNationalCurrencyWithdrawOrder 基础业务-会员法币提现记录
     * @return 基础业务-会员法币提现记录
     */
    @Override
    public List<MemberNationalCurrencyWithdrawOrder> selectMemberNationalCurrencyWithdrawOrderList(MemberNationalCurrencyWithdrawOrder memberNationalCurrencyWithdrawOrder)
    {
        return memberNationalCurrencyWithdrawOrderMapper.selectMemberNationalCurrencyWithdrawOrderList(memberNationalCurrencyWithdrawOrder);
    }

    /**
     * 新增基础业务-会员法币提现记录
     * 
     * @param memberNationalCurrencyWithdrawOrder 基础业务-会员法币提现记录
     * @return 结果
     */
    @Override
    public int insertMemberNationalCurrencyWithdrawOrder(MemberNationalCurrencyWithdrawOrder memberNationalCurrencyWithdrawOrder)
    {
        memberNationalCurrencyWithdrawOrder.setCreateTime(DateUtils.getTime());
        return memberNationalCurrencyWithdrawOrderMapper.insertMemberNationalCurrencyWithdrawOrder(memberNationalCurrencyWithdrawOrder);
    }

    /**
     * 修改基础业务-会员法币提现记录
     * 
     * @param memberNationalCurrencyWithdrawOrder 基础业务-会员法币提现记录
     * @return 结果
     */
    @Override
    public int updateMemberNationalCurrencyWithdrawOrder(MemberNationalCurrencyWithdrawOrder memberNationalCurrencyWithdrawOrder)
    {
        MemberNationalCurrencyWithdrawOrder getMemberncro=memberNationalCurrencyWithdrawOrderMapper.selectMemberNationalCurrencyWithdrawOrderById(memberNationalCurrencyWithdrawOrder.getId());
        if(memberNationalCurrencyWithdrawOrder.getCheckStatus()==1){
            //通过直接修改状态
            memberNationalCurrencyWithdrawOrder.setRechargeStatus(Long.parseLong("1"));
            memberNationalCurrencyWithdrawOrder.setOrderStatus(Long.parseLong("1"));
        }else{
            //不通过，退回客户提现的金额
            //查询会员钱包是否存在
            MemberWalletNationalCurrency memberWalletNationalCurrency=new MemberWalletNationalCurrency();
            memberWalletNationalCurrency.setCoinId(Integer.parseInt(getMemberncro.getCoinId()));
            memberWalletNationalCurrency.setMemberId(Long.parseLong(getMemberncro.getMemberId()));
            MemberWalletNationalCurrency memberWalletNationalCurrencyResult=memberWalletNationalCurrencyService.selectNationalCurrencyByMemberIdAndCoinId(memberWalletNationalCurrency);

            memberWalletNationalCurrency.setMoney(getMemberncro.getMoney());
            if(!ObjectUtils.isEmpty(memberWalletNationalCurrencyResult)){
                //有则修改法币余额
                if(!(memberWalletNationalCurrencyService.addMoney(memberWalletNationalCurrency)>0)){
                    new AjaxResult(-2,"修改法币余额失败！");
                    //回滚
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return 0;
                }
            }
            memberNationalCurrencyWithdrawOrder.setRechargeStatus(Long.parseLong("-1"));
            memberNationalCurrencyWithdrawOrder.setOrderStatus(Long.parseLong("-1"));
        }

        memberNationalCurrencyWithdrawOrder.setUpdateTime(DateUtils.getTime());
        return memberNationalCurrencyWithdrawOrderMapper.updateMemberNationalCurrencyWithdrawOrder(memberNationalCurrencyWithdrawOrder);
    }

    /**
     * 批量删除基础业务-会员法币提现记录
     * 
     * @param ids 需要删除的基础业务-会员法币提现记录主键
     * @return 结果
     */
    @Override
    public int deleteMemberNationalCurrencyWithdrawOrderByIds(Long[] ids)
    {
        return memberNationalCurrencyWithdrawOrderMapper.deleteMemberNationalCurrencyWithdrawOrderByIds(ids);
    }

    /**
     * 删除基础业务-会员法币提现记录信息
     * 
     * @param id 基础业务-会员法币提现记录主键
     * @return 结果
     */
    @Override
    public int deleteMemberNationalCurrencyWithdrawOrderById(Long id)
    {
        return memberNationalCurrencyWithdrawOrderMapper.deleteMemberNationalCurrencyWithdrawOrderById(id);
    }
}
