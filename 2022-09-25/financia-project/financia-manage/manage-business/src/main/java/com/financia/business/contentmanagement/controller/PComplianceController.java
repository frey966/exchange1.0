package com.financia.business.contentmanagement.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.financia.business.contentmanagement.service.IPComplianceService;
import com.financia.common.PCompliance;
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
 * 内容管理_合规经营Controller
 * 
 * @author ruoyi
 * @date 2022-08-23
 */
@RestController
@RequestMapping("/compliance")
@Api(tags = "内容管理_合规经营")
public class PComplianceController extends BaseController
{
    @Autowired
    private IPComplianceService pComplianceService;

    /**
     * 查询内容管理_合规经营列表
     */
//    @RequiresPermissions("gen:compliance:list")
    @GetMapping("/list")
    @ApiOperation(value = "合规经营列表",notes = "合规经营列表")
    public TableDataInfo list(PCompliance pCompliance)
    {
        startPage();
        List<PCompliance> list = pComplianceService.selectPComplianceList(pCompliance);
        return getDataTable(list);
    }

//    /**
//     * 导出内容管理_合规经营列表
//     */
//    @RequiresPermissions("gen:compliance:export")
//    @Log(title = "内容管理_合规经营", businessType = BusinessType.EXPORT)
//    @PostMapping("/export")
//    @ApiOperation(value = "查询列表",notes = "查询列表")
//    public void export(HttpServletResponse response, PCompliance pCompliance)
//    {
//        List<PCompliance> list = pComplianceService.selectPComplianceList(pCompliance);
//        ExcelUtil<PCompliance> util = new ExcelUtil<PCompliance>(PCompliance.class);
//        util.exportExcel(response, list, "内容管理_合规经营数据");
//    }

    /**
     * 获取内容管理_合规经营详细信息
     */
//    @RequiresPermissions("gen:compliance:query")
    @GetMapping(value = "getInfo/{id}")
    @ApiOperation(value = "合规经营详细信息",notes = "合规经营详细信息")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(pComplianceService.selectPComplianceById(id));
    }

    /**
     * 新增内容管理_合规经营
     */
//    @RequiresPermissions("gen:compliance:add")
    @Log(title = "内容管理_合规经营", businessType = BusinessType.INSERT)
    @PostMapping("add")
    @ApiOperation(value = "新增",notes = "新增")
    public AjaxResult add(@RequestBody PCompliance pCompliance)
    {
        return toAjax(pComplianceService.insertPCompliance(pCompliance));
    }

    /**
     * 修改内容管理_合规经营
     */
//    @RequiresPermissions("gen:compliance:edit")
    @Log(title = "内容管理_合规经营", businessType = BusinessType.UPDATE)
    @PostMapping("edit")
    @ApiOperation(value = "修改",notes = "修改")
    public AjaxResult edit(@RequestBody PCompliance pCompliance)
    {
        return toAjax(pComplianceService.updatePCompliance(pCompliance));
    }

    /**
     * 删除内容管理_合规经营
     */
//    @RequiresPermissions("gen:compliance:remove")
    @Log(title = "内容管理_合规经营", businessType = BusinessType.DELETE)
	@GetMapping("remove/{ids}")
    @ApiOperation(value = "删除",notes = "删除")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(pComplianceService.deletePComplianceByIds(ids));
    }
}
