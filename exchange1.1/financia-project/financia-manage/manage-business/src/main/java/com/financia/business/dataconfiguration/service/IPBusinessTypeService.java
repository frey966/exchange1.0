package com.financia.business.dataconfiguration.service;

import com.financia.business.PBusinessType;

import java.util.List;


/**
 * 业务类型Service接口
 * 
 * @author 花生
 * @date 2022-08-02
 */
public interface IPBusinessTypeService 
{
    /**
     * 查询业务类型
     * 
     * @param id 业务类型主键
     * @return 业务类型
     */
    public PBusinessType selectPBusinessTypeById(Long id);

    /**
     * 查询业务类型列表
     * 
     * @param pBusinessType 业务类型
     * @return 业务类型集合
     */
    public List<PBusinessType> selectPBusinessTypeList(PBusinessType pBusinessType);

    /**
     * 新增业务类型
     * 
     * @param pBusinessType 业务类型
     * @return 结果
     */
    public int insertPBusinessType(PBusinessType pBusinessType);

    /**
     * 修改业务类型
     * 
     * @param pBusinessType 业务类型
     * @return 结果
     */
    public int updatePBusinessType(PBusinessType pBusinessType);

    /**
     * 批量删除业务类型
     * 
     * @param ids 需要删除的业务类型主键集合
     * @return 结果
     */
    public int deletePBusinessTypeByIds(Long[] ids);

    /**
     * 删除业务类型信息
     * 
     * @param id 业务类型主键
     * @return 结果
     */
    public int deletePBusinessTypeById(Long id);
}
