package com.financia.business.dataconfiguration.controller;

import com.financia.business.ExCurrency;
import com.financia.business.dataconfiguration.service.IExCurrencyService;
import com.financia.common.core.web.controller.BaseController;
import com.financia.common.core.web.domain.AjaxResult;
import com.financia.common.core.web.page.TableDataInfo;
import com.financia.common.log.annotation.Log;
import com.financia.common.log.enums.BusinessType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 货币Controller
 *
 * @author 花生
 * @date 2022-08-02
 */
@RestController
@Api(tags = "币币管理-币币采集对")
@RequestMapping("/currency")
public class ExCurrencyController extends BaseController
{
    @Autowired
    private IExCurrencyService exCurrencyService;

    /**
     * 查询货币列表
     */
    @ApiOperation(value = "查询货币列表",notes = "查询货币列表")
//    @RequiresPermissions("system:currency:list")
    @GetMapping("/list")
    public TableDataInfo list(ExCurrency exCurrency)
    {
        startPage();
        List<ExCurrency> list = exCurrencyService.selectExCurrencyList(exCurrency);
        return getDataTable(list);
    }

    /**
     * 查询货币列表
     */
    @ApiOperation(value = "查询货币列表",notes = "查询货币列表")
//    @RequiresPermissions("system:currency:list")
    @GetMapping("/listAll")
    public AjaxResult listAll(ExCurrency exCurrency)
    {
        List<ExCurrency> list = exCurrencyService.selectExCurrencyList(exCurrency);
        return AjaxResult.success(list);
    }
//
//    /**
//     * 导出货币列表
//     */
//    @RequiresPermissions("system:currency:export")
////    @Log(title = "货币", businessType = BusinessType.EXPORT)
//    @PostMapping("/export")
//    public void export(HttpServletResponse response, ExCurrency exCurrency)
//    {
//        List<ExCurrency> list = exCurrencyService.selectExCurrencyList(exCurrency);
//        ExcelUtil<ExCurrency> util = new ExcelUtil<ExCurrency>(ExCurrency.class);
//        util.exportExcel(response, list, "货币数据");
//    }

    /**
     * 获取货币详细信息
     */
    @ApiOperation(value = "获取货币详细信息",notes = "获取货币详细信息")
//    @RequiresPermissions("system:currency:query")
    @GetMapping(value = "query/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(exCurrencyService.selectExCurrencyById(id));
    }

    /**
     * 新增货币
     */
    @ApiOperation(value = "新增货币",notes = "新增货币")
//    @RequiresPermissions("system:currency:add")
    @Log(title = "货币", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ExCurrency exCurrency)
    {
        return toAjax(exCurrencyService.insertExCurrency(exCurrency));
    }

    /**
     * 修改货币
     */
    @ApiOperation(value = "修改货币",notes = "修改货币")
//    @RequiresPermissions("system:currency:edit")
    @Log(title = "货币", businessType = BusinessType.UPDATE)
    @PostMapping(value = "edit")
    public AjaxResult edit(@RequestBody ExCurrency exCurrency)
    {
        return toAjax(exCurrencyService.updateExCurrency(exCurrency));
    }

    /**
     * 删除货币
     */
    @ApiOperation(value = "删除货币",notes = "删除货币")
//    @RequiresPermissions("system:currency:remove")
    @Log(title = "货币", businessType = BusinessType.DELETE)
	@GetMapping("remove/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(exCurrencyService.deleteExCurrencyByIds(ids));
    }
}
