package com.financia.business.dataconfiguration.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financia.business.ExContractCoin;

import java.util.List;

/**
 * 合约币种列Mapper接口
 *
 * @author 花生
 * @date 2022-08-03
 */
public interface ExContractCoinMapper extends BaseMapper<ExContractCoin>
{
    /**
     * 查询合约币种列
     *
     * @param id 合约币种列主键
     * @return 合约币种列
     */
    public ExContractCoin selectExContractCoinById(Long id);

    /**
     * 查询合约币种列列表
     *
     * @param exContractCoin 合约币种列
     * @return 合约币种列集合
     */
    public List<ExContractCoin> selectExContractCoinList(ExContractCoin exContractCoin);

    /**
     * 新增合约币种列
     *
     * @param exContractCoin 合约币种列
     * @return 结果
     */
    public int insertExContractCoin(ExContractCoin exContractCoin);

    /**
     * 修改合约币种列
     *
     * @param exContractCoin 合约币种列
     * @return 结果
     */
    public int updateExContractCoin(ExContractCoin exContractCoin);

    /**
     * 删除合约币种列
     *
     * @param id 合约币种列主键
     * @return 结果
     */
    public int deleteExContractCoinById(Long id);

    /**
     * 批量删除合约币种列
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteExContractCoinByIds(Long[] ids);
}
