package com.financia.business.contentmanagement.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.financia.business.contentmanagement.mapper.PCommonProblemMapper;
import com.financia.business.contentmanagement.service.IPCommonProblemService;
import com.financia.common.PCommonProblem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 帮助管理
类型1常见问题2新手指南3交易指南4币种质料5行情技术6条款协议7其他Service业务层处理
 * 
 * @author 花生
 * @date 2022-08-07
 */
@Service
public class PCommonProblemServiceImpl  extends ServiceImpl<PCommonProblemMapper, PCommonProblem> implements IPCommonProblemService
{
    @Autowired
    private PCommonProblemMapper pCommonProblemMapper;

    /**
     * 查询帮助管理
类型1常见问题2新手指南3交易指南4币种质料5行情技术6条款协议7其他
     * 
     * @param id 帮助管理
类型1常见问题2新手指南3交易指南4币种质料5行情技术6条款协议7其他主键
     * @return 帮助管理
类型1常见问题2新手指南3交易指南4币种质料5行情技术6条款协议7其他
     */
    @Override
    public PCommonProblem selectPCommonProblemById(Long id)
    {
        return pCommonProblemMapper.selectPCommonProblemById(id);
    }

    /**
     * 查询帮助管理
类型1常见问题2新手指南3交易指南4币种质料5行情技术6条款协议7其他列表
     * 
     * @param pCommonProblem 帮助管理
类型1常见问题2新手指南3交易指南4币种质料5行情技术6条款协议7其他
     * @return 帮助管理
类型1常见问题2新手指南3交易指南4币种质料5行情技术6条款协议7其他
     */
    @Override
    public List<PCommonProblem> selectPCommonProblemList(PCommonProblem pCommonProblem)
    {
        return pCommonProblemMapper.selectPCommonProblemList(pCommonProblem);
    }

    /**
     * 新增帮助管理
类型1常见问题2新手指南3交易指南4币种质料5行情技术6条款协议7其他
     * 
     * @param pCommonProblem 帮助管理
类型1常见问题2新手指南3交易指南4币种质料5行情技术6条款协议7其他
     * @return 结果
     */
    @Override
    public int insertPCommonProblem(PCommonProblem pCommonProblem)
    {
        return pCommonProblemMapper.insertPCommonProblem(pCommonProblem);
    }

    /**
     * 修改帮助管理
类型1常见问题2新手指南3交易指南4币种质料5行情技术6条款协议7其他
     * 
     * @param pCommonProblem 帮助管理
类型1常见问题2新手指南3交易指南4币种质料5行情技术6条款协议7其他
     * @return 结果
     */
    @Override
    public int updatePCommonProblem(PCommonProblem pCommonProblem)
    {
        return pCommonProblemMapper.updatePCommonProblem(pCommonProblem);
    }

    /**
     * 批量删除帮助管理
类型1常见问题2新手指南3交易指南4币种质料5行情技术6条款协议7其他
     * 
     * @param ids 需要删除的帮助管理
类型1常见问题2新手指南3交易指南4币种质料5行情技术6条款协议7其他主键
     * @return 结果
     */
    @Override
    public int deletePCommonProblemByIds(Long[] ids)
    {
        return pCommonProblemMapper.deletePCommonProblemByIds(ids);
    }

    /**
     * 删除帮助管理
类型1常见问题2新手指南3交易指南4币种质料5行情技术6条款协议7其他信息
     * 
     * @param id 帮助管理
类型1常见问题2新手指南3交易指南4币种质料5行情技术6条款协议7其他主键
     * @return 结果
     */
    @Override
    public int deletePCommonProblemById(Long id)
    {
        return pCommonProblemMapper.deletePCommonProblemById(id);
    }
}
