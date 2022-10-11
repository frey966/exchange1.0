package com.financia.business.finance.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financia.finance.FinanceTradingProject;

import java.util.List;

/**
 * 量化理财-理财产品信息Mapper接口
 *
 * @author 花生
 * @date 2022-08-15
 */
public interface FinanceTradingProjectMapper extends BaseMapper<FinanceTradingProject>
{
    /**
     * 查询量化理财-理财产品信息
     *
     * @param financeId 量化理财-理财产品信息主键
     * @return 量化理财-理财产品信息
     */
    public FinanceTradingProject selectFinanceTradingProjectByFinanceId(Long financeId);

    /**
     * 查询量化理财-理财产品信息列表
     *
     * @param financeTradingProject 量化理财-理财产品信息
     * @return 量化理财-理财产品信息集合
     */
    public List<FinanceTradingProject> selectFinanceTradingProjectList(FinanceTradingProject financeTradingProject);

    /**
     * 新增量化理财-理财产品信息
     *
     * @param financeTradingProject 量化理财-理财产品信息
     * @return 结果
     */
    public int insertFinanceTradingProject(FinanceTradingProject financeTradingProject);

    /**
     * 修改量化理财-理财产品信息
     *
     * @param financeTradingProject 量化理财-理财产品信息
     * @return 结果
     */
    public int updateFinanceTradingProject(FinanceTradingProject financeTradingProject);

    /**
     * 删除量化理财-理财产品信息
     *
     * @param financeId 量化理财-理财产品信息主键
     * @return 结果
     */
    public int deleteFinanceTradingProjectByFinanceId(Long financeId);

    /**
     * 批量删除量化理财-理财产品信息
     *
     * @param financeIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFinanceTradingProjectByFinanceIds(Long[] financeIds);
}
