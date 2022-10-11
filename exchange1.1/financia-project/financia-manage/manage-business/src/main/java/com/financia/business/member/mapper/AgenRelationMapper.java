package com.financia.business.member.mapper;

import com.financia.exchange.AgenRelation;

import java.util.List;

/**
 * 代理和会员关系
系统用户和代理关系Mapper接口
 * 
 * @author 花生
 * @date 2022-08-04
 */
public interface AgenRelationMapper 
{
    /**
     * 查询代理和会员关系
系统用户和代理关系
     * 
     * @param id 代理和会员关系
系统用户和代理关系主键
     * @return 代理和会员关系
系统用户和代理关系
     */
    public AgenRelation selectAgenRelationById(Long id);

    /**
     * 查询代理和会员关系
系统用户和代理关系列表
     * 
     * @param agenRelation 代理和会员关系
系统用户和代理关系
     * @return 代理和会员关系
系统用户和代理关系集合
     */
    public List<AgenRelation> selectAgenRelationList(AgenRelation agenRelation);

    /**
     * 新增代理和会员关系
系统用户和代理关系
     * 
     * @param agenRelation 代理和会员关系
系统用户和代理关系
     * @return 结果
     */
    public int insertAgenRelation(AgenRelation agenRelation);

    /**
     * 修改代理和会员关系
系统用户和代理关系
     * 
     * @param agenRelation 代理和会员关系
系统用户和代理关系
     * @return 结果
     */
    public int updateAgenRelation(AgenRelation agenRelation);

    /**
     * 删除代理和会员关系
系统用户和代理关系
     * 
     * @param id 代理和会员关系
系统用户和代理关系主键
     * @return 结果
     */
    public int deleteAgenRelationById(Long id);

    /**
     * 批量删除代理和会员关系
系统用户和代理关系
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteAgenRelationByIds(Long[] ids);
}
