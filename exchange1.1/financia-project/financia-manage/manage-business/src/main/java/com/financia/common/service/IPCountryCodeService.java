package com.financia.common.service;

import com.financia.common.PCountryCode;

import java.util.List;

/**
 * 国家区号Service接口
 * 
 * @author 花生
 * @date 2022-08-05
 */
public interface IPCountryCodeService 
{
    /**
     * 查询国家区号
     * 
     * @param id 国家区号主键
     * @return 国家区号
     */
    public PCountryCode selectPCountryCodeById(Long id);

    /**
     * 查询国家区号列表
     * 
     * @param pCountryCode 国家区号
     * @return 国家区号集合
     */
    public List<PCountryCode> selectPCountryCodeList(PCountryCode pCountryCode);

    /**
     * 新增国家区号
     * 
     * @param pCountryCode 国家区号
     * @return 结果
     */
    public int insertPCountryCode(PCountryCode pCountryCode);

    /**
     * 修改国家区号
     * 
     * @param pCountryCode 国家区号
     * @return 结果
     */
    public int updatePCountryCode(PCountryCode pCountryCode);

    /**
     * 批量删除国家区号
     * 
     * @param ids 需要删除的国家区号主键集合
     * @return 结果
     */
    public int deletePCountryCodeByIds(Long[] ids);

    /**
     * 删除国家区号信息
     * 
     * @param id 国家区号主键
     * @return 结果
     */
    public int deletePCountryCodeById(Long id);
}
