package com.financia.system.service.impl;

import com.financia.business.MemberBank;
import com.financia.system.mapper.MemberBankMapper;
import com.financia.system.service.IMemberBankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * memberBankService业务层处理
 * 
 * @author 花生
 * @date 2022-08-17
 */
@Service("memberBankService")
public class MemberBankServiceImpl implements IMemberBankService
{
    @Autowired
    private MemberBankMapper memberBankMapper;

    /**
     * 查询memberBank
     * 
     * @param id memberBank主键
     * @return memberBank
     */
    @Override
    public MemberBank selectMemberBankById(Long id)
    {
        return memberBankMapper.selectMemberBankById(id);
    }

    /**
     * 查询memberBank列表
     * 
     * @param memberBank memberBank
     * @return memberBank
     */
    @Override
    public List<MemberBank> selectMemberBankList(MemberBank memberBank)
    {
        return memberBankMapper.selectMemberBankList(memberBank);
    }

    /**
     * 新增memberBank
     * 
     * @param memberBank memberBank
     * @return 结果
     */
    @Override
    public int insertMemberBank(MemberBank memberBank)
    {
        return memberBankMapper.insertMemberBank(memberBank);
    }

    /**
     * 修改memberBank
     * 
     * @param memberBank memberBank
     * @return 结果
     */
    @Override
    public int updateMemberBank(MemberBank memberBank)
    {
        return memberBankMapper.updateMemberBank(memberBank);
    }

    /**
     * 批量删除memberBank
     * 
     * @param ids 需要删除的memberBank主键
     * @return 结果
     */
    @Override
    public int deleteMemberBankByIds(Long[] ids)
    {
        return memberBankMapper.deleteMemberBankByIds(ids);
    }

    /**
     * 删除memberBank信息
     * 
     * @param id memberBank主键
     * @return 结果
     */
    @Override
    public int deleteMemberBankById(Long id)
    {
        return memberBankMapper.deleteMemberBankById(id);
    }
}
