package com.financia.system.service.impl;

import java.util.List;
import com.financia.common.core.utils.DateUtils;
import com.financia.exchange.MemberNationalCurrencyRechargeOrder;
import com.financia.system.mapper.MemberNationalCurrencyRechargeOrderAPPMapper;
import com.financia.system.service.IMemberNationalCurrencyRechargeOrderAPPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 基础业务-会员法币充值记录Service业务层处理
 * 
 * @author 花生
 * @date 2022-09-18
 */
@Service
public class MemberNationalCurrencyRechargeOrderAPPServiceImpl implements IMemberNationalCurrencyRechargeOrderAPPService
{
    @Autowired
    private MemberNationalCurrencyRechargeOrderAPPMapper memberNationalCurrencyRechargeOrderAPPMapper;

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
        return memberNationalCurrencyRechargeOrderAPPMapper.insertMemberNationalCurrencyRechargeOrderAPP(memberNationalCurrencyRechargeOrderAPP);
    }

    /**
     * 修改基础业务-会员法币充值记录
     * 
     * @param memberNationalCurrencyRechargeOrderAPP 基础业务-会员法币充值记录
     * @return 结果
     */
    @Override
    public int updateMemberNationalCurrencyRechargeOrderAPP(MemberNationalCurrencyRechargeOrder memberNationalCurrencyRechargeOrderAPP)
    {
        return memberNationalCurrencyRechargeOrderAPPMapper.updateMemberNationalCurrencyRechargeOrderAPP(memberNationalCurrencyRechargeOrderAPP);
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
