package com.financia.common.mapper;

import com.financia.common.PContactUs;

import java.util.List;

/**
 * 个人中心-联系我们Mapper接口
 * 
 * @author 花生
 * @date 2022-08-22
 */
public interface PContactUsMapper 
{
    /**
     * 查询个人中心-联系我们
     * 
     * @param id 个人中心-联系我们主键
     * @return 个人中心-联系我们
     */
    public PContactUs selectPContactUsById(Long id);

    /**
     * 查询个人中心-联系我们列表
     * 
     * @param pContactUs 个人中心-联系我们
     * @return 个人中心-联系我们集合
     */
    public List<PContactUs> selectPContactUsList(PContactUs pContactUs);

    /**
     * 新增个人中心-联系我们
     * 
     * @param pContactUs 个人中心-联系我们
     * @return 结果
     */
    public int insertPContactUs(PContactUs pContactUs);

    /**
     * 修改个人中心-联系我们
     * 
     * @param pContactUs 个人中心-联系我们
     * @return 结果
     */
    public int updatePContactUs(PContactUs pContactUs);

    /**
     * 删除个人中心-联系我们
     * 
     * @param id 个人中心-联系我们主键
     * @return 结果
     */
    public int deletePContactUsById(Long id);

    /**
     * 批量删除个人中心-联系我们
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePContactUsByIds(Long[] ids);
}
