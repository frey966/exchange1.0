package com.financia.business.finance.service.impl;

import java.util.List;

import com.financia.business.finance.mapper.FinanceDepositMemberMapper;
import com.financia.business.finance.service.IFinanceDepositMemberService;
import com.financia.finance.FinanceDepositMember;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 量化理财-会员存款Service业务层处理
 * 
 * @author 花生
 * @date 2022-08-15
 */
@Service
public class FinanceDepositMemberServiceImpl implements IFinanceDepositMemberService
{
    @Autowired
    private FinanceDepositMemberMapper financeDepositMemberMapper;

    /**
     * 查询量化理财-会员存款
     * 
     * @param id 量化理财-会员存款主键
     * @return 量化理财-会员存款
     */
    @Override
    public FinanceDepositMember selectFinanceDepositMemberById(FinanceDepositMember financeDepositMember)
    {
        return financeDepositMemberMapper.selectFinanceDepositMemberById(financeDepositMember);
    }

    /**
     * 查询量化理财-会员存款列表
     * 
     * @param financeDepositMember 量化理财-会员存款
     * @return 量化理财-会员存款
     */
    @Override
    public List<FinanceDepositMember> selectFinanceDepositMemberList(FinanceDepositMember financeDepositMember)
    {
        return financeDepositMemberMapper.selectFinanceDepositMemberList(financeDepositMember);
    }

    /**
     * 新增量化理财-会员存款
     * 
     * @param financeDepositMember 量化理财-会员存款
     * @return 结果
     */
    @Override
    public int insertFinanceDepositMember(FinanceDepositMember financeDepositMember)
    {
        return financeDepositMemberMapper.insertFinanceDepositMember(financeDepositMember);
    }

    /**
     * 修改量化理财-会员存款
     * 
     * @param financeDepositMember 量化理财-会员存款
     * @return 结果
     */
    @Override
    public int updateFinanceDepositMember(FinanceDepositMember financeDepositMember)
    {
        return financeDepositMemberMapper.updateFinanceDepositMember(financeDepositMember);
    }

    /**
     * 批量删除量化理财-会员存款
     * 
     * @param ids 需要删除的量化理财-会员存款主键
     * @return 结果
     */
    @Override
    public int deleteFinanceDepositMemberByIds(Long[] ids)
    {
        return financeDepositMemberMapper.deleteFinanceDepositMemberByIds(ids);
    }

    /**
     * 删除量化理财-会员存款信息
     * 
     * @param id 量化理财-会员存款主键
     * @return 结果
     */
    @Override
    public int deleteFinanceDepositMemberById(Long id)
    {
        return financeDepositMemberMapper.deleteFinanceDepositMemberById(id);
    }
}
