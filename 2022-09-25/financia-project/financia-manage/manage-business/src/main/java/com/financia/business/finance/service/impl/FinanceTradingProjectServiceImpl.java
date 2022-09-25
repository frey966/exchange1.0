package com.financia.business.finance.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.financia.business.finance.mapper.FinanceTradingProjectMapper;
import com.financia.business.finance.service.IFinanceTradingProjectService;
import com.financia.finance.FinanceTradingProject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 量化理财-理财产品信息Service业务层处理
 *
 * @author 花生
 * @date 2022-08-15
 */
@Service
public class FinanceTradingProjectServiceImpl extends ServiceImpl<FinanceTradingProjectMapper, FinanceTradingProject> implements IFinanceTradingProjectService
{
    @Autowired
    private FinanceTradingProjectMapper financeTradingProjectMapper;

    /**
     * 查询量化理财-理财产品信息
     *
     * @param financeId 量化理财-理财产品信息主键
     * @return 量化理财-理财产品信息
     */
    @Override
    public FinanceTradingProject selectFinanceTradingProjectByFinanceId(Long financeId)
    {
        return financeTradingProjectMapper.selectFinanceTradingProjectByFinanceId(financeId);
    }

    /**
     * 查询量化理财-理财产品信息列表
     *
     * @param financeTradingProject 量化理财-理财产品信息
     * @return 量化理财-理财产品信息
     */
    @Override
    public List<FinanceTradingProject> selectFinanceTradingProjectList(FinanceTradingProject financeTradingProject)
    {
        return financeTradingProjectMapper.selectFinanceTradingProjectList(financeTradingProject);
    }

    /**
     * 新增量化理财-理财产品信息
     *
     * @param financeTradingProject 量化理财-理财产品信息
     * @return 结果
     */
    @Override
    public int insertFinanceTradingProject(FinanceTradingProject financeTradingProject)
    {
        return financeTradingProjectMapper.insertFinanceTradingProject(financeTradingProject);
    }

    /**
     * 修改量化理财-理财产品信息
     *
     * @param financeTradingProject 量化理财-理财产品信息
     * @return 结果
     */
    @Override
    public int updateFinanceTradingProject(FinanceTradingProject financeTradingProject)
    {
//        financeTradingProject.setCreateTime(DateUtils.getNowDate().toString());
        return financeTradingProjectMapper.updateFinanceTradingProject(financeTradingProject);
    }

    /**
     * 批量删除量化理财-理财产品信息
     *
     * @param financeIds 需要删除的量化理财-理财产品信息主键
     * @return 结果
     */
    @Override
    public int deleteFinanceTradingProjectByFinanceIds(Long[] financeIds)
    {
        return financeTradingProjectMapper.deleteFinanceTradingProjectByFinanceIds(financeIds);
    }

    /**
     * 删除量化理财-理财产品信息信息
     *
     * @param financeId 量化理财-理财产品信息主键
     * @return 结果
     */
    @Override
    public int deleteFinanceTradingProjectByFinanceId(Long financeId)
    {
        return financeTradingProjectMapper.deleteFinanceTradingProjectByFinanceId(financeId);
    }
}
