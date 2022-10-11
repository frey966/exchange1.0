package com.financia.common.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.financia.common.PCountryCode;
import com.financia.common.service.IPCountryCodeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
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
 * 国家区号Controller
 *
 * @author 花生
 * @date 2022-08-05
 */
@RestController
@RequestMapping("/pcountrycode")
@Api(tags="数据配置管理-国家区域号码")
public class PCountryCodeController extends BaseController
{
    @Autowired
    private IPCountryCodeService pCountryCodeService;

    /**
     * 查询国家区号列表
     */
//    @RequiresPermissions("system:code:list")
    @GetMapping("/list")
    @ApiOperation(value = "查询国家区号列表",notes = "查询国家区号列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name="pageNum",value="页码",dataTypeClass = Long.class, paramType = "query",example="1"),
            @ApiImplicitParam(name="pageSize",value="页大小",dataTypeClass = Long.class, paramType = "query",example="10")
    })
    public TableDataInfo list(PCountryCode pCountryCode)
    {
        startPage();
        List<PCountryCode> list = pCountryCodeService.selectPCountryCodeList(pCountryCode);
        return getDataTable(list);
    }

//    /**
//     * 导出国家区号列表
//     */
//    @RequiresPermissions("system:code:export")
//    @Log(title = "国家区号", businessType = BusinessType.EXPORT)
//    @PostMapping("/export")
//    @ApiOperation(value = "查询国家区号列表",notes = "查询国家区号列表")
//    public void export(HttpServletResponse response, PCountryCode pCountryCode)
//    {
//        List<PCountryCode> list = pCountryCodeService.selectPCountryCodeList(pCountryCode);
//        ExcelUtil<PCountryCode> util = new ExcelUtil<PCountryCode>(PCountryCode.class);
//        util.exportExcel(response, list, "国家区号数据");
//    }

    /**
     * 获取国家区号详细信息
     */
//    @RequiresPermissions("system:code:query")
    @GetMapping(value = "/{id}")
    @ApiOperation(value = "获取国家区号详细信息",notes = "获取国家区号详细信息")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(pCountryCodeService.selectPCountryCodeById(id));
    }

    /**
     * 新增国家区号
     */
//    @RequiresPermissions("system:code:add")
    @Log(title = "国家区号", businessType = BusinessType.INSERT)
    @PostMapping("add")
    @ApiOperation(value = "新增国家区号",notes = "新增国家区号")
    public AjaxResult add(@RequestBody PCountryCode pCountryCode)
    {
        return toAjax(pCountryCodeService.insertPCountryCode(pCountryCode));
    }

    /**
     * 修改国家区号
     */
//    @RequiresPermissions("system:code:edit")
    @Log(title = "国家区号", businessType = BusinessType.UPDATE)
    @PostMapping("edit")
    @ApiOperation(value = "修改国家区号",notes = "修改国家区号")
    public AjaxResult edit(@RequestBody PCountryCode pCountryCode)
    {
        return toAjax(pCountryCodeService.updatePCountryCode(pCountryCode));
    }

    /**
     * 删除国家区号
     */
    @ApiOperation(value = "删除国家区号",notes = "删除国家区号")
//    @RequiresPermissions("system:code:remove")
    @Log(title = "国家区号", businessType = BusinessType.DELETE)
    @GetMapping("remove/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(pCountryCodeService.deletePCountryCodeByIds(ids));
    }
}

