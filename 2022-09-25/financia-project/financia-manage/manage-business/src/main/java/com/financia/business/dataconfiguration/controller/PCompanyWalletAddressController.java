package com.financia.business.dataconfiguration.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.financia.business.dataconfiguration.service.IPCompanyWalletAddressService;
import com.financia.common.PCompanyWalletAddress;
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
 * 公链钱包Controller
 * 
 * @author 花生
 * @date 2022-08-24
 */

@RestController
@Api(tags = "财务管理-财务账号配置")
@RequestMapping("/pcompanywalletAddress")
public class PCompanyWalletAddressController extends BaseController
{
    @Autowired
    private IPCompanyWalletAddressService pCompanyWalletAddressService;

    /**
     * 查询公链钱包列表
     */
//    @RequiresPermissions("gen:address:list")
    @GetMapping("/list")
    @ApiImplicitParams({
            @ApiImplicitParam(name="pageNum",value="页码",dataTypeClass = Long.class, paramType = "query",example="1"),
            @ApiImplicitParam(name="pageSize",value="页大小",dataTypeClass = Long.class, paramType = "query",example="10")
    })
    @ApiOperation(value = "查询公链钱包列表",notes = "查询公链钱包列表")
    public TableDataInfo list(PCompanyWalletAddress pCompanyWalletAddress)
    {
        startPage();
        List<PCompanyWalletAddress> list = pCompanyWalletAddressService.selectPCompanyWalletAddressList(pCompanyWalletAddress);
        return getDataTable(list);
    }

//    /**
//     * 导出公链钱包列表
//     */
////    @RequiresPermissions("gen:address:export")
//    @Log(title = "公链钱包", businessType = BusinessType.EXPORT)
//    @PostMapping("/export")
//    @ApiOperation(value = "查询交易类型列表",notes = "查询交易类型列表")
//    public void export(HttpServletResponse response, PCompanyWalletAddress pCompanyWalletAddress)
//    {
//        List<PCompanyWalletAddress> list = pCompanyWalletAddressService.selectPCompanyWalletAddressList(pCompanyWalletAddress);
//        ExcelUtil<PCompanyWalletAddress> util = new ExcelUtil<PCompanyWalletAddress>(PCompanyWalletAddress.class);
//        util.exportExcel(response, list, "公链钱包数据");
//    }

    /**
     * 获取公链钱包详细信息
     */
//    @RequiresPermissions("gen:address:query")
    @GetMapping(value = "getInfo/{id}")
    @ApiOperation(value = "获取公链钱包详细信息",notes = "获取公链钱包详细信息")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(pCompanyWalletAddressService.selectPCompanyWalletAddressById(id));
    }

    /**
     * 新增公链钱包
     */
//    @RequiresPermissions("gen:address:add")
    @Log(title = "公链钱包", businessType = BusinessType.INSERT)
    @PostMapping("add")
    @ApiOperation(value = "新增公链钱包",notes = "新增公链钱包")
    public AjaxResult add(@RequestBody PCompanyWalletAddress pCompanyWalletAddress)
    {
        return toAjax(pCompanyWalletAddressService.insertPCompanyWalletAddress(pCompanyWalletAddress));
    }

    /**
     * 修改公链钱包
     */
//    @RequiresPermissions("gen:address:edit")
    @Log(title = "公链钱包", businessType = BusinessType.UPDATE)
    @PostMapping("edit")
    @ApiOperation(value = "修改公链钱包",notes = "修改公链钱包")
    public AjaxResult edit(@RequestBody PCompanyWalletAddress pCompanyWalletAddress)
    {
        return toAjax(pCompanyWalletAddressService.updatePCompanyWalletAddress(pCompanyWalletAddress));
    }

    /**
     * 删除公链钱包
     */
//    @RequiresPermissions("gen:address:remove")
    @Log(title = "公链钱包", businessType = BusinessType.DELETE)
	@GetMapping("remove/{ids}")
    @ApiOperation(value = "删除公链钱包",notes = "删除公链钱包")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(pCompanyWalletAddressService.deletePCompanyWalletAddressByIds(ids));
    }
}
