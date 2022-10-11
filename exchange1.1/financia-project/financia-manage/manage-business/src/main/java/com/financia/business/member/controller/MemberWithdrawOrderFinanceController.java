package com.financia.business.member.controller;

import com.financia.business.member.service.IMemberWithdrawOrderService;
import com.financia.common.core.web.controller.BaseController;
import com.financia.common.core.web.domain.AjaxResult;
import com.financia.common.core.web.page.TableDataInfo;
import com.financia.common.log.annotation.Log;
import com.financia.common.log.enums.BusinessType;
import com.financia.exchange.MemberWithdrawOrder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 财务管理-会员提现申请Controller
 * 
 * @author 花生
 * @date 2022-08-11
 */
@RestController
@RequestMapping("/memberwithdraworderFinance")
@Api(tags="财务管理-会员提现记录")
public class MemberWithdrawOrderFinanceController extends BaseController
{
    @Autowired
    private IMemberWithdrawOrderService memberWithdrawOrderService;

    /**
     * 查询交易所-会员提现申请列表
     */
//    @RequiresPermissions("system:order:list")
    @GetMapping("/list")
    @ApiOperation(value = "查询列表",notes = "查询列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name="currPage",value="页码",dataTypeClass = Long.class, paramType = "query",example="1"),
            @ApiImplicitParam(name="pageSize",value="页大小",dataTypeClass = Long.class, paramType = "query",example="10")
    })
    public TableDataInfo list(MemberWithdrawOrder memberWithdrawOrder)
    {
        startPage();
        memberWithdrawOrder.setPcwawithdrowAmount("ok");
        List<MemberWithdrawOrder> list = memberWithdrawOrderService.selectMemberWithdrawOrderList(memberWithdrawOrder);
        return getDataTable(list);
    }

//    /**
//     * 导出交易所-会员提现申请列表
//     */
//    @RequiresPermissions("system:order:export")
//    @Log(title = "交易所-会员提现申请", businessType = BusinessType.EXPORT)
//    @PostMapping("/export")
//    @ApiOperation(value = "查询列表",notes = "查询列表")
//    public void export(HttpServletResponse response, MemberWithdrawOrder memberWithdrawOrder)
//    {
//        List<MemberWithdrawOrder> list = memberWithdrawOrderService.selectMemberWithdrawOrderList(memberWithdrawOrder);
//        ExcelUtil<MemberWithdrawOrder> util = new ExcelUtil<MemberWithdrawOrder>(MemberWithdrawOrder.class);
//        util.exportExcel(response, list, "交易所-会员提现申请数据");
//    }

    /**
     * 获取交易所-会员提现申请详细信息
     */
//    @RequiresPermissions("system:order:query")
    @GetMapping(value = "getInfo/{id}")
    @ApiOperation(value = "获取详细信息",notes = "获取详细信息")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(memberWithdrawOrderService.selectMemberWithdrawOrderById(id));
    }


    /**
     * 修改交易所-会员提现申请
     */
//    @RequiresPermissions("system:order:edit")
    @Log(title = "财务管理-会员提现申请", businessType = BusinessType.UPDATE)
    @PostMapping("edit")
    @ApiOperation(value = "修改",notes = "修改")
    public AjaxResult edit(@RequestBody MemberWithdrawOrder memberWithdrawOrder)
    {
        return toAjax(memberWithdrawOrderService.updateMemberWithdrawOrder(memberWithdrawOrder));
    }

    /**
     * 删除交易所-会员提现申请
     */
//    @RequiresPermissions("system:order:remove")
    @Log(title = "财务管理-会员提现申请", businessType = BusinessType.DELETE)
	@GetMapping("remove/{ids}")
    @ApiOperation(value = "删除",notes = "删除")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(memberWithdrawOrderService.deleteMemberWithdrawOrderByIds(ids));
    }
}
