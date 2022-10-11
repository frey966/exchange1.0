package com.financia.business.contentmanagement.service.impl;

import java.util.List;

import com.financia.business.contentmanagement.mapper.PNoticeMapper;
import com.financia.business.contentmanagement.service.IPNoticeService;
import com.financia.exchange.PNotice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 公告管理Service业务层处理
 * 
 * @author 花生
 * @date 2022-08-16
 */
@Service
public class PNoticeServiceImpl implements IPNoticeService
{
    @Autowired
    private PNoticeMapper pNoticeMapper;

    /**
     * 查询公告管理
     * 
     * @param id 公告管理主键
     * @return 公告管理
     */
    @Override
    public PNotice selectPNoticeById(Long id)
    {
        return pNoticeMapper.selectPNoticeById(id);
    }

    /**
     * 查询公告管理列表
     * 
     * @param pNotice 公告管理
     * @return 公告管理
     */
    @Override
    public List<PNotice> selectPNoticeList(PNotice pNotice)
    {
        return pNoticeMapper.selectPNoticeList(pNotice);
    }

    /**
     * 新增公告管理
     * 
     * @param pNotice 公告管理
     * @return 结果
     */
    @Override
    public int insertPNotice(PNotice pNotice)
    {
        return pNoticeMapper.insertPNotice(pNotice);
    }

    /**
     * 修改公告管理
     * 
     * @param pNotice 公告管理
     * @return 结果
     */
    @Override
    public int updatePNotice(PNotice pNotice)
    {
        return pNoticeMapper.updatePNotice(pNotice);
    }

    /**
     * 批量删除公告管理
     * 
     * @param ids 需要删除的公告管理主键
     * @return 结果
     */
    @Override
    public int deletePNoticeByIds(Long[] ids)
    {
        return pNoticeMapper.deletePNoticeByIds(ids);
    }

    /**
     * 删除公告管理信息
     * 
     * @param id 公告管理主键
     * @return 结果
     */
    @Override
    public int deletePNoticeById(Long id)
    {
        return pNoticeMapper.deletePNoticeById(id);
    }
}
