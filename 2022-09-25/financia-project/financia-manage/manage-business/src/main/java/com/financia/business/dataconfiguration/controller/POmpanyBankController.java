package com.financia.business.dataconfiguration.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.financia.business.dataconfiguration.service.IPOmpanyBankService;
import com.financia.exchange.POmpanyBank;
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
 * 公司收款银行卡Controller
 * 
 * @author 花生
 * @date 2022-09-18
 */
@Api(tags="财务管理-公司银行卡设置")
@RestController
@RequestMapping("/pOmpanyBank")
public class POmpanyBankController extends BaseController
{
    @Autowired
    private IPOmpanyBankService pOmpanyBankService;

    /**
     * 查询公司收款银行卡列表
     */
//    @RequiresPermissions("gen:bank:list")
    @GetMapping("/list")
    @ApiOperation(value = "公司收款银行卡列表",notes = "公司收款银行卡列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name="pageNum",value="页码",dataTypeClass = Long.class, paramType = "query",example="1"),
            @ApiImplicitParam(name="pageSize",value="页大小",dataTypeClass = Long.class, paramType = "query",example="10")
    })
    public TableDataInfo list(POmpanyBank pOmpanyBank)
    {
        startPage();
        List<POmpanyBank> list = pOmpanyBankService.selectPOmpanyBankList(pOmpanyBank);
        return getDataTable(list);
    }

//    /**
//     * 导出公司收款银行卡列表
//     */
////    @RequiresPermissions("gen:bank:export")
//    @Log(title = "公司收款银行卡", businessType = BusinessType.EXPORT)
//    @PostMapping("/export")
//    public void export(HttpServletResponse response, POmpanyBank pOmpanyBank)
//    {
//        List<POmpanyBank> list = pOmpanyBankService.selectPOmpanyBankList(pOmpanyBank);
//        ExcelUtil<POmpanyBank> util = new ExcelUtil<POmpanyBank>(POmpanyBank.class);
//        util.exportExcel(response, list, "公司收款银行卡数据");
//    }

    /**
     * 获取公司收款银行卡详细信息
     */
//    @RequiresPermissions("gen:bank:query")
    @GetMapping(value = "getInfo/{id}")
    @ApiOperation(value = "公司收款银行卡详细信息",notes = "公司收款银行卡详细信息")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(pOmpanyBankService.selectPOmpanyBankById(id));
    }

    /**
     * 新增公司收款银行卡
     */
//    @RequiresPermissions("gen:bank:add")
    @Log(title = "公司收款银行卡", businessType = BusinessType.INSERT)
    @PostMapping("add")
    @ApiOperation(value = "新增公司收款银行卡",notes = "新增公司收款银行卡")
    public AjaxResult add(@RequestBody POmpanyBank pOmpanyBank)
    {
        return toAjax(pOmpanyBankService.insertPOmpanyBank(pOmpanyBank));
    }

    /**
     * 修改公司收款银行卡
     */
    @RequiresPermissions("gen:bank:edit")
    @Log(title = "公司收款银行卡", businessType = BusinessType.UPDATE)
    @PostMapping("edit")
    @ApiOperation(value = "修改公司收款银行卡",notes = "修改公司收款银行卡")
    public AjaxResult edit(@RequestBody POmpanyBank pOmpanyBank)
    {
        return toAjax(pOmpanyBankService.updatePOmpanyBank(pOmpanyBank));
    }

    /**
     * 删除公司收款银行卡
     */
    @RequiresPermissions("gen:bank:remove")
    @Log(title = "公司收款银行卡", businessType = BusinessType.DELETE)
	@GetMapping("remove/{ids}")
    @ApiOperation(value = "删除公司收款银行卡",notes = "删除公司收款银行卡")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(pOmpanyBankService.deletePOmpanyBankByIds(ids));
    }
}
