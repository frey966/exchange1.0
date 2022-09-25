package com.financia.system.mapper;

import java.util.List;

import com.financia.exchange.MemberNationalCurrencyRechargeOrder;

/**
 * 基础业务-会员法币充值记录Mapper接口
 * 
 * @author 花生
 * @date 2022-09-18
 */
public interface MemberNationalCurrencyRechargeOrderAPPMapper 
{
    /**
     * 查询基础业务-会员法币充值记录
     * 
     * @param id 基础业务-会员法币充值记录主键
     * @return 基础业务-会员法币充值记录
     */
    public MemberNationalCurrencyRechargeOrder selectMemberNationalCurrencyRechargeOrderAPPById(Long id);

    /**
     * 查询基础业务-会员法币充值记录列表
     * 
     * @param memberNationalCurrencyRechargeOrderAPP 基础业务-会员法币充值记录
     * @return 基础业务-会员法币充值记录集合
     */
    public List<MemberNationalCurrencyRechargeOrder> selectMemberNationalCurrencyRechargeOrderAPPList(MemberNationalCurrencyRechargeOrder memberNationalCurrencyRechargeOrderAPP);

    /**
     * 新增基础业务-会员法币充值记录
     * 
     * @param MemberNationalCurrencyRechargeOrder 基础业务-会员法币充值记录
     * @return 结果
     */
    public int insertMemberNationalCurrencyRechargeOrderAPP(MemberNationalCurrencyRechargeOrder MemberNationalCurrencyRechargeOrder);

    /**
     * 修改基础业务-会员法币充值记录
     * 
     * @param MemberNationalCurrencyRechargeOrder 基础业务-会员法币充值记录
     * @return 结果
     */
    public int updateMemberNationalCurrencyRechargeOrderAPP(MemberNationalCurrencyRechargeOrder MemberNationalCurrencyRechargeOrder);

    /**
     * 删除基础业务-会员法币充值记录
     * 
     * @param id 基础业务-会员法币充值记录主键
     * @return 结果
     */
    public int deleteMemberNationalCurrencyRechargeOrderAPPById(Long id);

    /**
     * 批量删除基础业务-会员法币充值记录
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteMemberNationalCurrencyRechargeOrderAPPByIds(Long[] ids);
}
