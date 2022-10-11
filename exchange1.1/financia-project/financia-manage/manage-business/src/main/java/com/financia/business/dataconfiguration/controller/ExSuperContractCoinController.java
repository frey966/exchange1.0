package com.financia.business.dataconfiguration.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.financia.business.dataconfiguration.service.IExSuperContractCoinService;
import com.financia.superleverage.SuperContractCoin;
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
 * 超级杠杆-币种列Controller
 * 
 * @author 花生
 * @date 2022-09-07
 */
@RestController
@Api(tags = "超级杠杆-采集设置")
@RequestMapping("/SuperContractCoin")
public class ExSuperContractCoinController extends BaseController
{
    @Autowired
    private IExSuperContractCoinService exSuperContractCoinService;

    /**
     * 查询超级杠杆-币种列列表
     */
//    @RequiresPermissions("gen:coin:list")
    @GetMapping("/list")
    @ApiOperation(value = "采集设置列表",notes = "采集设置列表")
    public TableDataInfo list(SuperContractCoin exSuperContractCoin)
    {
        startPage();
        List<SuperContractCoin> list = exSuperContractCoinService.selectExSuperContractCoinList(exSuperContractCoin);
        return getDataTable(list);
    }

//    /**
//     * 导出超级杠杆-币种列列表
//     */
//    @RequiresPermissions("gen:coin:export")
//    @Log(title = "超级杠杆-币种列", businessType = BusinessType.EXPORT)
//    @PostMapping("/export")
//    public void export(HttpServletResponse response, SuperContractCoin exSuperContractCoin)
//    {
//        List<SuperContractCoin> list = exSuperContractCoinService.selectExSuperContractCoinList(exSuperContractCoin);
//        ExcelUtil<SuperContractCoin> util = new ExcelUtil<SuperContractCoin>(SuperContractCoin.class);
//        util.exportExcel(response, list, "超级杠杆-币种列数据");
//    }

    /**
     * 获取超级杠杆-币种列详细信息
     */
//    @RequiresPermissions("gen:coin:query")
    @GetMapping(value = "getInfo/{id}")
    @ApiOperation(value = "采集设置详情",notes = "采集设置详情")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(exSuperContractCoinService.selectExSuperContractCoinById(id));
    }

    /**
     * 新增超级杠杆-币种列
     */
//    @RequiresPermissions("gen:coin:add")
    @Log(title = "超级杠杆-币种列", businessType = BusinessType.INSERT)
    @PostMapping("add")
    @ApiOperation(value = "采集设置添加",notes = "采集设置添加")
    public AjaxResult add(@RequestBody SuperContractCoin exSuperContractCoin)
    {
        return toAjax(exSuperContractCoinService.insertExSuperContractCoin(exSuperContractCoin));
    }

    /**
     * 修改超级杠杆-币种列
     */
//    @RequiresPermissions("gen:coin:edit")
    @Log(title = "超级杠杆-币种列", businessType = BusinessType.UPDATE)
    @PostMapping("edit")
    @ApiOperation(value = "采集设置修改",notes = "采集设置修改")
    public AjaxResult edit(@RequestBody SuperContractCoin exSuperContractCoin)
    {
        return toAjax(exSuperContractCoinService.updateExSuperContractCoin(exSuperContractCoin));
    }

    /**
     * 删除超级杠杆-币种列
     */
//    @RequiresPermissions("gen:coin:remove")
    @Log(title = "超级杠杆-币种列", businessType = BusinessType.DELETE)
	@GetMapping("remove/{ids}")
    @ApiOperation(value = "采集设置删除",notes = "采集设置删除")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(exSuperContractCoinService.deleteExSuperContractCoinByIds(ids));
    }
}
