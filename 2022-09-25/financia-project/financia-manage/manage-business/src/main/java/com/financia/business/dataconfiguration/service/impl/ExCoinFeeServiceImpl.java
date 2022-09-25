package com.financia.business.dataconfiguration.service.impl;

import java.util.List;

import com.financia.business.ExCoinFee;
import com.financia.business.dataconfiguration.mapper.ExCoinFeeMapper;
import com.financia.business.dataconfiguration.service.IExCoinFeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 合约-杠杆手续费率Service业务层处理
 *
 * @author 花生
 * @date 2022-08-11
 */
@Service
public class ExCoinFeeServiceImpl implements IExCoinFeeService
{
    @Autowired
    private ExCoinFeeMapper exCoinFeeMapper;

    /**
     * 查询合约-杠杆手续费率
     *
     * @param id 合约-杠杆手续费率主键
     * @return 合约-杠杆手续费率
     */
    @Override
    public ExCoinFee selectExCoinFeeById(Long id)
    {
        return exCoinFeeMapper.selectExCoinFeeById(id);
    }

    /**
     * 查询合约-杠杆手续费率列表
     *
     * @param exCoinFee 合约-杠杆手续费率
     * @return 合约-杠杆手续费率
     */
    @Override
    public List<ExCoinFee> selectExCoinFeeList(ExCoinFee exCoinFee)
    {
        return exCoinFeeMapper.selectExCoinFeeList(exCoinFee);
    }

    /**
     * 新增合约-杠杆手续费率
     *
     * @param exCoinFee 合约-杠杆手续费率
     * @return 结果
     */
    @Override
    public int insertExCoinFee(ExCoinFee exCoinFee)
    {
        return exCoinFeeMapper.insertExCoinFee(exCoinFee);
    }

    /**
     * 修改合约-杠杆手续费率
     *
     * @param exCoinFee 合约-杠杆手续费率
     * @return 结果
     */
    @Override
    public int updateExCoinFee(ExCoinFee exCoinFee)
    {
        return exCoinFeeMapper.updateExCoinFee(exCoinFee);
    }

    /**
     * 批量删除合约-杠杆手续费率
     *
     * @param ids 需要删除的合约-杠杆手续费率主键
     * @return 结果
     */
    @Override
    public int deleteExCoinFeeByIds(Long[] ids)
    {
        return exCoinFeeMapper.deleteExCoinFeeByIds(ids);
    }

    /**
     * 删除合约-杠杆手续费率信息
     *
     * @param id 合约-杠杆手续费率主键
     * @return 结果
     */
    @Override
    public int deleteExCoinFeeById(Long id)
    {
        return exCoinFeeMapper.deleteExCoinFeeById(id);
    }
}
