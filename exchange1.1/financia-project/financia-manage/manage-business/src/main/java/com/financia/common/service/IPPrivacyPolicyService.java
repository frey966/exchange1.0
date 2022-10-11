package com.financia.common.service;

import com.financia.common.PPrivacyPolicy;

import java.util.List;

/**
 * 隐私政策Service接口
 * 
 * @author 花生
 * @date 2022-08-22
 */
public interface IPPrivacyPolicyService 
{
    /**
     * 查询隐私政策
     * 
     * @param id 隐私政策主键
     * @return 隐私政策
     */
    public PPrivacyPolicy selectPPrivacyPolicyById(Long id);

    /**
     * 查询隐私政策列表
     * 
     * @param pPrivacyPolicy 隐私政策
     * @return 隐私政策集合
     */
    public List<PPrivacyPolicy> selectPPrivacyPolicyList(PPrivacyPolicy pPrivacyPolicy);

    /**
     * 新增隐私政策
     * 
     * @param pPrivacyPolicy 隐私政策
     * @return 结果
     */
    public int insertPPrivacyPolicy(PPrivacyPolicy pPrivacyPolicy);

    /**
     * 修改隐私政策
     * 
     * @param pPrivacyPolicy 隐私政策
     * @return 结果
     */
    public int updatePPrivacyPolicy(PPrivacyPolicy pPrivacyPolicy);

    /**
     * 批量删除隐私政策
     * 
     * @param ids 需要删除的隐私政策主键集合
     * @return 结果
     */
    public int deletePPrivacyPolicyByIds(Long[] ids);

    /**
     * 删除隐私政策信息
     * 
     * @param id 隐私政策主键
     * @return 结果
     */
    public int deletePPrivacyPolicyById(Long id);
}
