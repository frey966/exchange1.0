package com.financia.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.financia.common.PCommonProblem;
import com.financia.common.PCountryCode;
import com.financia.common.core.utils.StringUtils;
import com.financia.common.core.web.domain.AjaxResult;
import com.financia.system.service.PCommonProblemService;
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


@Api(tags="APP常见问题")
@RestController
@RequestMapping("common/problem")
@Slf4j
public class PCommonProblemController {

    @Autowired
    private PCommonProblemService pCommonProblemService;

    /**
     * 列表
     */
    @GetMapping("/list")
    @ApiOperation(value = "常见问题列表",notes = "常见问题列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name="content",value="搜索",dataTypeClass = String.class, paramType = "query")
    })
    public AjaxResult list(String content){
        QueryWrapper<PCommonProblem> objectQueryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(content)){
            objectQueryWrapper.lambda().like(PCommonProblem::getContent,content);
            objectQueryWrapper.lambda().or().like(PCommonProblem::getTitle,content);
        }
        objectQueryWrapper.lambda().eq(PCommonProblem::getStatus,1);
        List<PCommonProblem> list = pCommonProblemService.list(objectQueryWrapper);
        return AjaxResult.success(list);
    }
}
