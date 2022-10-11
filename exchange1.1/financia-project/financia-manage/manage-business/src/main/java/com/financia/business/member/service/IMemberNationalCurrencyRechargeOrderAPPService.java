package com.financia.business.member.service;

import com.financia.exchange.MemberNationalCurrencyRechargeOrder;

import java.util.List;

/**
 * 基础业务-会员法币充值记录Service接口
 * 
 * @author 花生
 * @date 2022-09-19
 */
public interface IMemberNationalCurrencyRechargeOrderAPPService 
{
    /**
     * 查询基础业务-会员法币充值记录
     * 
     * @param id 基础业务-会员法币充值记录主键
     * @return 基础业务-会员法币充值记录
     */
     MemberNationalCurrencyRechargeOrder selectMemberNationalCurrencyRechargeOrderAPPById(Long id);

    /**
     * 查询基础业务-会员法币充值记录列表
     * 
     * @param memberNationalCurrencyRechargeOrderAPP 基础业务-会员法币充值记录
     * @return 基础业务-会员法币充值记录集合
     */
     List<MemberNationalCurrencyRechargeOrder> selectMemberNationalCurrencyRechargeOrderAPPList(MemberNationalCurrencyRechargeOrder memberNationalCurrencyRechargeOrderAPP);

    /**
     * 新增基础业务-会员法币充值记录
     * 
     * @param memberNationalCurrencyRechargeOrderAPP 基础业务-会员法币充值记录
     * @return 结果
     */
     int insertMemberNationalCurrencyRechargeOrderAPP(MemberNationalCurrencyRechargeOrder memberNationalCurrencyRechargeOrderAPP);

    /**
     * 修改基础业务-会员法币充值记录
     * 
     * @param memberNationalCurrencyRechargeOrderAPP 基础业务-会员法币充值记录
     * @return 结果
     */
     int updateMemberNationalCurrencyRechargeOrderAPP(MemberNationalCurrencyRechargeOrder memberNationalCurrencyRechargeOrderAPP);

    /**
     * 批量删除基础业务-会员法币充值记录
     * 
     * @param ids 需要删除的基础业务-会员法币充值记录主键集合
     * @return 结果
     */
     int deleteMemberNationalCurrencyRechargeOrderAPPByIds(Long[] ids);

    /**
     * 删除基础业务-会员法币充值记录信息
     * 
     * @param id 基础业务-会员法币充值记录主键
     * @return 结果
     */
     int deleteMemberNationalCurrencyRechargeOrderAPPById(Long id);
}
