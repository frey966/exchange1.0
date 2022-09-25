package com.financia.common.service.impl;

import java.util.List;

import com.financia.common.PCountryCode;
import com.financia.common.mapper.PCountryCodeMapper;
import com.financia.common.service.IPCountryCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 国家区号Service业务层处理
 * 
 * @author 花生
 * @date 2022-08-05
 */
@Service
public class PCountryCodeServiceImpl implements IPCountryCodeService
{
    @Autowired
    private PCountryCodeMapper pCountryCodeMapper;

    /**
     * 查询国家区号
     * 
     * @param id 国家区号主键
     * @return 国家区号
     */
    @Override
    public PCountryCode selectPCountryCodeById(Long id)
    {
        return pCountryCodeMapper.selectPCountryCodeById(id);
    }

    /**
     * 查询国家区号列表
     * 
     * @param pCountryCode 国家区号
     * @return 国家区号
     */
    @Override
    public List<PCountryCode> selectPCountryCodeList(PCountryCode pCountryCode)
    {
        return pCountryCodeMapper.selectPCountryCodeList(pCountryCode);
    }

    /**
     * 新增国家区号
     * 
     * @param pCountryCode 国家区号
     * @return 结果
     */
    @Override
    public int insertPCountryCode(PCountryCode pCountryCode)
    {
        return pCountryCodeMapper.insertPCountryCode(pCountryCode);
    }

    /**
     * 修改国家区号
     * 
     * @param pCountryCode 国家区号
     * @return 结果
     */
    @Override
    public int updatePCountryCode(PCountryCode pCountryCode)
    {
        return pCountryCodeMapper.updatePCountryCode(pCountryCode);
    }

    /**
     * 批量删除国家区号
     * 
     * @param ids 需要删除的国家区号主键
     * @return 结果
     */
    @Override
    public int deletePCountryCodeByIds(Long[] ids)
    {
        return pCountryCodeMapper.deletePCountryCodeByIds(ids);
    }

    /**
     * 删除国家区号信息
     * 
     * @param id 国家区号主键
     * @return 结果
     */
    @Override
    public int deletePCountryCodeById(Long id)
    {
        return pCountryCodeMapper.deletePCountryCodeById(id);
    }
}
