package com.financia.business.finance.mapper;

import com.financia.finance.FinanceInterestMember;

import java.util.List;

/**
 * 量化理财-会员存款利息Mapper接口
 * 
 * @author 花生
 * @date 2022-08-15
 */
public interface FinanceInterestMemberMapper 
{
    /**
     * 查询量化理财-会员存款利息
     * 
     * @param id 量化理财-会员存款利息主键
     * @return 量化理财-会员存款利息
     */
    public FinanceInterestMember selectFinanceInterestMemberById(Long id);

    /**
     * 查询量化理财-会员存款利息列表
     * 
     * @param financeInterestMember 量化理财-会员存款利息
     * @return 量化理财-会员存款利息集合
     */
    public List<FinanceInterestMember> selectFinanceInterestMemberList(FinanceInterestMember financeInterestMember);

    /**
     * 新增量化理财-会员存款利息
     * 
     * @param financeInterestMember 量化理财-会员存款利息
     * @return 结果
     */
    public int insertFinanceInterestMember(FinanceInterestMember financeInterestMember);

    /**
     * 修改量化理财-会员存款利息
     * 
     * @param financeInterestMember 量化理财-会员存款利息
     * @return 结果
     */
    public int updateFinanceInterestMember(FinanceInterestMember financeInterestMember);

    /**
     * 删除量化理财-会员存款利息
     * 
     * @param id 量化理财-会员存款利息主键
     * @return 结果
     */
    public int deleteFinanceInterestMemberById(Long id);

    /**
     * 批量删除量化理财-会员存款利息
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFinanceInterestMemberByIds(Long[] ids);
}
