package com.financia.business.contentmanagement.service;

import com.financia.common.MemberShare;

import java.util.List;

/**
 * 个人中心-分享数据Service接口
 * 
 * @author 花生
 * @date 2022-08-23
 */
public interface IPShareInfoService 
{
    /**
     * 查询个人中心-分享数据
     * 
     * @param shareId 个人中心-分享数据主键
     * @return 个人中心-分享数据
     */
    public MemberShare selectPShareInfoByShareId(Long shareId);

    /**
     * 查询个人中心-分享数据列表
     * 
     * @param pShareInfo 个人中心-分享数据
     * @return 个人中心-分享数据集合
     */
    public List<MemberShare> selectPShareInfoList(MemberShare pShareInfo);

    /**
     * 新增个人中心-分享数据
     * 
     * @param pShareInfo 个人中心-分享数据
     * @return 结果
     */
    public int insertPShareInfo(MemberShare pShareInfo);

    /**
     * 修改个人中心-分享数据
     * 
     * @param pShareInfo 个人中心-分享数据
     * @return 结果
     */
    public int updatePShareInfo(MemberShare pShareInfo);

    /**
     * 批量删除个人中心-分享数据
     * 
     * @param shareIds 需要删除的个人中心-分享数据主键集合
     * @return 结果
     */
    public int deletePShareInfoByShareIds(Long[] shareIds);

    /**
     * 删除个人中心-分享数据信息
     * 
     * @param shareId 个人中心-分享数据主键
     * @return 结果
     */
    public int deletePShareInfoByShareId(Long shareId);
}
