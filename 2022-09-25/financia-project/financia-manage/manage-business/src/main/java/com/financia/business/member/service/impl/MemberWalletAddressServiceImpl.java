package com.financia.business.member.service.impl;

import java.util.List;

import com.financia.business.member.mapper.MemberWalletAddressMapper;
import com.financia.business.member.service.IMemberWalletAddressService;
import com.financia.common.core.utils.DateUtils;
import com.financia.exchange.MemberWalletAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 公链钱包Service业务层处理
 * 
 * @author 花生
 * @date 2022-08-11
 */
@Service
public class MemberWalletAddressServiceImpl implements IMemberWalletAddressService
{
    @Autowired
    private MemberWalletAddressMapper memberWalletAddressMapper;

    /**
     * 查询公链钱包
     * 
     * @param id 公链钱包主键
     * @return 公链钱包
     */
    @Override
    public MemberWalletAddress selectMemberWalletAddressById(Long id)
    {
        return memberWalletAddressMapper.selectMemberWalletAddressById(id);
    }

    /**
     * 查询公链钱包列表
     * 
     * @param memberWalletAddress 公链钱包
     * @return 公链钱包
     */
    @Override
    public List<MemberWalletAddress> selectMemberWalletAddressList(MemberWalletAddress memberWalletAddress)
    {
        return memberWalletAddressMapper.selectMemberWalletAddressList(memberWalletAddress);
    }

    /**
     * 新增公链钱包
     * 
     * @param memberWalletAddress 公链钱包
     * @return 结果
     */
    @Override
    public int insertMemberWalletAddress(MemberWalletAddress memberWalletAddress)
    {
        memberWalletAddress.setCreateTime(DateUtils.getNowDate());
        return memberWalletAddressMapper.insertMemberWalletAddress(memberWalletAddress);
    }

    /**
     * 修改公链钱包
     * 
     * @param memberWalletAddress 公链钱包
     * @return 结果
     */
    @Override
    public int updateMemberWalletAddress(MemberWalletAddress memberWalletAddress)
    {
        memberWalletAddress.setUpdateTime(DateUtils.getNowDate());
        return memberWalletAddressMapper.updateMemberWalletAddress(memberWalletAddress);
    }

    /**
     * 批量删除公链钱包
     * 
     * @param ids 需要删除的公链钱包主键
     * @return 结果
     */
    @Override
    public int deleteMemberWalletAddressByIds(Long[] ids)
    {
        return memberWalletAddressMapper.deleteMemberWalletAddressByIds(ids);
    }

    /**
     * 删除公链钱包信息
     * 
     * @param id 公链钱包主键
     * @return 结果
     */
    @Override
    public int deleteMemberWalletAddressById(Long id)
    {
        return memberWalletAddressMapper.deleteMemberWalletAddressById(id);
    }
}
