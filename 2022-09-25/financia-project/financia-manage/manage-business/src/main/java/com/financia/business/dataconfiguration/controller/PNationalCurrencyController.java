package com.financia.business.dataconfiguration.controller;

import java.util.List;

import com.financia.business.dataconfiguration.service.IPNationalCurrencyService;
import com.financia.exchange.PNationalCurrency;
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
 * 公共-法币信息Controller
 * 
 * @author 花生
 * @date 2022-08-07
 */
@Api(tags = "数据配置管理-法币设置")
@RestController
@RequestMapping("/pnationalcurrency")
public class PNationalCurrencyController extends BaseController
{
    @Autowired
    private IPNationalCurrencyService pNationalCurrencyService;

    /**
     * 查询公共-法币信息列表
     */
    @ApiOperation(value = "查询列表",notes = "查询列表")
//    @RequiresPermissions("system:currency:list")
    @GetMapping("/list")
    public TableDataInfo list(PNationalCurrency pNationalCurrency)
    {
        startPage();
        List<PNationalCurrency> list = pNationalCurrencyService.selectPNationalCurrencyList(pNationalCurrency);
        return getDataTable(list);
    }

//    /**
//     * 导出公共-法币信息列表
//     */
//    @ApiOperation(value = "查询列表",notes = "查询列表")
//    @RequiresPermissions("system:currency:export")
//    @Log(title = "公共-法币信息", businessType = BusinessType.EXPORT)
//    @PostMapping("/export")
//    public void export(HttpServletResponse response, PNationalCurrency pNationalCurrency)
//    {
//        List<PNationalCurrency> list = pNationalCurrencyService.selectPNationalCurrencyList(pNationalCurrency);
//        ExcelUtil<PNationalCurrency> util = new ExcelUtil<PNationalCurrency>(PNationalCurrency.class);
//        util.exportExcel(response, list, "公共-法币信息数据");
//    }

    /**
     * 获取公共-法币信息详细信息
     */
    @ApiOperation(value = "获取详情",notes = "获取详情")
//    @RequiresPermissions("system:currency:query")
    @GetMapping(value = "getInfo/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(pNationalCurrencyService.selectPNationalCurrencyById(id));
    }

    /**
     * 新增公共-法币信息
     */
    @ApiOperation(value = "新增",notes = "新增")
//    @RequiresPermissions("system:currency:add")
    @Log(title = "公共-法币信息", businessType = BusinessType.INSERT)
    @PostMapping("add")
    public AjaxResult add(@RequestBody PNationalCurrency pNationalCurrency)
    {
        return toAjax(pNationalCurrencyService.insertPNationalCurrency(pNationalCurrency));
    }

    /**
     * 修改公共-法币信息
     */
    @ApiOperation(value = "修改",notes = "修改")
//    @RequiresPermissions("system:currency:edit")
    @Log(title = "公共-法币信息", businessType = BusinessType.UPDATE)
    @PostMapping("edit")
    public AjaxResult edit(@RequestBody PNationalCurrency pNationalCurrency)
    {
        return toAjax(pNationalCurrencyService.updatePNationalCurrency(pNationalCurrency));
    }

    /**
     * 删除公共-法币信息
     */
//    @RequiresPermissions("system:currency:remove")
    @Log(title = "公共-法币信息", businessType = BusinessType.DELETE)
    @GetMapping("remove/{ids}")
    @ApiOperation(value = "删除",notes = "删除")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(pNationalCurrencyService.deletePNationalCurrencyByIds(ids));
    }
}
