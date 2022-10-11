package com.financia.business.dataconfiguration.controller;

import com.financia.business.dataconfiguration.service.IPCryptoCurrencyService;
import com.financia.common.core.web.controller.BaseController;
import com.financia.common.core.web.domain.AjaxResult;
import com.financia.common.core.web.page.TableDataInfo;
import com.financia.common.log.annotation.Log;
import com.financia.common.log.enums.BusinessType;
import com.financia.exchange.PCryptoCurrency;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 公共-加密货币Controller
 *
 * @author 花生
 * @date 2022-08-07
 */
@Api(tags = "数据配置管理-加密货币设置")
@RestController
@RequestMapping("/pcryptocurrency")
public class PCryptoCurrencyController extends BaseController
{
    @Autowired
    private IPCryptoCurrencyService pCryptoCurrencyService;

    /**
     * 查询公共-加密货币列表
     */
    @ApiOperation(value = "查询列表",notes = "查询列表")
//    @RequiresPermissions("system:currency:list")
    @GetMapping("/list")
    public TableDataInfo list(PCryptoCurrency pCryptoCurrency)
    {
        startPage();
        List<PCryptoCurrency> list = pCryptoCurrencyService.selectPCryptoCurrencyList(pCryptoCurrency);
        return getDataTable(list);
    }

    /**
     * 查询公共-加密货币列表
     */
    @ApiOperation(value = "查询列表All",notes = "查询列表")
//    @RequiresPermissions("system:currency:list")
    @GetMapping("/listAll")
    public AjaxResult listAll(PCryptoCurrency pCryptoCurrency)
    {
        List<PCryptoCurrency> list = pCryptoCurrencyService.selectPCryptoCurrencyList(pCryptoCurrency);
        return AjaxResult.success(list);
    }

//    /**
//     * 导出公共-加密货币列表
//     */
//    @ApiOperation(value = "删除",notes = "删除")
//    @RequiresPermissions("system:currency:export")
//    @Log(title = "公共-加密货币", businessType = BusinessType.EXPORT)
//    @PostMapping("/export")
//    public void export(HttpServletResponse response, PCryptoCurrency pCryptoCurrency)
//    {
//        List<PCryptoCurrency> list = pCryptoCurrencyService.selectPCryptoCurrencyList(pCryptoCurrency);
//        ExcelUtil<PCryptoCurrency> util = new ExcelUtil<PCryptoCurrency>(PCryptoCurrency.class);
//        util.exportExcel(response, list, "公共-加密货币数据");
//    }

    /**
     * 获取公共-加密货币详细信息
     */
    @ApiOperation(value = "获取详情",notes = "获取详情")
//    @RequiresPermissions("system:currency:query")
    @GetMapping(value = "getInfo{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(pCryptoCurrencyService.selectPCryptoCurrencyById(id));
    }

    /**
     * 新增公共-加密货币
     */
    @ApiOperation(value = "新增",notes = "新增")
//    @RequiresPermissions("system:currency:add")
    @Log(title = "公共-加密货币", businessType = BusinessType.INSERT)
    @PostMapping("add")
    public AjaxResult add(@RequestBody PCryptoCurrency pCryptoCurrency)
    {
        return toAjax(pCryptoCurrencyService.insertPCryptoCurrency(pCryptoCurrency));
    }

    /**
     * 修改公共-加密货币
     */
    @ApiOperation(value = "修改",notes = "修改")
//    @RequiresPermissions("system:currency:edit")
    @Log(title = "公共-加密货币", businessType = BusinessType.UPDATE)
    @PostMapping("edit")
    public AjaxResult edit(@RequestBody PCryptoCurrency pCryptoCurrency)
    {
        return toAjax(pCryptoCurrencyService.updatePCryptoCurrency(pCryptoCurrency));
    }

    /**
     * 删除公共-加密货币
     */
    @ApiOperation(value = "删除",notes = "删除")
//    @RequiresPermissions("system:currency:remove")
    @Log(title = "公共-加密货币", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(pCryptoCurrencyService.deletePCryptoCurrencyByIds(ids));
    }
}
