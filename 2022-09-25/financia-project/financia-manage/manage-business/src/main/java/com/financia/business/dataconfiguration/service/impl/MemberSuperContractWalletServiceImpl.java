package com.financia.business.dataconfiguration.service.impl;

import java.util.List;

import com.financia.business.dataconfiguration.mapper.MemberSuperContractWalletMapper;
import com.financia.business.dataconfiguration.service.IMemberSuperContractWalletService;
import com.financia.common.core.utils.DateUtils;
import com.financia.exchange.MemberSuperContractWallet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 超级杠杆-持仓Service业务层处理
 * 
 * @author 花生
 * @date 2022-09-07
 */
@Service
public class MemberSuperContractWalletServiceImpl implements IMemberSuperContractWalletService
{
    @Autowired
    private MemberSuperContractWalletMapper memberSuperContractWalletMapper;

    /**
     * 查询超级杠杆-持仓
     * 
     * @param id 超级杠杆-持仓主键
     * @return 超级杠杆-持仓
     */
    @Override
    public MemberSuperContractWallet selectMemberSuperContractWalletById(Long id)
    {
        return memberSuperContractWalletMapper.selectMemberSuperContractWalletById(id);
    }

    /**
     * 查询超级杠杆-持仓列表
     * 
     * @param memberSuperContractWallet 超级杠杆-持仓
     * @return 超级杠杆-持仓
     */
    @Override
    public List<MemberSuperContractWallet> selectMemberSuperContractWalletList(MemberSuperContractWallet memberSuperContractWallet)
    {
        return memberSuperContractWalletMapper.selectMemberSuperContractWalletList(memberSuperContractWallet);
    }

    /**
     * 新增超级杠杆-持仓
     * 
     * @param memberSuperContractWallet 超级杠杆-持仓
     * @return 结果
     */
    @Override
    public int insertMemberSuperContractWallet(MemberSuperContractWallet memberSuperContractWallet)
    {
        return memberSuperContractWalletMapper.insertMemberSuperContractWallet(memberSuperContractWallet);
    }

    /**
     * 修改超级杠杆-持仓
     * 
     * @param memberSuperContractWallet 超级杠杆-持仓
     * @return 结果
     */
    @Override
    public int updateMemberSuperContractWallet(MemberSuperContractWallet memberSuperContractWallet)
    {
        return memberSuperContractWalletMapper.updateMemberSuperContractWallet(memberSuperContractWallet);
    }

    /**
     * 批量删除超级杠杆-持仓
     * 
     * @param ids 需要删除的超级杠杆-持仓主键
     * @return 结果
     */
    @Override
    public int deleteMemberSuperContractWalletByIds(Long[] ids)
    {
        return memberSuperContractWalletMapper.deleteMemberSuperContractWalletByIds(ids);
    }

    /**
     * 删除超级杠杆-持仓信息
     * 
     * @param id 超级杠杆-持仓主键
     * @return 结果
     */
    @Override
    public int deleteMemberSuperContractWalletById(Long id)
    {
        return memberSuperContractWalletMapper.deleteMemberSuperContractWalletById(id);
    }
}
