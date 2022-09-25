package com.financia.business.finance.controller;

import java.util.List;

import com.financia.business.finance.service.IFinanceInterestMemberService;
import com.financia.finance.FinanceInterestMember;
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
 * 量化理财-会员存款利息Controller
 * 
 * @author 花生
 * @date 2022-08-15
 */
@Api(tags = "量化理财-会员存款利息")
@RestController
@RequestMapping("/financeinterestmember")
public class FinanceInterestMemberController extends BaseController
{
    @Autowired
    private IFinanceInterestMemberService financeInterestMemberService;

    /**
     * 查询量化理财-会员存款利息列表
     */
//    @RequiresPermissions("gen:member:list")
    @GetMapping("/list")
    @ApiOperation(value = "查询列表",notes = "查询列表")
    public TableDataInfo list(FinanceInterestMember financeInterestMember)
    {
        startPage();
        List<FinanceInterestMember> list = financeInterestMemberService.selectFinanceInterestMemberList(financeInterestMember);
        return getDataTable(list);
    }

//    /**
//     * 导出量化理财-会员存款利息列表
//     */
//    @RequiresPermissions("gen:member:export")
//    @Log(title = "量化理财-会员存款利息", businessType = BusinessType.EXPORT)
//    @PostMapping("/export")
//    @ApiOperation(value = "查询列表",notes = "查询列表")
//    public void export(HttpServletResponse response, FinanceInterestMember financeInterestMember)
//    {
//        List<FinanceInterestMember> list = financeInterestMemberService.selectFinanceInterestMemberList(financeInterestMember);
//        ExcelUtil<FinanceInterestMember> util = new ExcelUtil<FinanceInterestMember>(FinanceInterestMember.class);
//        util.exportExcel(response, list, "量化理财-会员存款利息数据");
//    }

    /**
     * 获取量化理财-会员存款利息详细信息
     */
//    @RequiresPermissions("gen:member:query")
    @GetMapping(value = "getInfo/{id}")
    @ApiOperation(value = "获取详情",notes = "获取详情")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(financeInterestMemberService.selectFinanceInterestMemberById(id));
    }

    /**
     * 新增量化理财-会员存款利息
     */
//    @RequiresPermissions("gen:member:add")
    @Log(title = "量化理财-会员存款利息", businessType = BusinessType.INSERT)
    @PostMapping("add")
    @ApiOperation(value = "新增",notes = "新增")
    public AjaxResult add(@RequestBody FinanceInterestMember financeInterestMember)
    {
        return toAjax(financeInterestMemberService.insertFinanceInterestMember(financeInterestMember));
    }

    /**
     * 修改量化理财-会员存款利息
     */
//    @RequiresPermissions("gen:member:edit")
//    @Log(title = "量化理财-会员存款利息", businessType = BusinessType.UPDATE)
//    @PutMapping("edit")
//    @ApiOperation(value = "修改",notes = "修改")
//    public AjaxResult edit(@RequestBody FinanceInterestMember financeInterestMember)
//    {
//        return toAjax(financeInterestMemberService.updateFinanceInterestMember(financeInterestMember));
//    }

//    /**
//     * 删除量化理财-会员存款利息
//     */
////    @RequiresPermissions("gen:member:remove")
//    @Log(title = "量化理财-会员存款利息", businessType = BusinessType.DELETE)
//	@DeleteMapping("remove/{ids}")
//    @ApiOperation(value = "删除",notes = "删除")
//    public AjaxResult remove(@PathVariable Long[] ids)
//    {
//        return toAjax(financeInterestMemberService.deleteFinanceInterestMemberByIds(ids));
//    }
}
