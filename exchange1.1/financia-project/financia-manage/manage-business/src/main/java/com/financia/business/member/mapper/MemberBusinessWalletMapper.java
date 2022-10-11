package com.financia.business.member.mapper;

import com.financia.exchange.MemberBusinessWallet;

import java.util.List;

/**
 * 会员-业务钱包金额Mapper接口
 * 
 * @author 花生
 * @date 2022-08-11
 */
public interface MemberBusinessWalletMapper 
{
    /**
     * 查询会员-业务钱包金额
     * 
     * @param id 会员-业务钱包金额主键
     * @return 会员-业务钱包金额
     */
    public MemberBusinessWallet selectMemberBusinessWalletById(Long id);

    /**
     * 查询会员-业务钱包金额列表
     * 
     * @param memberBusinessWallet 会员-业务钱包金额
     * @return 会员-业务钱包金额集合
     */
    public List<MemberBusinessWallet> selectMemberBusinessWalletList(MemberBusinessWallet memberBusinessWallet);

    /**
     * 新增会员-业务钱包金额
     * 
     * @param memberBusinessWallet 会员-业务钱包金额
     * @return 结果
     */
    public int insertMemberBusinessWallet(MemberBusinessWallet memberBusinessWallet);

    /**
     * 修改会员-业务钱包金额
     * 
     * @param memberBusinessWallet 会员-业务钱包金额
     * @return 结果
     */
    public int updateMemberBusinessWallet(MemberBusinessWallet memberBusinessWallet);

    /**
     * 删除会员-业务钱包金额
     * 
     * @param id 会员-业务钱包金额主键
     * @return 结果
     */
    public int deleteMemberBusinessWalletById(Long id);

    /**
     * 批量删除会员-业务钱包金额
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteMemberBusinessWalletByIds(Long[] ids);
}
