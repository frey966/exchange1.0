package com.financia.common.service.impl;

import java.util.List;

import com.financia.common.PPrivacyPolicy;
import com.financia.common.core.utils.DateUtils;
import com.financia.common.mapper.PPrivacyPolicyMapper;
import com.financia.common.service.IPPrivacyPolicyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 隐私政策Service业务层处理
 * 
 * @author 花生
 * @date 2022-08-22
 */
@Service
public class PPrivacyPolicyServiceImpl implements IPPrivacyPolicyService
{
    @Autowired
    private PPrivacyPolicyMapper pPrivacyPolicyMapper;

    /**
     * 查询隐私政策
     * 
     * @param id 隐私政策主键
     * @return 隐私政策
     */
    @Override
    public PPrivacyPolicy selectPPrivacyPolicyById(Long id)
    {
        return pPrivacyPolicyMapper.selectPPrivacyPolicyById(id);
    }

    /**
     * 查询隐私政策列表
     * 
     * @param pPrivacyPolicy 隐私政策
     * @return 隐私政策
     */
    @Override
    public List<PPrivacyPolicy> selectPPrivacyPolicyList(PPrivacyPolicy pPrivacyPolicy)
    {
        return pPrivacyPolicyMapper.selectPPrivacyPolicyList(pPrivacyPolicy);
    }

    /**
     * 新增隐私政策
     * 
     * @param pPrivacyPolicy 隐私政策
     * @return 结果
     */
    @Override
    public int insertPPrivacyPolicy(PPrivacyPolicy pPrivacyPolicy)
    {
        return pPrivacyPolicyMapper.insertPPrivacyPolicy(pPrivacyPolicy);
    }

    /**
     * 修改隐私政策
     * 
     * @param pPrivacyPolicy 隐私政策
     * @return 结果
     */
    @Override
    public int updatePPrivacyPolicy(PPrivacyPolicy pPrivacyPolicy)
    {
        return pPrivacyPolicyMapper.updatePPrivacyPolicy(pPrivacyPolicy);
    }

    /**
     * 批量删除隐私政策
     * 
     * @param ids 需要删除的隐私政策主键
     * @return 结果
     */
    @Override
    public int deletePPrivacyPolicyByIds(Long[] ids)
    {
        return pPrivacyPolicyMapper.deletePPrivacyPolicyByIds(ids);
    }

    /**
     * 删除隐私政策信息
     * 
     * @param id 隐私政策主键
     * @return 结果
     */
    @Override
    public int deletePPrivacyPolicyById(Long id)
    {
        return pPrivacyPolicyMapper.deletePPrivacyPolicyById(id);
    }
}
