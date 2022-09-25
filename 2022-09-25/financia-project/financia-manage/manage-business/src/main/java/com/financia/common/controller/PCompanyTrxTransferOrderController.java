package com.financia.common.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.annotation.TableName;
import com.financia.common.PCompanyTrxTransferOrder;
import com.financia.common.service.IPCompanyTrxTransferOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
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
 * 公共-公司钱包TRX转账记录Controller
 * 
 * @author 花生
 * @date 2022-08-28
 */
@RestController
@RequestMapping("/pcompanyTrxTransferOrder")
@Api(tags="财务管理-trx转账记录")
public class PCompanyTrxTransferOrderController extends BaseController
{
    @Autowired
    private IPCompanyTrxTransferOrderService pCompanyTrxTransferOrderService;

    /**
     * 查询公共-公司钱包TRX转账记录列表
     */
//    @RequiresPermissions("gen:order:list")
    @GetMapping("/list")
    @ApiOperation(value = "TRX转账记录列表",notes = "TRX转账记录列表")
    public TableDataInfo list(PCompanyTrxTransferOrder pCompanyTrxTransferOrder)
    {
        startPage();
        List<PCompanyTrxTransferOrder> list = pCompanyTrxTransferOrderService.selectPCompanyTrxTransferOrderList(pCompanyTrxTransferOrder);
        return getDataTable(list);
    }

//    /**
//     * 导出公共-公司钱包TRX转账记录列表
//     */
//    @RequiresPermissions("gen:order:export")
//    @Log(title = "公共-公司钱包TRX转账记录", businessType = BusinessType.EXPORT)
//    @PostMapping("/export")
//    @ApiOperation(value = "TRX转账记录列表",notes = "TRX转账记录列表")
//    public void export(HttpServletResponse response, PCompanyTrxTransferOrder pCompanyTrxTransferOrder)
//    {
//        List<PCompanyTrxTransferOrder> list = pCompanyTrxTransferOrderService.selectPCompanyTrxTransferOrderList(pCompanyTrxTransferOrder);
//        ExcelUtil<PCompanyTrxTransferOrder> util = new ExcelUtil<PCompanyTrxTransferOrder>(PCompanyTrxTransferOrder.class);
//        util.exportExcel(response, list, "公共-公司钱包TRX转账记录数据");
//    }

    /**
     * 获取公共-公司钱包TRX转账记录详细信息
     */
//    @RequiresPermissions("gen:order:query")
    @GetMapping(value = "/{id}")
    @ApiOperation(value = "TRX转账记录详细信息",notes = "TRX转账记录详细信息")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(pCompanyTrxTransferOrderService.selectPCompanyTrxTransferOrderById(id));
    }

    /**
     * 新增公共-公司钱包TRX转账记录
     */
//    @RequiresPermissions("gen:order:add")
    @Log(title = "公共-公司钱包TRX转账记录", businessType = BusinessType.INSERT)
    @PostMapping("add")
    @ApiOperation(value = "TRX转账记录新增",notes = "TRX转账记录新增")
    public AjaxResult add(@RequestBody PCompanyTrxTransferOrder pCompanyTrxTransferOrder)
    {
        return toAjax(pCompanyTrxTransferOrderService.insertPCompanyTrxTransferOrder(pCompanyTrxTransferOrder));
    }

    /**
     * 修改公共-公司钱包TRX转账记录
     */
//    @RequiresPermissions("gen:order:edit")
    @Log(title = "公共-公司钱包TRX转账记录", businessType = BusinessType.UPDATE)
    @PostMapping("edit")
    @ApiOperation(value = "TRX转账记录修改",notes = "TRX转账记录修改")
    public AjaxResult edit(@RequestBody PCompanyTrxTransferOrder pCompanyTrxTransferOrder)
    {
        return toAjax(pCompanyTrxTransferOrderService.updatePCompanyTrxTransferOrder(pCompanyTrxTransferOrder));
    }

    /**
     * 删除公共-公司钱包TRX转账记录
     */
//    @RequiresPermissions("gen:order:remove")
    @Log(title = "公共-公司钱包TRX转账记录", businessType = BusinessType.DELETE)
	@GetMapping("remove/{ids}")
    @ApiOperation(value = "TRX转账记录删除",notes = "TRX转账记录删除")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(pCompanyTrxTransferOrderService.deletePCompanyTrxTransferOrderByIds(ids));
    }
}
