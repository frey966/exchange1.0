package com.financia.common.service.impl;

import java.util.List;

import com.financia.common.PContactUs;
import com.financia.common.core.utils.DateUtils;
import com.financia.common.mapper.PContactUsMapper;
import com.financia.common.service.IPContactUsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 个人中心-联系我们Service业务层处理
 * 
 * @author 花生
 * @date 2022-08-22
 */
@Service
public class PContactUsServiceImpl implements IPContactUsService
{
    @Autowired
    private PContactUsMapper pContactUsMapper;

    /**
     * 查询个人中心-联系我们
     * 
     * @param id 个人中心-联系我们主键
     * @return 个人中心-联系我们
     */
    @Override
    public PContactUs selectPContactUsById(Long id)
    {
        return pContactUsMapper.selectPContactUsById(id);
    }

    /**
     * 查询个人中心-联系我们列表
     * 
     * @param pContactUs 个人中心-联系我们
     * @return 个人中心-联系我们
     */
    @Override
    public List<PContactUs> selectPContactUsList(PContactUs pContactUs)
    {
        return pContactUsMapper.selectPContactUsList(pContactUs);
    }

    /**
     * 新增个人中心-联系我们
     * 
     * @param pContactUs 个人中心-联系我们
     * @return 结果
     */
    @Override
    public int insertPContactUs(PContactUs pContactUs)
    {
        return pContactUsMapper.insertPContactUs(pContactUs);
    }

    /**
     * 修改个人中心-联系我们
     * 
     * @param pContactUs 个人中心-联系我们
     * @return 结果
     */
    @Override
    public int updatePContactUs(PContactUs pContactUs)
    {
        return pContactUsMapper.updatePContactUs(pContactUs);
    }

    /**
     * 批量删除个人中心-联系我们
     * 
     * @param ids 需要删除的个人中心-联系我们主键
     * @return 结果
     */
    @Override
    public int deletePContactUsByIds(Long[] ids)
    {
        return pContactUsMapper.deletePContactUsByIds(ids);
    }

    /**
     * 删除个人中心-联系我们信息
     * 
     * @param id 个人中心-联系我们主键
     * @return 结果
     */
    @Override
    public int deletePContactUsById(Long id)
    {
        return pContactUsMapper.deletePContactUsById(id);
    }
}
