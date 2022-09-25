package com.financia.business.contentmanagement.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.financia.business.contentmanagement.service.IPCommonProblemService;
import com.financia.common.PCommonProblem;
import com.financia.common.core.utils.StringUtils;
import com.financia.common.log.enums.BusinessType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.financia.common.log.annotation.Log;
import com.financia.common.core.web.controller.BaseController;
import com.financia.common.core.web.domain.AjaxResult;
import com.financia.common.core.web.page.TableDataInfo;

/**
 * 帮助管理
类型1常见问题2新手指南3交易指南4币种质料5行情技术6条款协议7其他Controller
 * 
 * @author 花生
 * @date 2022-08-07
 */
@RestController
@RequestMapping("/problem")
@Api(tags = "内容管理-帮助管理")
public class PCommonProblemController extends BaseController
{
    @Autowired
    private IPCommonProblemService pCommonProblemService;

    /**
     * 查询帮助管理
类型1常见问题2新手指南3交易指南4币种质料5行情技术6条款协议7其他列表
     */
    @GetMapping("/list")
    @ApiOperation(value = "查询列表",notes = "查询列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页码", dataTypeClass = Long.class, paramType = "query", example = "1"),
            @ApiImplicitParam(name = "pageSize", value = "页大小", dataTypeClass = Long.class, paramType = "query", example = "10"),
            @ApiImplicitParam(name="content",value="搜索",dataTypeClass = String.class, paramType = "query"),
            @ApiImplicitParam(name="status",value="状态 1为开启 0闭关",dataTypeClass = String.class, paramType = "query")

    })
    public TableDataInfo list(String content,Integer status)
    {
        startPage();
        QueryWrapper<PCommonProblem> objectQueryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(content)){
            objectQueryWrapper.lambda().like(PCommonProblem::getContent,content);
            objectQueryWrapper.lambda().or().like(PCommonProblem::getTitle,content);
        }
        if (status!= null ){
            objectQueryWrapper.lambda().eq(PCommonProblem::getStatus,status);
        }
        List<PCommonProblem> list = pCommonProblemService.list(objectQueryWrapper);
        return getDataTable(list);
    }


    /**
     * 获取帮助管理
类型1常见问题2新手指南3交易指南4币种质料5行情技术6条款协议7其他详细信息
     */
    @GetMapping(value = "getInfo/{id}")
    @ApiOperation(value = "获取详情",notes = "获取详情")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(pCommonProblemService.getById(id));
    }

    /**
     * 新增帮助管理
类型1常见问题2新手指南3交易指南4币种质料5行情技术6条款协议7其他
     */
    @Log(title = "帮助管理 类型1常见问题2新手指南3交易指南4币种质料5行情技术6条款协议7其他", businessType = BusinessType.INSERT)
    @PostMapping("add")
    @ApiOperation(value = "新增",notes = "新增")
    public AjaxResult add(@RequestBody PCommonProblem pCommonProblem)
    {
        return toAjax(pCommonProblemService.insertPCommonProblem(pCommonProblem));
    }

    /**
     * 修改帮助管理
类型1常见问题2新手指南3交易指南4币种质料5行情技术6条款协议7其他
     */
    @PostMapping("edit")
    @ApiOperation(value = "修改",notes = "修改")
    public AjaxResult edit(@RequestBody PCommonProblem pCommonProblem)
    {
        return toAjax(pCommonProblemService.updatePCommonProblem(pCommonProblem));
    }

    /**
     * 删除帮助管理
类型1常见问题2新手指南3交易指南4币种质料5行情技术6条款协议7其他
     */
    @Log(title = "帮助管理 类型1常见问题2新手指南3交易指南4币种质料5行情技术6条款协议7其他", businessType = BusinessType.DELETE)
	@GetMapping("remove/{ids}")
    @ApiOperation(value = "删除",notes = "删除")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(pCommonProblemService.deletePCommonProblemByIds(ids));
    }
}
