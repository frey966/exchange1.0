package com.financia.business.member.mapper;

import com.financia.exchange.MemberWithdrawInfo;

import java.util.List;

/**
 * 公共-会员提现汇总Mapper接口
 * 
 * @author 花生
 * @date 2022-08-11
 */
public interface MemberWithdrawInfoMapper 
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
     * 删除公共-会员提现汇总
     * 
     * @param id 公共-会员提现汇总主键
     * @return 结果
     */
    public int deleteMemberWithdrawInfoById(Long id);

    /**
     * 批量删除公共-会员提现汇总
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteMemberWithdrawInfoByIds(Long[] ids);
}
