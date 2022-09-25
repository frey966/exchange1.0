package com.financia.business.member.service;

import com.financia.exchange.MemberWalletAddress;

import java.util.List;

/**
 * 公链钱包Service接口
 * 
 * @author 花生
 * @date 2022-08-11
 */
public interface IMemberWalletAddressService 
{
    /**
     * 查询公链钱包
     * 
     * @param id 公链钱包主键
     * @return 公链钱包
     */
    public MemberWalletAddress selectMemberWalletAddressById(Long id);

    /**
     * 查询公链钱包列表
     * 
     * @param memberWalletAddress 公链钱包
     * @return 公链钱包集合
     */
    public List<MemberWalletAddress> selectMemberWalletAddressList(MemberWalletAddress memberWalletAddress);

    /**
     * 新增公链钱包
     * 
     * @param memberWalletAddress 公链钱包
     * @return 结果
     */
    public int insertMemberWalletAddress(MemberWalletAddress memberWalletAddress);

    /**
     * 修改公链钱包
     * 
     * @param memberWalletAddress 公链钱包
     * @return 结果
     */
    public int updateMemberWalletAddress(MemberWalletAddress memberWalletAddress);

    /**
     * 批量删除公链钱包
     * 
     * @param ids 需要删除的公链钱包主键集合
     * @return 结果
     */
    public int deleteMemberWalletAddressByIds(Long[] ids);

    /**
     * 删除公链钱包信息
     * 
     * @param id 公链钱包主键
     * @return 结果
     */
    public int deleteMemberWalletAddressById(Long id);
}
