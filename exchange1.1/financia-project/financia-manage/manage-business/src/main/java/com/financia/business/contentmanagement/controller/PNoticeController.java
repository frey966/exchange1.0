package com.financia.business.contentmanagement.controller;

import java.util.List;

import com.financia.business.contentmanagement.service.IPNoticeService;
import com.financia.exchange.PNotice;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.financia.common.log.annotation.Log;
import com.financia.common.log.enums.BusinessType;
import com.financia.common.security.annotation.RequiresPermissions;
import com.financia.common.core.web.controller.BaseController;
import com.financia.common.core.web.domain.AjaxResult;
import com.financia.common.core.web.page.TableDataInfo;

/**
 * 公告管理Controller
 * 
 * @author 花生
 * @date 2022-08-16
 */
@RestController
@RequestMapping("/pnotice")
@Api(tags = "内容管理-公告管理")
public class PNoticeController extends BaseController
{
    @Autowired
    private IPNoticeService pNoticeService;

    /**
     * 查询公告管理列表
     */
//    @RequiresPermissions("gen:notice:list")
    @GetMapping("/list")
    @ApiOperation(value = "查询列表",notes = "查询列表")
    public TableDataInfo list(PNotice pNotice)
    {
        startPage();
        List<PNotice> list = pNoticeService.selectPNoticeList(pNotice);
        return getDataTable(list);
    }

//    /**
//     * 导出公告管理列表
//     */
//    @RequiresPermissions("gen:notice:export")
//    @Log(title = "公告管理", businessType = BusinessType.EXPORT)
//    @PostMapping("/export")
//    @ApiOperation(value = "查询列表",notes = "查询列表")
//    public void export(HttpServletResponse response, PNotice pNotice)
//    {
//        List<PNotice> list = pNoticeService.selectPNoticeList(pNotice);
//        ExcelUtil<PNotice> util = new ExcelUtil<PNotice>(PNotice.class);
//        util.exportExcel(response, list, "公告管理数据");
//    }

    /**
     * 获取公告管理详细信息
     */
//    @RequiresPermissions("gen:notice:query")
    @GetMapping(value = "getInfo/{id}")
    @ApiOperation(value = "获取公告管理详细信息",notes = "获取公告管理详细信息")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(pNoticeService.selectPNoticeById(id));
    }

    /**
     * 新增公告管理
     */
//    @RequiresPermissions("gen:notice:add")
    @Log(title = "公告管理", businessType = BusinessType.INSERT)
    @PostMapping("add")
    @ApiOperation(value = "新增",notes = "新增")
    public AjaxResult add(@RequestBody PNotice pNotice)
    {
        return toAjax(pNoticeService.insertPNotice(pNotice));
    }

    /**
     * 修改公告管理
     */
//    @RequiresPermissions("gen:notice:edit")
    @Log(title = "公告管理", businessType = BusinessType.UPDATE)
    @PostMapping("edit")
    @ApiOperation(value = "修改",notes = "修改")
    public AjaxResult edit(@RequestBody PNotice pNotice)
    {
        return toAjax(pNoticeService.updatePNotice(pNotice));
    }

    /**
     * 删除公告管理
     */
    @RequiresPermissions("gen:notice:remove")
    @Log(title = "公告管理", businessType = BusinessType.DELETE)
	@GetMapping("/{ids}")
    @ApiOperation(value = "删除",notes = "删除")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(pNoticeService.deletePNoticeByIds(ids));
    }
}
