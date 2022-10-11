package com.financia.business.contentmanagement.service.impl;

import java.util.List;

import com.financia.business.contentmanagement.mapper.PShareInfoMapper;
import com.financia.business.contentmanagement.service.IPShareInfoService;
import com.financia.common.MemberShare;
import com.financia.common.core.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 个人中心-分享数据Service业务层处理
 * 
 * @author 花生
 * @date 2022-08-23
 */
@Service
public class PShareInfoServiceImpl implements IPShareInfoService
{
    @Autowired
    private PShareInfoMapper pShareInfoMapper;

    /**
     * 查询个人中心-分享数据
     * 
     * @param shareId 个人中心-分享数据主键
     * @return 个人中心-分享数据
     */
    @Override
    public MemberShare selectPShareInfoByShareId(Long shareId)
    {
        return pShareInfoMapper.selectPShareInfoByShareId(shareId);
    }

    /**
     * 查询个人中心-分享数据列表
     * 
     * @param pShareInfo 个人中心-分享数据
     * @return 个人中心-分享数据
     */
    @Override
    public List<MemberShare> selectPShareInfoList(MemberShare pShareInfo)
    {
        return pShareInfoMapper.selectPShareInfoList(pShareInfo);
    }

    /**
     * 新增个人中心-分享数据
     * 
     * @param pShareInfo 个人中心-分享数据
     * @return 结果
     */
    @Override
    public int insertPShareInfo(MemberShare pShareInfo)
    {
        return pShareInfoMapper.insertPShareInfo(pShareInfo);
    }

    /**
     * 修改个人中心-分享数据
     * 
     * @param pShareInfo 个人中心-分享数据
     * @return 结果
     */
    @Override
    public int updatePShareInfo(MemberShare pShareInfo)
    {
        return pShareInfoMapper.updatePShareInfo(pShareInfo);
    }

    /**
     * 批量删除个人中心-分享数据
     * 
     * @param shareIds 需要删除的个人中心-分享数据主键
     * @return 结果
     */
    @Override
    public int deletePShareInfoByShareIds(Long[] shareIds)
    {
        return pShareInfoMapper.deletePShareInfoByShareIds(shareIds);
    }

    /**
     * 删除个人中心-分享数据信息
     * 
     * @param shareId 个人中心-分享数据主键
     * @return 结果
     */
    @Override
    public int deletePShareInfoByShareId(Long shareId)
    {
        return pShareInfoMapper.deletePShareInfoByShareId(shareId);
    }
}
