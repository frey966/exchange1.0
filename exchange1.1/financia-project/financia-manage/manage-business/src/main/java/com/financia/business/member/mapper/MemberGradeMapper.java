package com.financia.business.member.mapper;

import com.financia.business.MemberGrade;

import java.util.List;

/**
 * 会员等级Mapper接口
 * 
 * @author 花生
 * @date 2022-08-05
 */
public interface MemberGradeMapper 
{
    /**
     * 查询会员等级
     * 
     * @param id 会员等级主键
     * @return 会员等级
     */
    public MemberGrade selectMemberGradeById(Long id);

    /**
     * 查询会员等级列表
     * 
     * @param memberGrade 会员等级
     * @return 会员等级集合
     */
    public List<MemberGrade> selectMemberGradeList(MemberGrade memberGrade);

    /**
     * 新增会员等级
     * 
     * @param memberGrade 会员等级
     * @return 结果
     */
    public int insertMemberGrade(MemberGrade memberGrade);

    /**
     * 修改会员等级
     * 
     * @param memberGrade 会员等级
     * @return 结果
     */
    public int updateMemberGrade(MemberGrade memberGrade);

    /**
     * 删除会员等级
     * 
     * @param id 会员等级主键
     * @return 结果
     */
    public int deleteMemberGradeById(Long id);

    /**
     * 批量删除会员等级
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteMemberGradeByIds(Long[] ids);
}
