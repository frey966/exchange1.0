package com.financia.business.dataconfiguration.controller;

import java.util.List;

import com.financia.business.dataconfiguration.service.IPConversionRateService;
import com.financia.exchange.PConversionRate;
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
import com.financia.common.core.web.controller.BaseController;
import com.financia.common.core.web.domain.AjaxResult;
import com.financia.common.core.web.page.TableDataInfo;

/**
 * 法币汇率Controller
 * 
 * @author 花生
 * @date 2022-08-08
 */
@Api(tags = "数据配置管理-汇率设置")
@RestController
@RequestMapping("/pconversionrate")
public class PConversionRateController extends BaseController
{
    @Autowired
    private IPConversionRateService pConversionRateService;

    /**
     * 查询法币汇率列表
     */
    @ApiOperation(value = "查询列表",notes = "查询列表")
//    @RequiresPermissions("business:rate:list")
    @GetMapping("/list")
    public TableDataInfo list(PConversionRate pConversionRate)
    {
        startPage();
        List<PConversionRate> list = pConversionRateService.selectPConversionRateList(pConversionRate);
        return getDataTable(list);
    }
//
//    /**
//     * 导出法币汇率列表
//     */
//    @ApiOperation(value = "查询列表",notes = "查询列表")
//    @RequiresPermissions("business:rate:export")
//    @Log(title = "法币汇率", businessType = BusinessType.EXPORT)
//    @PostMapping("/export")
//    public void export(HttpServletResponse response, PConversionRate pConversionRate)
//    {
//        List<PConversionRate> list = pConversionRateService.selectPConversionRateList(pConversionRate);
//        ExcelUtil<PConversionRate> util = new ExcelUtil<PConversionRate>(PConversionRate.class);
//        util.exportExcel(response, list, "法币汇率数据");
//    }

    /**
     * 获取法币汇率详细信息
     */
    @ApiOperation(value = "获取详情",notes = "获取详情")
//    @RequiresPermissions("business:rate:query")
    @GetMapping(value = "getInfo/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(pConversionRateService.selectPConversionRateById(id));
    }

    /**
     * 新增法币汇率
     */
    @ApiOperation(value = "新增",notes = "新增")
//    @RequiresPermissions("business:rate:add")
    @Log(title = "法币汇率", businessType = BusinessType.INSERT)
    @PostMapping("add")
    public AjaxResult add(@RequestBody PConversionRate pConversionRate)
    {
        return toAjax(pConversionRateService.insertPConversionRate(pConversionRate));
    }

    /**
     * 修改法币汇率
     */
    @ApiOperation(value = "修改",notes = "修改")
//    @RequiresPermissions("business:rate:edit")
    @Log(title = "法币汇率", businessType = BusinessType.UPDATE)
    @PostMapping("edit")
    public AjaxResult edit(@RequestBody PConversionRate pConversionRate)
    {
        return toAjax(pConversionRateService.updatePConversionRate(pConversionRate));
    }

    /**
     * 删除法币汇率
     */
    @ApiOperation(value = "删除",notes = "删除")
//    @RequiresPermissions("business:rate:remove")
    @Log(title = "法币汇率", businessType = BusinessType.DELETE)
	@GetMapping("remove/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(pConversionRateService.deletePConversionRateByIds(ids));
    }
}
