package com.financia.system.mapper;

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
    public MemberLanguage selectMemberLanguageById(Long id);

    /**
     * 查询语言列表
     * 
     * @param memberLanguage 语言
     * @return 语言集合
     */
    public List<MemberLanguage> selectMemberLanguageList(MemberLanguage memberLanguage);


}
