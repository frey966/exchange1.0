package com.financia.business.member.service.impl;

import com.financia.business.member.mapper.MemberBusinessWalletMapper;
import com.financia.business.member.service.IMemberBusinessWalletService;
import com.financia.common.core.utils.DateUtils;
import com.financia.exchange.MemberBusinessWallet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 会员-业务钱包金额Service业务层处理
 * 
 * @author 花生
 * @date 2022-08-11
 */
@Service
public class MemberBusinessWalletServiceImpl implements IMemberBusinessWalletService
{
    @Autowired
    private MemberBusinessWalletMapper memberBusinessWalletMapper;

    /**
     * 查询会员-业务钱包金额
     * 
     * @param id 会员-业务钱包金额主键
     * @return 会员-业务钱包金额
     */
    @Override
    public MemberBusinessWallet selectMemberBusinessWalletById(Long id)
    {
        return memberBusinessWalletMapper.selectMemberBusinessWalletById(id);
    }

    /**
     * 查询会员-业务钱包金额列表
     * 
     * @param memberBusinessWallet 会员-业务钱包金额
     * @return 会员-业务钱包金额
     */
    @Override
    public List<MemberBusinessWallet> selectMemberBusinessWalletList(MemberBusinessWallet memberBusinessWallet)
    {
        return memberBusinessWalletMapper.selectMemberBusinessWalletList(memberBusinessWallet);
    }

    /**
     * 新增会员-业务钱包金额
     * 
     * @param memberBusinessWallet 会员-业务钱包金额
     * @return 结果
     */
    @Override
    public int insertMemberBusinessWallet(MemberBusinessWallet memberBusinessWallet)
    {
        memberBusinessWallet.setCreateTime(DateUtils.getNowDate());
        return memberBusinessWalletMapper.insertMemberBusinessWallet(memberBusinessWallet);
    }

    /**
     * 修改会员-业务钱包金额
     * 
     * @param memberBusinessWallet 会员-业务钱包金额
     * @return 结果
     */
    @Override
    public int updateMemberBusinessWallet(MemberBusinessWallet memberBusinessWallet)
    {
        return memberBusinessWalletMapper.updateMemberBusinessWallet(memberBusinessWallet);
    }

    /**
     * 批量删除会员-业务钱包金额
     * 
     * @param ids 需要删除的会员-业务钱包金额主键
     * @return 结果
     */
    @Override
    public int deleteMemberBusinessWalletByIds(Long[] ids)
    {
        return memberBusinessWalletMapper.deleteMemberBusinessWalletByIds(ids);
    }

    /**
     * 删除会员-业务钱包金额信息
     * 
     * @param id 会员-业务钱包金额主键
     * @return 结果
     */
    @Override
    public int deleteMemberBusinessWalletById(Long id)
    {
        return memberBusinessWalletMapper.deleteMemberBusinessWalletById(id);
    }
}
