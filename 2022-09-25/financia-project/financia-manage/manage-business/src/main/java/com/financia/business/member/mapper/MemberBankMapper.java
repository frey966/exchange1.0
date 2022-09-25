package com.financia.business.member.mapper;

import com.financia.business.MemberBank;

import java.util.List;

/**
 * memberBankMapper接口
 * 
 * @author 花生
 * @date 2022-08-17
 */
public interface MemberBankMapper 
{
    /**
     * 查询memberBank
     * 
     * @param id memberBank主键
     * @return memberBank
     */
    public MemberBank selectMemberBankById(Long id);

    /**
     * 查询memberBank列表
     * 
     * @param memberBank memberBank
     * @return memberBank集合
     */
    public List<MemberBank> selectMemberBankList(MemberBank memberBank);

    /**
     * 新增memberBank
     * 
     * @param memberBank memberBank
     * @return 结果
     */
    public int insertMemberBank(MemberBank memberBank);

    /**
     * 修改memberBank
     * 
     * @param memberBank memberBank
     * @return 结果
     */
    public int updateMemberBank(MemberBank memberBank);

    /**
     * 删除memberBank
     * 
     * @param id memberBank主键
     * @return 结果
     */
    public int deleteMemberBankById(Long id);

    /**
     * 批量删除memberBank
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteMemberBankByIds(Long[] ids);
}
