package com.financia.system.controller;

import com.financia.business.MemberLanguage;
import com.financia.common.core.web.controller.BaseController;
import com.financia.common.core.web.domain.AjaxResult;
import com.financia.common.core.web.page.TableDataInfo;
import com.financia.common.log.annotation.Log;
import com.financia.common.log.enums.BusinessType;
import com.financia.system.service.IMemberLanguageServiceApi;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 语言Controller
 * 
 * @author 花生
 * @date 2022-08-18
 */
@RestController
@RequestMapping("/memberlanguage")
@Api(tags="公共-语言表")
public class MemberLanguageController extends BaseController
{
    @Autowired
    private IMemberLanguageServiceApi memberLanguageService;

    /**
     * 查询语言列表
     */
    @GetMapping("/list")
    @ApiOperation(value = "查询语言列表",notes = "查询语言列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name="pageNum",value="页码",dataTypeClass = Long.class, paramType = "query",example="1"),
            @ApiImplicitParam(name="pageSize",value="页大小",dataTypeClass = Long.class, paramType = "query",example="10")
    })
    public TableDataInfo list(MemberLanguage memberLanguage)
    {
        startPage();
        List<MemberLanguage> list = memberLanguageService.selectMemberLanguageList(memberLanguage);
        return getDataTable(list);
    }



    /**
     * 获取语言详细信息
     */
    @GetMapping(value = "getInfo/{id}")
    @ApiOperation(value = "获取语言详细信息",notes = "获取语言详细信息")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(memberLanguageService.selectMemberLanguageById(id));
    }

}
