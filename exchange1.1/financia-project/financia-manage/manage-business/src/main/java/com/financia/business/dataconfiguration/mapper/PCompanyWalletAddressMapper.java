package com.financia.business.dataconfiguration.mapper;

import com.financia.common.PCompanyWalletAddress;

import java.util.List;

/**
 * 公链钱包Mapper接口
 * 
 * @author 花生
 * @date 2022-08-24
 */
public interface PCompanyWalletAddressMapper 
{
    /**
     * 查询公链钱包
     * 
     * @param id 公链钱包主键
     * @return 公链钱包
     */
    public PCompanyWalletAddress selectPCompanyWalletAddressById(Long id);

    /**
     * 查询公链钱包列表
     * 
     * @param pCompanyWalletAddress 公链钱包
     * @return 公链钱包集合
     */
    public List<PCompanyWalletAddress> selectPCompanyWalletAddressList(PCompanyWalletAddress pCompanyWalletAddress);

    /**
     * 新增公链钱包
     * 
     * @param pCompanyWalletAddress 公链钱包
     * @return 结果
     */
    public int insertPCompanyWalletAddress(PCompanyWalletAddress pCompanyWalletAddress);

    /**
     * 修改公链钱包
     * 
     * @param pCompanyWalletAddress 公链钱包
     * @return 结果
     */
    public int updatePCompanyWalletAddress(PCompanyWalletAddress pCompanyWalletAddress);

    /**
     * 删除公链钱包
     * 
     * @param id 公链钱包主键
     * @return 结果
     */
    public int deletePCompanyWalletAddressById(Long id);

    /**
     * 批量删除公链钱包
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePCompanyWalletAddressByIds(Long[] ids);
}
