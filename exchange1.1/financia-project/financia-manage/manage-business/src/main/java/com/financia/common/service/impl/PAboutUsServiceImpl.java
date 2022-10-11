package com.financia.common.service.impl;

import java.util.List;

import com.financia.common.AboutUs;
import com.financia.common.core.utils.DateUtils;
import com.financia.common.mapper.PAboutUsMapper;
import com.financia.common.service.IPAboutUsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 个人中心-关于我们Service业务层处理
 * 
 * @author 花生
 * @date 2022-08-22
 */
@Service
public class PAboutUsServiceImpl implements IPAboutUsService
{
    @Autowired
    private PAboutUsMapper pAboutUsMapper;

    /**
     * 查询个人中心-关于我们
     * 
     * @param id 个人中心-关于我们主键
     * @return 个人中心-关于我们
     */
    @Override
    public AboutUs selectPAboutUsById(Long id)
    {
        return pAboutUsMapper.selectPAboutUsById(id);
    }

    /**
     * 查询个人中心-关于我们列表
     * 
     * @param pAboutUs 个人中心-关于我们
     * @return 个人中心-关于我们
     */
    @Override
    public List<AboutUs> selectPAboutUsList(AboutUs pAboutUs)
    {
        return pAboutUsMapper.selectPAboutUsList(pAboutUs);
    }

    /**
     * 新增个人中心-关于我们
     * 
     * @param pAboutUs 个人中心-关于我们
     * @return 结果
     */
    @Override
    public int insertPAboutUs(AboutUs pAboutUs)
    {
        return pAboutUsMapper.insertPAboutUs(pAboutUs);
    }

    /**
     * 修改个人中心-关于我们
     * 
     * @param pAboutUs 个人中心-关于我们
     * @return 结果
     */
    @Override
    public int updatePAboutUs(AboutUs pAboutUs)
    {
        return pAboutUsMapper.updatePAboutUs(pAboutUs);
    }

    /**
     * 批量删除个人中心-关于我们
     * 
     * @param ids 需要删除的个人中心-关于我们主键
     * @return 结果
     */
    @Override
    public int deletePAboutUsByIds(Long[] ids)
    {
        return pAboutUsMapper.deletePAboutUsByIds(ids);
    }

    /**
     * 删除个人中心-关于我们信息
     * 
     * @param id 个人中心-关于我们主键
     * @return 结果
     */
    @Override
    public int deletePAboutUsById(Long id)
    {
        return pAboutUsMapper.deletePAboutUsById(id);
    }
}
