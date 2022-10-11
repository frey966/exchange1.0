package com.financia.common.service;

import com.financia.common.AboutUs;

import java.util.List;
/**
 * 个人中心-关于我们Service接口
 * 
 * @author 花生
 * @date 2022-08-22
 */
public interface IPAboutUsService 
{
    /**
     * 查询个人中心-关于我们
     * 
     * @param id 个人中心-关于我们主键
     * @return 个人中心-关于我们
     */
    public AboutUs selectPAboutUsById(Long id);

    /**
     * 查询个人中心-关于我们列表
     * 
     * @param pAboutUs 个人中心-关于我们
     * @return 个人中心-关于我们集合
     */
    public List<AboutUs> selectPAboutUsList(AboutUs pAboutUs);

    /**
     * 新增个人中心-关于我们
     * 
     * @param pAboutUs 个人中心-关于我们
     * @return 结果
     */
    public int insertPAboutUs(AboutUs pAboutUs);

    /**
     * 修改个人中心-关于我们
     * 
     * @param pAboutUs 个人中心-关于我们
     * @return 结果
     */
    public int updatePAboutUs(AboutUs pAboutUs);

    /**
     * 批量删除个人中心-关于我们
     * 
     * @param ids 需要删除的个人中心-关于我们主键集合
     * @return 结果
     */
    public int deletePAboutUsByIds(Long[] ids);

    /**
     * 删除个人中心-关于我们信息
     * 
     * @param id 个人中心-关于我们主键
     * @return 结果
     */
    public int deletePAboutUsById(Long id);
}
