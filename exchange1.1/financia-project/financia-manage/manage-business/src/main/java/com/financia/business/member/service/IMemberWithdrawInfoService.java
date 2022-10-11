package com.financia.business.member.service;

import com.financia.exchange.MemberWithdrawInfo;

import java.util.List;

/**
 * 公共-会员提现汇总Service接口
 * 
 * @author 花生
 * @date 2022-08-11
 */
public interface IMemberWithdrawInfoService 
{
    /**
     * 查询公共-会员提现汇总
     * 
     * @param id 公共-会员提现汇总主键
     * @return 公共-会员提现汇总
     */
    public MemberWithdrawInfo selectMemberWithdrawInfoById(Long id);

    /**
     * 查询公共-会员提现汇总列表
     * 
     * @param memberWithdrawInfo 公共-会员提现汇总
     * @return 公共-会员提现汇总集合
     */
    public List<MemberWithdrawInfo> selectMemberWithdrawInfoList(MemberWithdrawInfo memberWithdrawInfo);

    /**
     * 新增公共-会员提现汇总
     * 
     * @param memberWithdrawInfo 公共-会员提现汇总
     * @return 结果
     */
    public int insertMemberWithdrawInfo(MemberWithdrawInfo memberWithdrawInfo);

    /**
     * 修改公共-会员提现汇总
     * 
     * @param memberWithdrawInfo 公共-会员提现汇总
     * @return 结果
     */
    public int updateMemberWithdrawInfo(MemberWithdrawInfo memberWithdrawInfo);

    /**
     * 批量删除公共-会员提现汇总
     * 
     * @param ids 需要删除的公共-会员提现汇总主键集合
     * @return 结果
     */
    public int deleteMemberWithdrawInfoByIds(Long[] ids);

    /**
     * 删除公共-会员提现汇总信息
     * 
     * @param id 公共-会员提现汇总主键
     * @return 结果
     */
    public int deleteMemberWithdrawInfoById(Long id);
}
