package com.financia.business.contentmanagement.service;

import com.financia.common.PCompliance;

import java.util.List;

/**
 * 内容管理_合规经营Service接口
 * 
 * @author ruoyi
 * @date 2022-08-23
 */
public interface IPComplianceService 
{
    /**
     * 查询内容管理_合规经营
     * 
     * @param id 内容管理_合规经营主键
     * @return 内容管理_合规经营
     */
    public PCompliance selectPComplianceById(Long id);

    /**
     * 查询内容管理_合规经营列表
     * 
     * @param pCompliance 内容管理_合规经营
     * @return 内容管理_合规经营集合
     */
    public List<PCompliance> selectPComplianceList(PCompliance pCompliance);

    /**
     * 新增内容管理_合规经营
     * 
     * @param pCompliance 内容管理_合规经营
     * @return 结果
     */
    public int insertPCompliance(PCompliance pCompliance);

    /**
     * 修改内容管理_合规经营
     * 
     * @param pCompliance 内容管理_合规经营
     * @return 结果
     */
    public int updatePCompliance(PCompliance pCompliance);

    /**
     * 批量删除内容管理_合规经营
     * 
     * @param ids 需要删除的内容管理_合规经营主键集合
     * @return 结果
     */
    public int deletePComplianceByIds(Long[] ids);

    /**
     * 删除内容管理_合规经营信息
     * 
     * @param id 内容管理_合规经营主键
     * @return 结果
     */
    public int deletePComplianceById(Long id);
}
