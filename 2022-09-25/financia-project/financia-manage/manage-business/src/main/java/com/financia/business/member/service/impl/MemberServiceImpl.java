package com.financia.business.member.service.impl;

import com.financia.business.member.mapper.MemberMapper;
import com.financia.business.member.service.IMemberService;
import com.financia.exchange.Member;
import icu.mhb.mybatisplus.plugln.base.service.impl.JoinServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 会员信息Service业务层处理
 *
 * @author ruoyi
 * @date 2022-07-13
 */
@Service
public class MemberServiceImpl extends JoinServiceImpl<MemberMapper, Member> implements IMemberService
{
    @Autowired
    private MemberMapper memberMapper;

    /**
     * 查询会员信息
     *
     * @param id 会员信息主键
     * @return 会员信息
     */
    @Override
    public Member selectMemberById(Long id)
    {
        return memberMapper.selectMemberById(id);
    }

    /**
     * 查询会员信息列表
     *
     * @param member 会员信息
     * @return 会员信息
     */
    @Override
    public List<Member> selectMemberList(Member member)
    {
        return memberMapper.selectMemberList(member);
    }

    /**
     * 新增会员信息
     *
     * @param member 会员信息
     * @return 结果
     */
    @Override
    public int insertMember(Member member)
    {
        //member.setCreateTime(DateUtils.getNowDate());
        return memberMapper.insertMember(member);
    }

    /**
     * 修改会员信息
     *
     * @param member 会员信息
     * @return 结果
     */
    @Override
    public int updateMember(Member member)
    {
        //member.setUpdateTime(DateUtils.getNowDate());
        return memberMapper.updateMember(member);
    }


    /**
     * 删除会员信息信息
     *
     * @param id 会员信息主键
     * @return 结果
     */
    @Override
    public int deleteMemberById(Long id)
    {
        return memberMapper.deleteMemberById(id);
    }

}
