package com.financia.business.finance.service.impl;

import java.util.List;

import com.financia.business.finance.mapper.FinanceInterestMemberMapper;
import com.financia.business.finance.service.IFinanceInterestMemberService;
import com.financia.finance.FinanceInterestMember;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 量化理财-会员存款利息Service业务层处理
 * 
 * @author 花生
 * @date 2022-08-15
 */
@Service
public class FinanceInterestMemberServiceImpl implements IFinanceInterestMemberService
{
    @Autowired
    private FinanceInterestMemberMapper financeInterestMemberMapper;

    /**
     * 查询量化理财-会员存款利息
     * 
     * @param id 量化理财-会员存款利息主键
     * @return 量化理财-会员存款利息
     */
    @Override
    public FinanceInterestMember selectFinanceInterestMemberById(Long id)
    {
        return financeInterestMemberMapper.selectFinanceInterestMemberById(id);
    }

    /**
     * 查询量化理财-会员存款利息列表
     * 
     * @param financeInterestMember 量化理财-会员存款利息
     * @return 量化理财-会员存款利息
     */
    @Override
    public List<FinanceInterestMember> selectFinanceInterestMemberList(FinanceInterestMember financeInterestMember)
    {
        return financeInterestMemberMapper.selectFinanceInterestMemberList(financeInterestMember);
    }

    /**
     * 新增量化理财-会员存款利息
     * 
     * @param financeInterestMember 量化理财-会员存款利息
     * @return 结果
     */
    @Override
    public int insertFinanceInterestMember(FinanceInterestMember financeInterestMember)
    {
        return financeInterestMemberMapper.insertFinanceInterestMember(financeInterestMember);
    }

    /**
     * 修改量化理财-会员存款利息
     * 
     * @param financeInterestMember 量化理财-会员存款利息
     * @return 结果
     */
    @Override
    public int updateFinanceInterestMember(FinanceInterestMember financeInterestMember)
    {
        return financeInterestMemberMapper.updateFinanceInterestMember(financeInterestMember);
    }

    /**
     * 批量删除量化理财-会员存款利息
     * 
     * @param ids 需要删除的量化理财-会员存款利息主键
     * @return 结果
     */
    @Override
    public int deleteFinanceInterestMemberByIds(Long[] ids)
    {
        return financeInterestMemberMapper.deleteFinanceInterestMemberByIds(ids);
    }

    /**
     * 删除量化理财-会员存款利息信息
     * 
     * @param id 量化理财-会员存款利息主键
     * @return 结果
     */
    @Override
    public int deleteFinanceInterestMemberById(Long id)
    {
        return financeInterestMemberMapper.deleteFinanceInterestMemberById(id);
    }
}
