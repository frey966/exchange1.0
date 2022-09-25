package com.financia.business.dataconfiguration.service.impl;

import java.util.List;

import com.financia.business.PBusinessType;
import com.financia.business.dataconfiguration.mapper.PBusinessTypeMapper;
import com.financia.business.dataconfiguration.service.IPBusinessTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 业务类型Service业务层处理
 * 
 * @author 花生
 * @date 2022-08-02
 */
@Service
public class PBusinessTypeServiceImpl implements IPBusinessTypeService
{
    @Autowired
    private PBusinessTypeMapper pBusinessTypeMapper;

    /**
     * 查询业务类型
     * 
     * @param id 业务类型主键
     * @return 业务类型
     */
    @Override
    public PBusinessType selectPBusinessTypeById(Long id)
    {
        return pBusinessTypeMapper.selectPBusinessTypeById(id);
    }

    /**
     * 查询业务类型列表
     * 
     * @param pBusinessType 业务类型
     * @return 业务类型
     */
    @Override
    public List<PBusinessType> selectPBusinessTypeList(PBusinessType pBusinessType)
    {
        return pBusinessTypeMapper.selectPBusinessTypeList(pBusinessType);
    }

    /**
     * 新增业务类型
     * 
     * @param pBusinessType 业务类型
     * @return 结果
     */
    @Override
    public int insertPBusinessType(PBusinessType pBusinessType)
    {
//        pBusinessType.setCreateTime(DateUtils.getNowDate());
        return pBusinessTypeMapper.insertPBusinessType(pBusinessType);
    }

    /**
     * 修改业务类型
     * 
     * @param pBusinessType 业务类型
     * @return 结果
     */
    @Override
    public int updatePBusinessType(PBusinessType pBusinessType)
    {
        return pBusinessTypeMapper.updatePBusinessType(pBusinessType);
    }

    /**
     * 批量删除业务类型
     * 
     * @param ids 需要删除的业务类型主键
     * @return 结果
     */
    @Override
    public int deletePBusinessTypeByIds(Long[] ids)
    {
        return pBusinessTypeMapper.deletePBusinessTypeByIds(ids);
    }

    /**
     * 删除业务类型信息
     * 
     * @param id 业务类型主键
     * @return 结果
     */
    @Override
    public int deletePBusinessTypeById(Long id)
    {
        return pBusinessTypeMapper.deletePBusinessTypeById(id);
    }
}
