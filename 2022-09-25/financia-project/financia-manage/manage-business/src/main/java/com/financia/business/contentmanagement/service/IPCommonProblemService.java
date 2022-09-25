package com.financia.business.contentmanagement.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.financia.common.PCommonProblem;

import java.util.List;

/**
 * 帮助管理
类型1常见问题2新手指南3交易指南4币种质料5行情技术6条款协议7其他Service接口
 * 
 * @author 花生
 * @date 2022-08-07
 */
public interface IPCommonProblemService  extends IService<PCommonProblem>
{
    /**
     * 查询帮助管理
类型1常见问题2新手指南3交易指南4币种质料5行情技术6条款协议7其他
     * 
     * @param id 帮助管理
类型1常见问题2新手指南3交易指南4币种质料5行情技术6条款协议7其他主键
     * @return 帮助管理
类型1常见问题2新手指南3交易指南4币种质料5行情技术6条款协议7其他
     */
     PCommonProblem selectPCommonProblemById(Long id);

    /**
     * 查询帮助管理
类型1常见问题2新手指南3交易指南4币种质料5行情技术6条款协议7其他列表
     * 
     * @param pCommonProblem 帮助管理
类型1常见问题2新手指南3交易指南4币种质料5行情技术6条款协议7其他
     * @return 帮助管理
类型1常见问题2新手指南3交易指南4币种质料5行情技术6条款协议7其他集合
     */
     List<PCommonProblem> selectPCommonProblemList(PCommonProblem pCommonProblem);

    /**
     * 新增帮助管理
类型1常见问题2新手指南3交易指南4币种质料5行情技术6条款协议7其他
     * 
     * @param pCommonProblem 帮助管理
类型1常见问题2新手指南3交易指南4币种质料5行情技术6条款协议7其他
     * @return 结果
     */
     int insertPCommonProblem(PCommonProblem pCommonProblem);

    /**
     * 修改帮助管理
类型1常见问题2新手指南3交易指南4币种质料5行情技术6条款协议7其他
     * 
     * @param pCommonProblem 帮助管理
类型1常见问题2新手指南3交易指南4币种质料5行情技术6条款协议7其他
     * @return 结果
     */
     int updatePCommonProblem(PCommonProblem pCommonProblem);

    /**
     * 批量删除帮助管理
类型1常见问题2新手指南3交易指南4币种质料5行情技术6条款协议7其他
     * 
     * @param ids 需要删除的帮助管理
类型1常见问题2新手指南3交易指南4币种质料5行情技术6条款协议7其他主键集合
     * @return 结果
     */
     int deletePCommonProblemByIds(Long[] ids);

    /**
     * 删除帮助管理
类型1常见问题2新手指南3交易指南4币种质料5行情技术6条款协议7其他信息
     * 
     * @param id 帮助管理
类型1常见问题2新手指南3交易指南4币种质料5行情技术6条款协议7其他主键
     * @return 结果
     */
     int deletePCommonProblemById(Long id);
}
