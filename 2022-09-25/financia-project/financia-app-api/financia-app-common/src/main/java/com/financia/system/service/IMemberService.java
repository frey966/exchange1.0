package com.financia.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.financia.exchange.Member;

import java.util.List;

/**
 * 会员信息Service接口
 *
 * @author ruoyi
 * @date 2022-07-13
 */
public interface IMemberService extends IService<Member>
{
    /**
     * 查询会员信息
     *
     * @param id 会员信息主键
     * @return 会员信息
     */
     Member selectMemberById(Long id);

    /**
     * 查询会员信息列表
     *
     * @param member 会员信息
     * @return 会员信息集合
     */
     List<Member> selectMemberList(Member member);

    /**
     * 新增会员信息
     *
     * @param member 会员信息
     * @return 结果
     */
     int insertMember(Member member);

    /**
     * 修改会员信息
     *
     * @param member 会员信息
     * @return 结果
     */
     int updateMember(Member member);

    /**
     * 批量删除会员信息
     *
     * @param ids 需要删除的会员信息主键集合
     * @return 结果
     */
     int deleteMemberByIds(Long[] ids);

    /**
     * 删除会员信息信息
     *
     * @param id 会员信息主键
     * @return 结果
     */
     int deleteMemberById(Long id);

    /**
     * 查询会员信息
     *
     * @param phone 电话
     * @return 会员信息
     */
     Member getMemberByPhone(String phone);

    /**
     * 查询会员信息
     *
     * @param email 邮箱
     * @return 会员信息
     */
     Member getMemberByEmail(String email);
}
