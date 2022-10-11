package com.financia.business.member.service.impl;

import com.financia.business.member.mapper.MemberWithdrawInfoMapper;
import com.financia.business.member.service.IMemberWithdrawInfoService;
import com.financia.exchange.MemberWithdrawInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 公共-会员提现汇总Service业务层处理
 * 
 * @author 花生
 * @date 2022-08-11
 */
@Service
public class MemberWithdrawInfoServiceImpl implements IMemberWithdrawInfoService
{
    @Autowired
    private MemberWithdrawInfoMapper memberWithdrawInfoMapper;

    /**
     * 查询公共-会员提现汇总
     * 
     * @param id 公共-会员提现汇总主键
     * @return 公共-会员提现汇总
     */
    @Override
    public MemberWithdrawInfo selectMemberWithdrawInfoById(Long id)
    {
        return memberWithdrawInfoMapper.selectMemberWithdrawInfoById(id);
    }

    /**
     * 查询公共-会员提现汇总列表
     * 
     * @param memberWithdrawInfo 公共-会员提现汇总
     * @return 公共-会员提现汇总
     */
    @Override
    public List<MemberWithdrawInfo> selectMemberWithdrawInfoList(MemberWithdrawInfo memberWithdrawInfo)
    {
        return memberWithdrawInfoMapper.selectMemberWithdrawInfoList(memberWithdrawInfo);
    }

    /**
     * 新增公共-会员提现汇总
     * 
     * @param memberWithdrawInfo 公共-会员提现汇总
     * @return 结果
     */
    @Override
    public int insertMemberWithdrawInfo(MemberWithdrawInfo memberWithdrawInfo)
    {
        return memberWithdrawInfoMapper.insertMemberWithdrawInfo(memberWithdrawInfo);
    }

    /**
     * 修改公共-会员提现汇总
     * 
     * @param memberWithdrawInfo 公共-会员提现汇总
     * @return 结果
     */
    @Override
    public int updateMemberWithdrawInfo(MemberWithdrawInfo memberWithdrawInfo)
    {
        return memberWithdrawInfoMapper.updateMemberWithdrawInfo(memberWithdrawInfo);
    }

    /**
     * 批量删除公共-会员提现汇总
     * 
     * @param ids 需要删除的公共-会员提现汇总主键
     * @return 结果
     */
    @Override
    public int deleteMemberWithdrawInfoByIds(Long[] ids)
    {
        return memberWithdrawInfoMapper.deleteMemberWithdrawInfoByIds(ids);
    }

    /**
     * 删除公共-会员提现汇总信息
     * 
     * @param id 公共-会员提现汇总主键
     * @return 结果
     */
    @Override
    public int deleteMemberWithdrawInfoById(Long id)
    {
        return memberWithdrawInfoMapper.deleteMemberWithdrawInfoById(id);
    }
}
