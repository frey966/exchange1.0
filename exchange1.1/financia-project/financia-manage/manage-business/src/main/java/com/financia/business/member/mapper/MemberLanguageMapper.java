package com.financia.business.member.mapper;

import com.financia.business.MemberLanguage;

import java.util.List;

/**
 * 语言Mapper接口
 * 
 * @author 花生
 * @date 2022-08-18
 */
public interface MemberLanguageMapper 
{
    /**
     * 查询语言
     * 
     * @param id 语言主键
     * @return 语言
     */
     MemberLanguage selectMemberLanguageById(Long id);

    /**
     * 查询语言列表
     * 
     * @param memberLanguage 语言
     * @return 语言集合
     */
     List<MemberLanguage> selectMemberLanguageList(MemberLanguage memberLanguage);

    /**
     * 新增语言
     * 
     * @param memberLanguage 语言
     * @return 结果
     */
     int insertMemberLanguage(MemberLanguage memberLanguage);

    /**
     * 修改语言
     * 
     * @param memberLanguage 语言
     * @return 结果
     */
     int updateMemberLanguage(MemberLanguage memberLanguage);

    /**
     * 删除语言
     * 
     * @param id 语言主键
     * @return 结果
     */
     int deleteMemberLanguageById(Long id);

    /**
     * 批量删除语言
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
     int deleteMemberLanguageByIds(Long[] ids);
}
