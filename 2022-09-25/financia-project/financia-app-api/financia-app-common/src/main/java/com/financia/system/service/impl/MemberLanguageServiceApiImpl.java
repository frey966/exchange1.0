package com.financia.system.service.impl;

import com.financia.business.MemberLanguage;
import com.financia.system.mapper.MemberLanguageMapper;
import com.financia.system.service.IMemberLanguageServiceApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 语言Service业务层处理
 * 
 * @author 花生
 * @date 2022-08-18
 */
@Service
public class MemberLanguageServiceApiImpl implements IMemberLanguageServiceApi
{
    @Autowired
    private MemberLanguageMapper memberLanguageMapper;

    /**
     * 查询语言
     * 
     * @param id 语言主键
     * @return 语言
     */
    @Override
    public MemberLanguage selectMemberLanguageById(Long id)
    {
        return memberLanguageMapper.selectMemberLanguageById(id);
    }

    /**
     * 查询语言列表
     * 
     * @param memberLanguage 语言
     * @return 语言
     */
    @Override
    public List<MemberLanguage> selectMemberLanguageList(MemberLanguage memberLanguage)
    {
        return memberLanguageMapper.selectMemberLanguageList(memberLanguage);
    }

}
