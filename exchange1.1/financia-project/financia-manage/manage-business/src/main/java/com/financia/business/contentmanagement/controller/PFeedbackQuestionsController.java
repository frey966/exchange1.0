package com.financia.business.contentmanagement.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.financia.business.contentmanagement.service.PFeedbackQuestionsService;
import com.financia.common.PCommonProblem;
import com.financia.common.PFeedbackQuestions;
import com.financia.common.core.utils.StringUtils;
import com.financia.common.core.web.controller.BaseController;
import com.financia.common.core.web.domain.AjaxResult;
import com.financia.common.core.web.page.TableDataInfo;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@Api(tags = "APP反馈意见")
@RestController
@RequestMapping("feedback/questions")
@Slf4j
public class PFeedbackQuestionsController extends BaseController {

    @Autowired
    private PFeedbackQuestionsService pFeedbackQuestionsService;


    /**
     * 增加反馈
     */
    @GetMapping("/list")
    @ApiOperation(value = "反馈意见列表", notes = "反馈意见列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "content", value = "反馈内容", dataTypeClass = String.class, paramType = "query"),
            @ApiImplicitParam(name = "contact", value = "联系方式", dataTypeClass = String.class, paramType = "query"),
            @ApiImplicitParam(name = "account", value = "账号", dataTypeClass = String.class, paramType = "query")

    })
    public TableDataInfo list(String account, String contact, String content) {
        startPage();
        QueryWrapper<PFeedbackQuestions> objectQueryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(content)) {
            objectQueryWrapper.lambda().like(PFeedbackQuestions::getContent, content);
        }
        if (!StringUtils.isEmpty(contact)) {
            objectQueryWrapper.lambda().like(PFeedbackQuestions::getContact, content);
        }
        if (!StringUtils.isEmpty(account)) {
            objectQueryWrapper.lambda().like(PFeedbackQuestions::getAccount, content);
        }
        List<PFeedbackQuestions> list = pFeedbackQuestionsService.list(objectQueryWrapper);
        return getDataTable(list);
    }

    /**
     *反馈问题删除
     */
    @GetMapping("delete")
    @ApiOperation(value = "删除意见", notes = "删除意见")
    public AjaxResult add(String ids) {
        String[] split = ids.split(",");
        return toAjax(pFeedbackQuestionsService.removeBatchByIds(Lists.newArrayList(split)));
    }

}
