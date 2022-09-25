package com.financia.business.contentmanagement.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.financia.business.contentmanagement.service.IPShareInfoService;
import com.financia.common.MemberShare;
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
 * 个人中心-分享数据Controller
 * 
 * @author 花生
 * @date 2022-08-23
 */
@RestController
@RequestMapping("/pshareinfo")
@Api(tags = "内容管理_分享数据")
public class PShareInfoController extends BaseController
{
    @Autowired
    private IPShareInfoService pShareInfoService;

    /**
     * 查询个人中心-分享数据列表
     */
//    @RequiresPermissions("gen:info:list")
    @GetMapping("/list")
    @ApiOperation(value = "分享数据列表",notes = "分享数据列表")
    public TableDataInfo list(MemberShare pShareInfo)
    {
        startPage();
        List<MemberShare> list = pShareInfoService.selectPShareInfoList(pShareInfo);
        return getDataTable(list);
    }

//    /**
//     * 导出个人中心-分享数据列表
//     */
////    @RequiresPermissions("gen:info:export")
//    @Log(title = "个人中心-分享数据", businessType = BusinessType.EXPORT)
//    @PostMapping("/export")
//    @ApiOperation(value = "分享数据列表",notes = "分享数据列表")
//    public void export(HttpServletResponse response, MemberShare pShareInfo)
//    {
//        List<MemberShare> list = pShareInfoService.selectPShareInfoList(pShareInfo);
//        ExcelUtil<MemberShare> util = new ExcelUtil<MemberShare>(MemberShare.class);
//        util.exportExcel(response, list, "个人中心-分享数据数据");
//    }

    /**
     * 获取个人中心-分享数据详细信息
     */
//    @RequiresPermissions("gen:info:query")
    @GetMapping(value = "getInfo/{shareId}")
    @ApiOperation(value = "分享数据详细信息",notes = "分享数据详细信息")
    public AjaxResult getInfo(@PathVariable("shareId") Long shareId)
    {
        return AjaxResult.success(pShareInfoService.selectPShareInfoByShareId(shareId));
    }

    /**
     * 新增个人中心-分享数据
     */
//    @RequiresPermissions("gen:info:add")
    @Log(title = "个人中心-分享数据", businessType = BusinessType.INSERT)
    @PostMapping("add")
    @ApiOperation(value = "新增",notes = "新增")
    public AjaxResult add(@RequestBody MemberShare pShareInfo)
    {
        return toAjax(pShareInfoService.insertPShareInfo(pShareInfo));
    }

    /**
     * 修改个人中心-分享数据
     */
//    @RequiresPermissions("gen:info:edit")
    @Log(title = "个人中心-分享数据", businessType = BusinessType.UPDATE)
    @PostMapping("edit")
    @ApiOperation(value = "修改",notes = "修改")
    public AjaxResult edit(@RequestBody MemberShare pShareInfo)
    {
        return toAjax(pShareInfoService.updatePShareInfo(pShareInfo));
    }

    /**
     * 删除个人中心-分享数据
     */
//    @RequiresPermissions("gen:info:remove")
    @Log(title = "个人中心-分享数据", businessType = BusinessType.DELETE)
	@GetMapping("/{shareIds}")
    @ApiOperation(value = "删除",notes = "删除")
    public AjaxResult remove(@PathVariable Long[] shareIds)
    {
        return toAjax(pShareInfoService.deletePShareInfoByShareIds(shareIds));
    }
}
