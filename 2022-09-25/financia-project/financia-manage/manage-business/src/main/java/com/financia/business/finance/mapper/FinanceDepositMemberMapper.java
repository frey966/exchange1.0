package com.financia.business.finance.mapper;

import com.financia.finance.FinanceDepositMember;

import java.util.List;

/**
 * 量化理财-会员存款Mapper接口
 * 
 * @author 花生
 * @date 2022-08-15
 */
public interface FinanceDepositMemberMapper 
{
    /**
     * 查询量化理财-会员存款
     * 
     * @param id 量化理财-会员存款主键
     * @return 量化理财-会员存款
     */
    public FinanceDepositMember selectFinanceDepositMemberById(FinanceDepositMember financeDepositMember);

    /**
     * 查询量化理财-会员存款列表
     * 
     * @param financeDepositMember 量化理财-会员存款
     * @return 量化理财-会员存款集合
     */
    public List<FinanceDepositMember> selectFinanceDepositMemberList(FinanceDepositMember financeDepositMember);

    /**
     * 新增量化理财-会员存款
     * 
     * @param financeDepositMember 量化理财-会员存款
     * @return 结果
     */
    public int insertFinanceDepositMember(FinanceDepositMember financeDepositMember);

    /**
     * 修改量化理财-会员存款
     * 
     * @param financeDepositMember 量化理财-会员存款
     * @return 结果
     */
    public int updateFinanceDepositMember(FinanceDepositMember financeDepositMember);

    /**
     * 删除量化理财-会员存款
     * 
     * @param id 量化理财-会员存款主键
     * @return 结果
     */
    public int deleteFinanceDepositMemberById(Long id);

    /**
     * 批量删除量化理财-会员存款
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFinanceDepositMemberByIds(Long[] ids);
}
