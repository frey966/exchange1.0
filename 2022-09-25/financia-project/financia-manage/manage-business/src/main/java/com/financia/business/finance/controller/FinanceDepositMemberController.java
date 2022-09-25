package com.financia.business.finance.controller;

import java.util.List;

import com.financia.business.finance.service.IFinanceDepositMemberService;
import com.financia.finance.FinanceDepositMember;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.financia.common.log.annotation.Log;
import com.financia.common.log.enums.BusinessType;
import com.financia.common.core.web.controller.BaseController;
import com.financia.common.core.web.domain.AjaxResult;
import com.financia.common.core.web.page.TableDataInfo;

/**
 * 量化理财-会员存款Controller
 * 
 * @author 花生
 * @date 2022-08-15
 */
@Api(tags = "量化理财-会员存款")
@RestController
@RequestMapping("/financedepositmember")
public class FinanceDepositMemberController extends BaseController
{
    @Autowired
    private IFinanceDepositMemberService financeDepositMemberService;

    /**
     * 查询量化理财-会员存款列表
     */
//    @RequiresPermissions("gen:member:list")
    @GetMapping("/list")
    @ApiOperation(value = "查询列表",notes = "查询列表")
    public TableDataInfo list(FinanceDepositMember financeDepositMember)
    {
        startPage();
        List<FinanceDepositMember> list = financeDepositMemberService.selectFinanceDepositMemberList(financeDepositMember);
        return getDataTable(list);
    }

//    /**
//     * 导出量化理财-会员存款列表
//     */
//    @RequiresPermissions("gen:member:export")
//    @Log(title = "量化理财-会员存款", businessType = BusinessType.EXPORT)
//    @PostMapping("/export")
//    @ApiOperation(value = "查询列表",notes = "查询列表")
//    public void export(HttpServletResponse response, FinanceDepositMember financeDepositMember)
//    {
//        List<FinanceDepositMember> list = financeDepositMemberService.selectFinanceDepositMemberList(financeDepositMember);
//        ExcelUtil<FinanceDepositMember> util = new ExcelUtil<FinanceDepositMember>(FinanceDepositMember.class);
//        util.exportExcel(response, list, "量化理财-会员存款数据");
//    }

    /**
     * 获取量化理财-会员存款详细信息
     */
//    @RequiresPermissions("gen:member:query")
    @GetMapping(value = "getInfo")
    @ApiOperation(value = "获取详情",notes = "获取详情")
    public AjaxResult getInfo(FinanceDepositMember financeDepositMember)
    {
        return AjaxResult.success(financeDepositMemberService.selectFinanceDepositMemberById(financeDepositMember));
    }

//    /**
//     * 获取量化理财-会员存款详细信息
//     */
////    @RequiresPermissions("gen:member:query")
//    @GetMapping(value = "getInfo/{id}")
//    @ApiOperation(value = "获取详情",notes = "获取详情")
//    public AjaxResult getInfo(@PathVariable("id") Long id)
//    {
//        return AjaxResult.success(financeDepositMemberService.selectFinanceDepositMemberById(id));
//    }

    /**
     * 新增量化理财-会员存款
     */
//    @RequiresPermissions("gen:member:add")
    @Log(title = "量化理财-会员存款", businessType = BusinessType.INSERT)
    @PostMapping("add")
    @ApiOperation(value = "新增",notes = "新增")
    public AjaxResult add(@RequestBody FinanceDepositMember financeDepositMember)
    {
        return toAjax(financeDepositMemberService.insertFinanceDepositMember(financeDepositMember));
    }

    /**
     * 修改量化理财-会员存款
     */
//    @RequiresPermissions("gen:member:edit")
//    @Log(title = "量化理财-会员存款", businessType = BusinessType.UPDATE)
//    @PostMapping("edit")
//    @ApiOperation(value = "修改",notes = "修改")
//    public AjaxResult edit(@RequestBody FinanceDepositMember financeDepositMember)
//    {
//        return toAjax(financeDepositMemberService.updateFinanceDepositMember(financeDepositMember));
//    }

//    /**
//     * 删除量化理财-会员存款
//     */
//    @RequiresPermissions("gen:member:remove")
//    @Log(title = "量化理财-会员存款", businessType = BusinessType.DELETE)
//	@GetMapping("remove/{ids}")
//    @ApiOperation(value = "删除",notes = "删除")
//    public AjaxResult remove(@PathVariable Long[] ids)
//    {
//        return toAjax(financeDepositMemberService.deleteFinanceDepositMemberByIds(ids));
//    }
}
