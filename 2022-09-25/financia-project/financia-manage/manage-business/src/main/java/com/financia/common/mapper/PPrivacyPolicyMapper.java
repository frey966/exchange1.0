package com.financia.common.mapper;

import com.financia.common.PPrivacyPolicy;

import java.util.List;

/**
 * 隐私政策Mapper接口
 * 
 * @author 花生
 * @date 2022-08-22
 */
public interface PPrivacyPolicyMapper 
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
     * 删除隐私政策
     * 
     * @param id 隐私政策主键
     * @return 结果
     */
    public int deletePPrivacyPolicyById(Long id);

    /**
     * 批量删除隐私政策
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePPrivacyPolicyByIds(Long[] ids);
}
