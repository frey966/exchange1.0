package com.financia.exchange.cs.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.financia.common.core.enums.DataStatus;
import com.financia.common.core.web.controller.BaseController;
import com.financia.common.core.web.domain.AjaxResult;
import com.financia.common.core.web.page.TableDataInfo;
import com.financia.cs.CsQuestion;
import com.financia.exchange.cs.service.CsQuestionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @ApiOperation(value = "问题列表",notes = "问题列表")
    public TableDataInfo list(CsQuestion question)
    {
        List<CsQuestion> list = csQuestionService.list(new LambdaQueryWrapper<CsQuestion>().eq(CsQuestion::getStatus, DataStatus.VALID.getCode()).eq(CsQuestion::getOnLine,1));
        return getDataTable(list);
    }


    /**
     *
     */
    @GetMapping(value = "getInfo/{id}")
    @ApiOperation(value = "问题详细信息",notes = "通知详细信息")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(csQuestionService.getById(id));
    }

}
