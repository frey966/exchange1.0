package com.financia.system.service.impl;

import java.util.List;
import com.financia.common.core.utils.DateUtils;
import com.financia.exchange.POmpanyBank;
import com.financia.system.mapper.POmpanyBankMapper;
import com.financia.system.service.IPOmpanyBankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 公司收款银行卡Service业务层处理
 * 
 * @author 花生
 * @date 2022-09-18
 */
@Service
public class POmpanyBankServiceImpl implements IPOmpanyBankService
{
    @Autowired
    private POmpanyBankMapper pOmpanyBankMapper;

    /**
     * 查询公司收款银行卡
     * 
     * @param id 公司收款银行卡主键
     * @return 公司收款银行卡
     */
    @Override
    public POmpanyBank selectPOmpanyBankById(Long id)
    {
        return pOmpanyBankMapper.selectPOmpanyBankById(id);
    }

    /**
     * 查询公司收款银行卡列表
     * 
     * @param pOmpanyBank 公司收款银行卡
     * @return 公司收款银行卡
     */
    @Override
    public List<POmpanyBank> selectPOmpanyBankList(POmpanyBank pOmpanyBank)
    {
        return pOmpanyBankMapper.selectPOmpanyBankList(pOmpanyBank);
    }

    /**
     * 新增公司收款银行卡
     * 
     * @param pOmpanyBank 公司收款银行卡
     * @return 结果
     */
    @Override
    public int insertPOmpanyBank(POmpanyBank pOmpanyBank)
    {
        return pOmpanyBankMapper.insertPOmpanyBank(pOmpanyBank);
    }

    /**
     * 修改公司收款银行卡
     * 
     * @param pOmpanyBank 公司收款银行卡
     * @return 结果
     */
    @Override
    public int updatePOmpanyBank(POmpanyBank pOmpanyBank)
    {
        return pOmpanyBankMapper.updatePOmpanyBank(pOmpanyBank);
    }

    /**
     * 批量删除公司收款银行卡
     * 
     * @param ids 需要删除的公司收款银行卡主键
     * @return 结果
     */
    @Override
    public int deletePOmpanyBankByIds(Long[] ids)
    {
        return pOmpanyBankMapper.deletePOmpanyBankByIds(ids);
    }

    /**
     * 删除公司收款银行卡信息
     * 
     * @param id 公司收款银行卡主键
     * @return 结果
     */
    @Override
    public int deletePOmpanyBankById(Long id)
    {
        return pOmpanyBankMapper.deletePOmpanyBankById(id);
    }
}
