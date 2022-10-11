package com.financia.business.member.service.impl;

import java.util.List;

import com.financia.business.member.mapper.AgenRelationMapper;
import com.financia.business.member.service.IAgenRelationService;
import com.financia.common.core.utils.DateUtils;
import com.financia.exchange.AgenRelation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 代理和会员关系
系统用户和代理关系Service业务层处理
 * 
 * @author 花生
 * @date 2022-08-04
 */
@Service
public class AgenRelationServiceImpl implements IAgenRelationService
{
    @Autowired
    private AgenRelationMapper agenRelationMapper;

    /**
     * 查询代理和会员关系
系统用户和代理关系
     * 
     * @param id 代理和会员关系
系统用户和代理关系主键
     * @return 代理和会员关系
系统用户和代理关系
     */
    @Override
    public AgenRelation selectAgenRelationById(Long id)
    {
        return agenRelationMapper.selectAgenRelationById(id);
    }

    /**
     * 查询代理和会员关系系统用户和代理关系列表
     * 
     * @param agenRelation 代理和会员关系系统用户和代理关系
     * @return 代理和会员关系系统用户和代理关系
     */
    @Override
    public List<AgenRelation> selectAgenRelationList(AgenRelation agenRelation)
    {
        return agenRelationMapper.selectAgenRelationList(agenRelation);
    }

    /**
     * 新增代理和会员关系
系统用户和代理关系
     * 
     * @param agenRelation 代理和会员关系
系统用户和代理关系
     * @return 结果
     */
    @Override
    public int insertAgenRelation(AgenRelation agenRelation)
    {
        agenRelation.setCreateTime(DateUtils.getNowDate());
        return agenRelationMapper.insertAgenRelation(agenRelation);
    }

    /**
     * 修改代理和会员关系
系统用户和代理关系
     * 
     * @param agenRelation 代理和会员关系
系统用户和代理关系
     * @return 结果
     */
    @Override
    public int updateAgenRelation(AgenRelation agenRelation)
    {
        return agenRelationMapper.updateAgenRelation(agenRelation);
    }

    /**
     * 批量删除代理和会员关系
系统用户和代理关系
     * 
     * @param ids 需要删除的代理和会员关系
系统用户和代理关系主键
     * @return 结果
     */
    @Override
    public int deleteAgenRelationByIds(Long[] ids)
    {
        return agenRelationMapper.deleteAgenRelationByIds(ids);
    }

    /**
     * 删除代理和会员关系
系统用户和代理关系信息
     * 
     * @param id 代理和会员关系
系统用户和代理关系主键
     * @return 结果
     */
    @Override
    public int deleteAgenRelationById(Long id)
    {
        return agenRelationMapper.deleteAgenRelationById(id);
    }
}
