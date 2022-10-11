package com.financia.business.dataconfiguration.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.financia.business.ExContractCoin;
import com.financia.business.dataconfiguration.mapper.ExContractCoinMapper;
import com.financia.business.dataconfiguration.service.IExContractCoinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 合约币种列Service业务层处理
 *
 * @author 花生
 * @date 2022-08-03
 */
@Service
public class ExContractCoinServiceImpl extends ServiceImpl<ExContractCoinMapper, ExContractCoin> implements IExContractCoinService
{
    @Autowired
    private ExContractCoinMapper exContractCoinMapper;

    /**
     * 查询合约币种列
     *
     * @param id 合约币种列主键
     * @return 合约币种列
     */
    @Override
    public ExContractCoin selectExContractCoinById(Long id)
    {
        return exContractCoinMapper.selectExContractCoinById(id);
    }

    /**
     * 查询合约币种列列表
     *
     * @param exContractCoin 合约币种列
     * @return 合约币种列
     */
    @Override
    public List<ExContractCoin> selectExContractCoinList(ExContractCoin exContractCoin)
    {
        return exContractCoinMapper.selectExContractCoinList(exContractCoin);
    }

    /**
     * 新增合约币种列
     *
     * @param exContractCoin 合约币种列
     * @return 结果
     */
    @Override
    public int insertExContractCoin(ExContractCoin exContractCoin)
    {
        return exContractCoinMapper.insertExContractCoin(exContractCoin);
    }

    /**
     * 修改合约币种列
     *
     * @param exContractCoin 合约币种列
     * @return 结果
     */
    @Override
    public int updateExContractCoin(ExContractCoin exContractCoin)
    {
        return exContractCoinMapper.updateExContractCoin(exContractCoin);
    }

    /**
     * 批量删除合约币种列
     *
     * @param ids 需要删除的合约币种列主键
     * @return 结果
     */
    @Override
    public int deleteExContractCoinByIds(Long[] ids)
    {
        return exContractCoinMapper.deleteExContractCoinByIds(ids);
    }

    /**
     * 删除合约币种列信息
     *
     * @param id 合约币种列主键
     * @return 结果
     */
    @Override
    public int deleteExContractCoinById(Long id)
    {
        return exContractCoinMapper.deleteExContractCoinById(id);
    }
}
