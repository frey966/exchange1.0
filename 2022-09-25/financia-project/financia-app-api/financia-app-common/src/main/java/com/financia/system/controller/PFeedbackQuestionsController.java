package com.financia.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.financia.common.PCommonProblem;
import com.financia.common.PFeedbackQuestions;
import com.financia.common.core.utils.StringUtils;
import com.financia.common.core.web.controller.BaseController;
import com.financia.common.core.web.domain.AjaxResult;
import com.financia.exchange.Member;
import com.financia.system.service.IMemberService;
import com.financia.system.service.PFeedbackQuestionsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;


@Api(tags="APP反馈意见")
@RestController
@RequestMapping("feedback/questions")
@Slf4j
public class PFeedbackQuestionsController extends BaseController {

    @Autowired
    private PFeedbackQuestionsService pFeedbackQuestionsService;

    @Autowired
    private IMemberService memberService;

    /**
     * 增加反馈
     */
    @GetMapping ("/add")
    @ApiOperation(value = "提交意见",notes = "提交意见")
    @ApiImplicitParams({
            @ApiImplicitParam(name="content",value="反馈内容",dataTypeClass = String.class, paramType = "query"),
            @ApiImplicitParam(name="contact",value="联系方式",dataTypeClass = String.class, paramType = "query")

    })
    public AjaxResult list(String content,String contact){
        Long userId = getUserId();
        Member member = memberService.getById(userId);
        PFeedbackQuestions  pFeedbackQuestions=new PFeedbackQuestions();
        pFeedbackQuestions.setAccount(member.getUsername());
        pFeedbackQuestions.setContact(contact);
        pFeedbackQuestions.setContent(content);
        pFeedbackQuestions.setCreateTime(new Date());
        pFeedbackQuestionsService.save(pFeedbackQuestions);
        return AjaxResult.success();
    }
}
