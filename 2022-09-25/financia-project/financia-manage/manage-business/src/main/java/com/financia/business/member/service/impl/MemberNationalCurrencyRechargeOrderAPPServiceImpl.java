package com.financia.business.member.service.impl;

import java.util.List;

import com.financia.business.member.mapper.MemberNationalCurrencyRechargeOrderAPPMapper;
import com.financia.business.member.service.IMemberNationalCurrencyRechargeOrderAPPService;
import com.financia.business.member.service.MemberWalletNationalCurrencyService;
import com.financia.common.core.utils.DateUtils;
import com.financia.common.core.web.domain.AjaxResult;
import com.financia.exchange.MemberNationalCurrencyRechargeOrder;
import com.financia.exchange.MemberWalletNationalCurrency;
import com.financia.exchange.feign.client.RemoteMemberWalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.ObjectUtils;

/**
 * 基础业务-会员法币充值记录Service业务层处理
 * 
 * @author 花生
 * @date 2022-09-19
 */
@Service
public class MemberNationalCurrencyRechargeOrderAPPServiceImpl implements IMemberNationalCurrencyRechargeOrderAPPService
{
    @Autowired
    private MemberNationalCurrencyRechargeOrderAPPMapper memberNationalCurrencyRechargeOrderAPPMapper;
    @Autowired
    private RemoteMemberWalletService memberWalletService;
    @Autowired
    private MemberWalletNationalCurrencyService memberWalletNationalCurrencyService;
    /**
     * 查询基础业务-会员法币充值记录
     * 
     * @param id 基础业务-会员法币充值记录主键
     * @return 基础业务-会员法币充值记录
     */
    @Override
    public MemberNationalCurrencyRechargeOrder selectMemberNationalCurrencyRechargeOrderAPPById(Long id)
    {
        return memberNationalCurrencyRechargeOrderAPPMapper.selectMemberNationalCurrencyRechargeOrderAPPById(id);
    }

    /**
     * 查询基础业务-会员法币充值记录列表
     * 
     * @param memberNationalCurrencyRechargeOrderAPP 基础业务-会员法币充值记录
     * @return 基础业务-会员法币充值记录
     */
    @Override
    public List<MemberNationalCurrencyRechargeOrder> selectMemberNationalCurrencyRechargeOrderAPPList(MemberNationalCurrencyRechargeOrder memberNationalCurrencyRechargeOrderAPP)
    {
        return memberNationalCurrencyRechargeOrderAPPMapper.selectMemberNationalCurrencyRechargeOrderAPPList(memberNationalCurrencyRechargeOrderAPP);
    }

    /**
     * 新增基础业务-会员法币充值记录
     * 
     * @param memberNationalCurrencyRechargeOrderAPP 基础业务-会员法币充值记录
     * @return 结果
     */
    @Override
    public int insertMemberNationalCurrencyRechargeOrderAPP(MemberNationalCurrencyRechargeOrder memberNationalCurrencyRechargeOrderAPP)
    {
        memberNationalCurrencyRechargeOrderAPP.setCreateTime(DateUtils.getNowDate());
        return memberNationalCurrencyRechargeOrderAPPMapper.insertMemberNationalCurrencyRechargeOrderAPP(memberNationalCurrencyRechargeOrderAPP);
    }

    /**
     * 修改基础业务-会员法币充值记录
     * 
     * @param memberNationalCurrencyRechargeOrderAPP 基础业务-会员法币充值记录
     * @return 结果
     * {
     *   "checkStatus": "1",
     *   "id": 1
     * }
     */
    @Override
    public int updateMemberNationalCurrencyRechargeOrderAPP(MemberNationalCurrencyRechargeOrder memberNationalCurrencyRechargeOrderAPP)
    {
        MemberNationalCurrencyRechargeOrder getMemberncro=memberNationalCurrencyRechargeOrderAPPMapper.selectMemberNationalCurrencyRechargeOrderAPPById(memberNationalCurrencyRechargeOrderAPP.getId());
        if(memberNationalCurrencyRechargeOrderAPP.getCheckStatus().equals("1")){
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
            }else{
                //否则直接添加法币钱包
                if(!(memberWalletNationalCurrencyService.insertMemberWalletNationalCurrency(memberWalletNationalCurrency)>0)){
                    new AjaxResult(-3,"创建法币钱包失败！");
                    //回滚
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return 0;
                }
            }
            getMemberncro.setRechargeStatus(Long.parseLong("1"));
            getMemberncro.setOrderStatus(Long.parseLong("1"));
        }else{
            getMemberncro.setRechargeStatus(Long.parseLong("-1"));
            getMemberncro.setOrderStatus(Long.parseLong("-1"));
        }
        getMemberncro.setUpdateTime(DateUtils.getNowDate());
        getMemberncro.setCheckStatus(memberNationalCurrencyRechargeOrderAPP.getCheckStatus());
        getMemberncro.setUpdateBy(memberNationalCurrencyRechargeOrderAPP.getUpdateBy());
        return memberNationalCurrencyRechargeOrderAPPMapper.updateMemberNationalCurrencyRechargeOrderAPP(getMemberncro);
    }

    /**
     * 批量删除基础业务-会员法币充值记录
     * 
     * @param ids 需要删除的基础业务-会员法币充值记录主键
     * @return 结果
     */
    @Override
    public int deleteMemberNationalCurrencyRechargeOrderAPPByIds(Long[] ids)
    {
        return memberNationalCurrencyRechargeOrderAPPMapper.deleteMemberNationalCurrencyRechargeOrderAPPByIds(ids);
    }

    /**
     * 删除基础业务-会员法币充值记录信息
     * 
     * @param id 基础业务-会员法币充值记录主键
     * @return 结果
     */
    @Override
    public int deleteMemberNationalCurrencyRechargeOrderAPPById(Long id)
    {
        return memberNationalCurrencyRechargeOrderAPPMapper.deleteMemberNationalCurrencyRechargeOrderAPPById(id);
    }
}
