package com.financia.business.dataconfiguration.service.impl;

import java.util.List;

import com.financia.business.dataconfiguration.mapper.PCompanyWalletAddressMapper;
import com.financia.business.dataconfiguration.service.IPCompanyWalletAddressService;
import com.financia.common.PCompanyWalletAddress;
import com.financia.common.core.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 公链钱包Service业务层处理
 * 
 * @author 花生
 * @date 2022-08-24
 */
@Service
public class PCompanyWalletAddressServiceImpl implements IPCompanyWalletAddressService
{
    @Autowired
    private PCompanyWalletAddressMapper pCompanyWalletAddressMapper;

    /**
     * 查询公链钱包
     * 
     * @param id 公链钱包主键
     * @return 公链钱包
     */
    @Override
    public PCompanyWalletAddress selectPCompanyWalletAddressById(Long id)
    {
        return pCompanyWalletAddressMapper.selectPCompanyWalletAddressById(id);
    }

    /**
     * 查询公链钱包列表
     * 
     * @param pCompanyWalletAddress 公链钱包
     * @return 公链钱包
     */
    @Override
    public List<PCompanyWalletAddress> selectPCompanyWalletAddressList(PCompanyWalletAddress pCompanyWalletAddress)
    {
        return pCompanyWalletAddressMapper.selectPCompanyWalletAddressList(pCompanyWalletAddress);
    }

    /**
     * 新增公链钱包
     * 
     * @param pCompanyWalletAddress 公链钱包
     * @return 结果
     */
    @Override
    public int insertPCompanyWalletAddress(PCompanyWalletAddress pCompanyWalletAddress)
    {
        return pCompanyWalletAddressMapper.insertPCompanyWalletAddress(pCompanyWalletAddress);
    }

    /**
     * 修改公链钱包
     * 
     * @param pCompanyWalletAddress 公链钱包
     * @return 结果
     */
    @Override
    public int updatePCompanyWalletAddress(PCompanyWalletAddress pCompanyWalletAddress)
    {
        return pCompanyWalletAddressMapper.updatePCompanyWalletAddress(pCompanyWalletAddress);
    }

    /**
     * 批量删除公链钱包
     * 
     * @param ids 需要删除的公链钱包主键
     * @return 结果
     */
    @Override
    public int deletePCompanyWalletAddressByIds(Long[] ids)
    {
        return pCompanyWalletAddressMapper.deletePCompanyWalletAddressByIds(ids);
    }

    /**
     * 删除公链钱包信息
     * 
     * @param id 公链钱包主键
     * @return 结果
     */
    @Override
    public int deletePCompanyWalletAddressById(Long id)
    {
        return pCompanyWalletAddressMapper.deletePCompanyWalletAddressById(id);
    }
}
