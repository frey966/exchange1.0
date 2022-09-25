package com.financia.business.dataconfiguration.service;

import java.util.List;

import com.financia.exchange.POmpanyBank;

/**
 * 公司收款银行卡Service接口
 * 
 * @author 花生
 * @date 2022-09-18
 */
public interface IPOmpanyBankService 
{
    /**
     * 查询公司收款银行卡
     * 
     * @param id 公司收款银行卡主键
     * @return 公司收款银行卡
     */
    public POmpanyBank selectPOmpanyBankById(Long id);

    /**
     * 查询公司收款银行卡列表
     * 
     * @param pOmpanyBank 公司收款银行卡
     * @return 公司收款银行卡集合
     */
    public List<POmpanyBank> selectPOmpanyBankList(POmpanyBank pOmpanyBank);

    /**
     * 新增公司收款银行卡
     * 
     * @param pOmpanyBank 公司收款银行卡
     * @return 结果
     */
    public int insertPOmpanyBank(POmpanyBank pOmpanyBank);

    /**
     * 修改公司收款银行卡
     * 
     * @param pOmpanyBank 公司收款银行卡
     * @return 结果
     */
    public int updatePOmpanyBank(POmpanyBank pOmpanyBank);

    /**
     * 批量删除公司收款银行卡
     * 
     * @param ids 需要删除的公司收款银行卡主键集合
     * @return 结果
     */
    public int deletePOmpanyBankByIds(Long[] ids);

    /**
     * 删除公司收款银行卡信息
     * 
     * @param id 公司收款银行卡主键
     * @return 结果
     */
    public int deletePOmpanyBankById(Long id);
}
