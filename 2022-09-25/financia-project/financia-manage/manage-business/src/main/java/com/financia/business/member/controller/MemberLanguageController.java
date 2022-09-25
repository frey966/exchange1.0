package com.financia.business.member.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.financia.business.MemberLanguage;
import com.financia.business.member.service.IMemberLanguageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.financia.common.log.annotation.Log;
import com.financia.common.log.enums.BusinessType;
import com.financia.common.security.annotation.RequiresPermissions;
import com.financia.common.core.web.controller.BaseController;
import com.financia.common.core.web.domain.AjaxResult;
import com.financia.common.core.utils.poi.ExcelUtil;
import com.financia.common.core.web.page.TableDataInfo;

/**
 * 语言Controller
 * 
 * @author 花生
 * @date 2022-08-18
 */
@RestController
@RequestMapping("/memberlanguage")
@Api(tags="数据配置管理-国际语音管理")
public class MemberLanguageController extends BaseController
{
    @Autowired
    private IMemberLanguageService memberLanguageService;

    /**
     * 查询语言列表
     */
//    @RequiresPermissions("gen:language:list")
    @GetMapping("/list")
    @ApiOperation(value = "查询语言列表",notes = "查询语言列表")
    public TableDataInfo list(MemberLanguage memberLanguage)
    {
        startPage();
        List<MemberLanguage> list = memberLanguageService.selectMemberLanguageList(memberLanguage);
        return getDataTable(list);
    }

//    /**
//     * 导出语言列表
//     */
//    @RequiresPermissions("gen:language:export")
//    @Log(title = "语言", businessType = BusinessType.EXPORT)
//    @PostMapping("/export")
//    public void export(HttpServletResponse response, MemberLanguage memberLanguage)
//    {
//        List<MemberLanguage> list = memberLanguageService.selectMemberLanguageList(memberLanguage);
//        ExcelUtil<MemberLanguage> util = new ExcelUtil<MemberLanguage>(MemberLanguage.class);
//        util.exportExcel(response, list, "语言数据");
//    }

    /**
     * 获取语言详细信息
     */
//    @RequiresPermissions("gen:language:query")
    @GetMapping(value = "getInfo/{id}")
    @ApiOperation(value = "获取语言详细信息",notes = "获取语言详细信息")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(memberLanguageService.selectMemberLanguageById(id));
    }

    /**
     * 新增语言
     */
//    @RequiresPermissions("gen:language:add")
    @Log(title = "语言", businessType = BusinessType.INSERT)
    @PostMapping("add")
    @ApiOperation(value = "新增语言",notes = "新增语言")
    public AjaxResult add(@RequestBody MemberLanguage memberLanguage)
    {
        return toAjax(memberLanguageService.insertMemberLanguage(memberLanguage));
    }

    /**
     * 修改语言
     */
//    @RequiresPermissions("gen:language:edit")
    @Log(title = "语言", businessType = BusinessType.UPDATE)
    @PostMapping(value = "edit")
    @ApiOperation(value = "修改语言",notes = "修改语言")
    public AjaxResult edit(@RequestBody MemberLanguage memberLanguage)
    {
        return toAjax(memberLanguageService.updateMemberLanguage(memberLanguage));
    }

    /**
     * 删除语言
     */
//    @RequiresPermissions("gen:language:remove")
    @Log(title = "语言", businessType = BusinessType.DELETE)
	@GetMapping("remove/{ids}")
    @ApiOperation(value = "删除语言",notes = "删除语言")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(memberLanguageService.deleteMemberLanguageByIds(ids));
    }
}
