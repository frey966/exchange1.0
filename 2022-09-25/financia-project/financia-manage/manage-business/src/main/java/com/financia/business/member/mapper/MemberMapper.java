package com.financia.business.member.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financia.exchange.Member;
import icu.mhb.mybatisplus.plugln.base.mapper.JoinBaseMapper;

import java.util.List;


/**
 * 会员信息Mapper接口
 *
 * @author ruoyi
 * @date 2022-07-13
 */
public interface MemberMapper  extends JoinBaseMapper<Member>
{
    /**
     * 查询会员信息
     *
     * @param id 会员信息主键
     * @return 会员信息
     */
    public Member selectMemberById(Long id);

    /**
     * 查询会员信息列表
     *
     * @param member 会员信息
     * @return 会员信息集合
     */
    public List<Member> selectMemberList(Member member);

    /**
     * 新增会员信息
     *
     * @param member 会员信息
     * @return 结果
     */
    public int insertMember(Member member);

    /**
     * 修改会员信息
     *
     * @param member 会员信息
     * @return 结果
     */
    public int updateMember(Member member);

    /**
     * 删除会员信息
     *
     * @param id 会员信息主键
     * @return 结果
     */
    public int deleteMemberById(Long id);

    /**
     * 批量删除会员信息
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteMemberByIds(Long[] ids);
}
