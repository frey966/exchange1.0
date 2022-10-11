package com.financia.business.member.service.impl;

import java.util.List;

import com.financia.business.MemberLanguage;
import com.financia.business.member.mapper.MemberLanguageMapper;
import com.financia.business.member.service.IMemberLanguageService;
import com.financia.common.core.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 语言Service业务层处理
 * 
 * @author 花生
 * @date 2022-08-18
 */
@Service
public class MemberLanguageServiceImpl implements IMemberLanguageService
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

    /**
     * 新增语言
     * 
     * @param memberLanguage 语言
     * @return 结果
     */
    @Override
    public int insertMemberLanguage(MemberLanguage memberLanguage)
    {
        return memberLanguageMapper.insertMemberLanguage(memberLanguage);
    }

    /**
     * 修改语言
     * 
     * @param memberLanguage 语言
     * @return 结果
     */
    @Override
    public int updateMemberLanguage(MemberLanguage memberLanguage)
    {
        return memberLanguageMapper.updateMemberLanguage(memberLanguage);
    }

    /**
     * 批量删除语言
     * 
     * @param ids 需要删除的语言主键
     * @return 结果
     */
    @Override
    public int deleteMemberLanguageByIds(Long[] ids)
    {
        return memberLanguageMapper.deleteMemberLanguageByIds(ids);
    }

    /**
     * 删除语言信息
     * 
     * @param id 语言主键
     * @return 结果
     */
    @Override
    public int deleteMemberLanguageById(Long id)
    {
        return memberLanguageMapper.deleteMemberLanguageById(id);
    }
}
