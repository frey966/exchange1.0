package com.financia.business.contentmanagement.service.impl;

import java.util.List;

import com.financia.business.contentmanagement.mapper.PComplianceMapper;
import com.financia.business.contentmanagement.service.IPComplianceService;
import com.financia.common.PCompliance;
import com.financia.common.core.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 内容管理_合规经营Service业务层处理
 * 
 * @author ruoyi
 * @date 2022-08-23
 */
@Service
public class PComplianceServiceImpl implements IPComplianceService
{
    @Autowired
    private PComplianceMapper pComplianceMapper;

    /**
     * 查询内容管理_合规经营
     * 
     * @param id 内容管理_合规经营主键
     * @return 内容管理_合规经营
     */
    @Override
    public PCompliance selectPComplianceById(Long id)
    {
        return pComplianceMapper.selectPComplianceById(id);
    }

    /**
     * 查询内容管理_合规经营列表
     * 
     * @param pCompliance 内容管理_合规经营
     * @return 内容管理_合规经营
     */
    @Override
    public List<PCompliance> selectPComplianceList(PCompliance pCompliance)
    {
        return pComplianceMapper.selectPComplianceList(pCompliance);
    }

    /**
     * 新增内容管理_合规经营
     * 
     * @param pCompliance 内容管理_合规经营
     * @return 结果
     */
    @Override
    public int insertPCompliance(PCompliance pCompliance)
    {
        return pComplianceMapper.insertPCompliance(pCompliance);
    }

    /**
     * 修改内容管理_合规经营
     * 
     * @param pCompliance 内容管理_合规经营
     * @return 结果
     */
    @Override
    public int updatePCompliance(PCompliance pCompliance)
    {
        return pComplianceMapper.updatePCompliance(pCompliance);
    }

    /**
     * 批量删除内容管理_合规经营
     * 
     * @param ids 需要删除的内容管理_合规经营主键
     * @return 结果
     */
    @Override
    public int deletePComplianceByIds(Long[] ids)
    {
        return pComplianceMapper.deletePComplianceByIds(ids);
    }

    /**
     * 删除内容管理_合规经营信息
     * 
     * @param id 内容管理_合规经营主键
     * @return 结果
     */
    @Override
    public int deletePComplianceById(Long id)
    {
        return pComplianceMapper.deletePComplianceById(id);
    }
}
