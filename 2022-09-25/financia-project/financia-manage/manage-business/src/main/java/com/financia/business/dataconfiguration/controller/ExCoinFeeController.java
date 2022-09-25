package com.financia.business.dataconfiguration.controller;

import java.util.List;

import com.financia.business.ExCoinFee;
import com.financia.business.dataconfiguration.service.IExCoinFeeService;
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
 * 合约-杠杆手续费率Controller
 *
 * @author 花生
 * @date 2022-08-11
 */
@RestController
@RequestMapping("/excoinfee")
@Api(tags = "合约管理-合约手续")
public class ExCoinFeeController extends BaseController
{
    @Autowired
    private IExCoinFeeService exCoinFeeService;

    /**
     * 查询合约-杠杆手续费率列表
     */
//    @RequiresPermissions("system:fee:list")
    @GetMapping("/list")
    @ApiOperation(value = "查询列表",notes = "查询列表")
    public TableDataInfo list(ExCoinFee exCoinFee)
    {
        startPage();
        List<ExCoinFee> list = exCoinFeeService.selectExCoinFeeList(exCoinFee);
        return getDataTable(list);
    }

//    /**
//     * 导出合约-杠杆手续费率列表
//     */
//    @RequiresPermissions("system:fee:export")
//    @Log(title = "合约-杠杆手续费率", businessType = BusinessType.EXPORT)
//    @PostMapping("/export")
//    public void export(HttpServletResponse response, ExCoinFee exCoinFee)
//    {
//        List<ExCoinFee> list = exCoinFeeService.selectExCoinFeeList(exCoinFee);
//        ExcelUtil<ExCoinFee> util = new ExcelUtil<ExCoinFee>(ExCoinFee.class);
//        util.exportExcel(response, list, "合约-杠杆手续费率数据");
//    }

    /**
     * 获取合约-杠杆手续费率详细信息
     */
//    @RequiresPermissions("system:fee:query")
    @GetMapping(value = "getInfo/{id}")
    @ApiOperation(value = "获取详情",notes = "获取详情")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(exCoinFeeService.selectExCoinFeeById(id));
    }

    /**
     * 新增合约-杠杆手续费率
     */
//    @RequiresPermissions("system:fee:add")
    @Log(title = "合约-杠杆手续费率", businessType = BusinessType.INSERT)
    @PostMapping("add")
    @ApiOperation(value = "新增",notes = "新增")
    public AjaxResult add(@RequestBody ExCoinFee exCoinFee)
    {
        return toAjax(exCoinFeeService.insertExCoinFee(exCoinFee));
    }

    /**
     * 修改合约-杠杆手续费率
     */
//    @RequiresPermissions("system:fee:edit")
    @Log(title = "合约-杠杆手续费率", businessType = BusinessType.UPDATE)
    @PostMapping("edit")
    @ApiOperation(value = "修改",notes = "修改")
    public AjaxResult edit(@RequestBody ExCoinFee exCoinFee)
    {
        return toAjax(exCoinFeeService.updateExCoinFee(exCoinFee));
    }

    /**
     * 删除合约-杠杆手续费率
     */
//    @RequiresPermissions("system:fee:remove")
    @Log(title = "合约-杠杆手续费率", businessType = BusinessType.DELETE)
	@GetMapping("remove/{ids}")
    @ApiOperation(value = "删除",notes = "删除")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(exCoinFeeService.deleteExCoinFeeByIds(ids));
    }
}
