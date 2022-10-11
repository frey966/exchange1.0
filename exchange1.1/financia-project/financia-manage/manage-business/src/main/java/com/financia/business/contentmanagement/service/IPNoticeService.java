package com.financia.business.contentmanagement.service;

import com.financia.exchange.PNotice;

import java.util.List;

/**
 * 公告管理Service接口
 * 
 * @author 花生
 * @date 2022-08-16
 */
public interface IPNoticeService 
{
    /**
     * 查询公告管理
     * 
     * @param id 公告管理主键
     * @return 公告管理
     */
    public PNotice selectPNoticeById(Long id);

    /**
     * 查询公告管理列表
     * 
     * @param pNotice 公告管理
     * @return 公告管理集合
     */
    public List<PNotice> selectPNoticeList(PNotice pNotice);

    /**
     * 新增公告管理
     * 
     * @param pNotice 公告管理
     * @return 结果
     */
    public int insertPNotice(PNotice pNotice);

    /**
     * 修改公告管理
     * 
     * @param pNotice 公告管理
     * @return 结果
     */
    public int updatePNotice(PNotice pNotice);

    /**
     * 批量删除公告管理
     * 
     * @param ids 需要删除的公告管理主键集合
     * @return 结果
     */
    public int deletePNoticeByIds(Long[] ids);

    /**
     * 删除公告管理信息
     * 
     * @param id 公告管理主键
     * @return 结果
     */
    public int deletePNoticeById(Long id);
}
