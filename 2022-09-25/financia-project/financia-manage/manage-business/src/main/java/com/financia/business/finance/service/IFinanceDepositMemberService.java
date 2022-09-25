package com.financia.business.finance.service;

import com.financia.finance.FinanceDepositMember;

import java.util.List;

/**
 * 量化理财-会员存款Service接口
 * 
 * @author 花生
 * @date 2022-08-15
 */
public interface IFinanceDepositMemberService 
{
    /**
     * 查询量化理财-会员存款
     * 
     * @param id 量化理财-会员存款主键
     * @return 量化理财-会员存款
     */
     FinanceDepositMember selectFinanceDepositMemberById(FinanceDepositMember financeDepositMember);

    /**
     * 查询量化理财-会员存款列表
     * 
     * @param financeDepositMember 量化理财-会员存款
     * @return 量化理财-会员存款集合
     */
     List<FinanceDepositMember> selectFinanceDepositMemberList(FinanceDepositMember financeDepositMember);

    /**
     * 新增量化理财-会员存款
     * 
     * @param financeDepositMember 量化理财-会员存款
     * @return 结果
     */
     int insertFinanceDepositMember(FinanceDepositMember financeDepositMember);

    /**
     * 修改量化理财-会员存款
     * 
     * @param financeDepositMember 量化理财-会员存款
     * @return 结果
     */
     int updateFinanceDepositMember(FinanceDepositMember financeDepositMember);

    /**
     * 批量删除量化理财-会员存款
     * 
     * @param ids 需要删除的量化理财-会员存款主键集合
     * @return 结果
     */
     int deleteFinanceDepositMemberByIds(Long[] ids);

    /**
     * 删除量化理财-会员存款信息
     * 
     * @param id 量化理财-会员存款主键
     * @return 结果
     */
     int deleteFinanceDepositMemberById(Long id);
}
