package com.financia.business.dataconfiguration.mapper;

import com.financia.exchange.MemberSuperContractWallet;

import java.util.List;

/**
 * 超级杠杆-持仓Mapper接口
 * 
 * @author 花生
 * @date 2022-09-07
 */
public interface MemberSuperContractWalletMapper 
{
    /**
     * 查询超级杠杆-持仓
     * 
     * @param id 超级杠杆-持仓主键
     * @return 超级杠杆-持仓
     */
    public MemberSuperContractWallet selectMemberSuperContractWalletById(Long id);

    /**
     * 查询超级杠杆-持仓列表
     * 
     * @param memberSuperContractWallet 超级杠杆-持仓
     * @return 超级杠杆-持仓集合
     */
    public List<MemberSuperContractWallet> selectMemberSuperContractWalletList(MemberSuperContractWallet memberSuperContractWallet);

    /**
     * 新增超级杠杆-持仓
     * 
     * @param memberSuperContractWallet 超级杠杆-持仓
     * @return 结果
     */
    public int insertMemberSuperContractWallet(MemberSuperContractWallet memberSuperContractWallet);

    /**
     * 修改超级杠杆-持仓
     * 
     * @param memberSuperContractWallet 超级杠杆-持仓
     * @return 结果
     */
    public int updateMemberSuperContractWallet(MemberSuperContractWallet memberSuperContractWallet);

    /**
     * 删除超级杠杆-持仓
     * 
     * @param id 超级杠杆-持仓主键
     * @return 结果
     */
    public int deleteMemberSuperContractWalletById(Long id);

    /**
     * 批量删除超级杠杆-持仓
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteMemberSuperContractWalletByIds(Long[] ids);
}
