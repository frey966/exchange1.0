package com.financia.common.mapper;

import com.financia.common.PCountryCode;

import java.util.List;

/**
 * 国家区号Mapper接口
 * 
 * @author 花生
 * @date 2022-08-05
 */
public interface PCountryCodeMapper 
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
     * 删除国家区号
     * 
     * @param id 国家区号主键
     * @return 结果
     */
    public int deletePCountryCodeById(Long id);

    /**
     * 批量删除国家区号
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePCountryCodeByIds(Long[] ids);
}
