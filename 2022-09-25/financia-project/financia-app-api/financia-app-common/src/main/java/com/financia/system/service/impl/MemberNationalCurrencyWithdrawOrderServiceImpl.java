package com.financia.system.service.impl;

import java.util.List;

import com.financia.common.core.utils.DateUtils;
import com.financia.common.core.web.domain.AjaxResult;
import com.financia.exchange.MemberNationalCurrencyRechargeOrder;
import com.financia.exchange.MemberNationalCurrencyWithdrawOrder;
import com.financia.exchange.MemberWalletNationalCurrency;
import com.financia.system.mapper.MemberNationalCurrencyWithdrawOrderMapper;
import com.financia.system.service.IMemberNationalCurrencyWithdrawOrderService;
import com.financia.system.service.MemberWalletNationalCurrencyService;
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


}
