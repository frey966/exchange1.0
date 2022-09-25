package com.financia.business.dataconfiguration.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.financia.business.dataconfiguration.service.IExSuperContractOrderEntrustService;
import com.financia.superleverage.SuperContractOrderEntrust;
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
 * 超级杠杆-委托订单Controller
 * 
 * @author 花生
 * @date 2022-09-07
 */
@RestController
@Api(tags = "超级杠杆-交易订单")
@RequestMapping("/SuperContractOrderEntrust")
public class ExSuperContractOrderEntrustController extends BaseController
{
    @Autowired
    private IExSuperContractOrderEntrustService exSuperContractOrderEntrustService;

    /**
     * 查询超级杠杆-委托订单列表
     */
//    @RequiresPermissions("gen:entrust:list")
    @ApiOperation(value = "订单列表",notes = "订单列表")
    @GetMapping("/list")
    public TableDataInfo list(SuperContractOrderEntrust exSuperContractOrderEntrust)
    {
        startPage();
        List<SuperContractOrderEntrust> list = exSuperContractOrderEntrustService.selectExSuperContractOrderEntrustList(exSuperContractOrderEntrust);
        return getDataTable(list);
    }

//    /**
//     * 导出超级杠杆-委托订单列表
//     */
//    @RequiresPermissions("gen:entrust:export")
//    @Log(title = "超级杠杆-委托订单", businessType = BusinessType.EXPORT)
//    @PostMapping("/export")
//    public void export(HttpServletResponse response, SuperContractOrderEntrust exSuperContractOrderEntrust)
//    {
//        List<SuperContractOrderEntrust> list = exSuperContractOrderEntrustService.selectExSuperContractOrderEntrustList(exSuperContractOrderEntrust);
//        ExcelUtil<SuperContractOrderEntrust> util = new ExcelUtil<SuperContractOrderEntrust>(SuperContractOrderEntrust.class);
//        util.exportExcel(response, list, "超级杠杆-委托订单数据");
//    }

    /**
     * 获取超级杠杆-委托订单详细信息
     */
//    @RequiresPermissions("gen:entrust:query")
    @GetMapping(value = "getInfo/{id}")
    @ApiOperation(value = "订单详情",notes = "订单详情")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(exSuperContractOrderEntrustService.selectExSuperContractOrderEntrustById(id));
    }

//    /**
//     * 新增超级杠杆-委托订单
//     */
////    @RequiresPermissions("gen:entrust:add")
//    @Log(title = "超级杠杆-委托订单", businessType = BusinessType.INSERT)
//    @ApiOperation(value = "订单详情",notes = "订单详情")
//    @PostMapping("add")
//    public AjaxResult add(@RequestBody SuperContractOrderEntrust exSuperContractOrderEntrust)
//    {
//        return toAjax(exSuperContractOrderEntrustService.insertExSuperContractOrderEntrust(exSuperContractOrderEntrust));
//    }

    /**
     * 修改超级杠杆-委托订单
     */
//    @RequiresPermissions("gen:entrust:edit")
    @Log(title = "超级杠杆-委托订单", businessType = BusinessType.UPDATE)
    @PostMapping("edit")
    @ApiOperation(value = "订单修改",notes = "订单修改")
    public AjaxResult edit(@RequestBody SuperContractOrderEntrust exSuperContractOrderEntrust)
    {
        return toAjax(exSuperContractOrderEntrustService.updateExSuperContractOrderEntrust(exSuperContractOrderEntrust));
    }

    /**
     * 删除超级杠杆-委托订单
     */
//    @RequiresPermissions("gen:entrust:remove")
    @Log(title = "超级杠杆-委托订单", businessType = BusinessType.DELETE)
    @ApiOperation(value = "订单删除",notes = "订单删除")
	@GetMapping("remove/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(exSuperContractOrderEntrustService.deleteExSuperContractOrderEntrustByIds(ids));
    }
}
