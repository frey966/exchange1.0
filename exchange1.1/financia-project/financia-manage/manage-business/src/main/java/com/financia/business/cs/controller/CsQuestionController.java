package com.financia.business.cs.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.financia.business.cs.service.CsQuestionService;
import com.financia.common.core.enums.DataStatus;
import com.financia.common.core.web.controller.BaseController;
import com.financia.common.core.web.domain.AjaxResult;
import com.financia.common.core.web.page.TableDataInfo;
import com.financia.common.log.annotation.Log;
import com.financia.common.log.enums.BusinessType;
import com.financia.cs.CsQuestion;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**

 */
@RestController
@RequestMapping("/question")
@Api(tags="客服-自动回复配置")
public class CsQuestionController extends BaseController
{
    @Autowired
    private CsQuestionService csQuestionService;

    /**
     * 通知列表
     */
//    @RequiresPermissions("gen:us:list")
    @GetMapping("/list")
    @ApiOperation(value = "问题管理列表",notes = "问题管理列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name="pageNum",value="页码",dataTypeClass = Long.class, paramType = "query",example="1"),
            @ApiImplicitParam(name="pageSize",value="页大小",dataTypeClass = Long.class, paramType = "query",example="10")
    })
    public TableDataInfo list(CsQuestion question)
    {
        startPage();
        LambdaQueryWrapper<CsQuestion> csQuestionLambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (question.getOnLine() != null) {
            csQuestionLambdaQueryWrapper.eq(CsQuestion::getOnLine,question.getOnLine()).eq(CsQuestion::getStatus, DataStatus.VALID.getCode());
        }else {
            csQuestionLambdaQueryWrapper.eq(CsQuestion::getStatus, DataStatus.VALID.getCode());
        }
        List<CsQuestion> list = csQuestionService.list(csQuestionLambdaQueryWrapper);
        return getDataTable(list);
    }


    /**
     *
     */
    @GetMapping(value = "getInfo/{id}")
    @ApiOperation(value = "问题管理详细信息",notes = "通知管理详细信息")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(csQuestionService.getById(id));
    }

    /**
     * 新增个人中心-关于我们
     */
//    @RequiresPermissions("gen:us:add")
    @Log(title = "问题管理", businessType = BusinessType.INSERT)
    @PostMapping("add")
    @ApiOperation(value = "新增",notes = "新增")
    public AjaxResult add(@RequestBody CsQuestion question)
    {
        question.setUpdateTime(new Date());
        return toAjax(csQuestionService.save(question));
    }

    /**
     * 问题管理
     */
//    @RequiresPermissions("gen:us:edit")
    @Log(title = "问题管理", businessType = BusinessType.UPDATE)
    @PostMapping("edit")
    @ApiOperation(value = "修改",notes = "修改")
    public AjaxResult edit(@RequestBody CsQuestion question)
    {
        return toAjax(csQuestionService.updateById(question));
    }

    /**
     * 问题管理
     */
//    @RequiresPermissions("gen:us:remove")
    @Log(title = "问题管理", businessType = BusinessType.DELETE)
	@GetMapping("remove/{ids}")
    @ApiOperation(value = "删除",notes = "删除")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        for (Long id : ids) {
            CsQuestion question = new CsQuestion();
            question.setId(id);
            question.setStatus(DataStatus.DELETED.getCode());
            csQuestionService.updateById(question);
        }
        return toAjax(true);
    }
}
