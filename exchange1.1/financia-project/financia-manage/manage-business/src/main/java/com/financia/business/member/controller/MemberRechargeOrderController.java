package com.financia.business.member.controller;

import java.util.List;

import com.financia.business.member.service.IMemberRechargeOrderService;
import com.financia.exchange.MemberRechargeOrder;
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
import com.financia.common.security.annotation.RequiresPermissions;
import com.financia.common.core.web.controller.BaseController;
import com.financia.common.core.web.domain.AjaxResult;
import com.financia.common.core.web.page.TableDataInfo;

/**
 * 交易所-会员充值记录Controller
 * 
 * @author 花生
 * @date 2022-08-11
 */
@RestController
@RequestMapping("/memberrechargeorder")
@Api(tags="会员管理-会员充值记录")
public class MemberRechargeOrderController extends BaseController
{
    @Autowired
    private IMemberRechargeOrderService memberRechargeOrderService;

    /**
     * 查询交易所-会员充值记录列表
     */
//    @RequiresPermissions("system:order:list")
    @GetMapping("/list")
    @ApiOperation(value = "查询列表",notes = "查询列表")
    public TableDataInfo list(MemberRechargeOrder memberRechargeOrder)
    {
        startPage();
        List<MemberRechargeOrder> list = memberRechargeOrderService.selectMemberRechargeOrderList(memberRechargeOrder);
        return getDataTable(list);
    }

//    /**
//     * 导出交易所-会员充值记录列表
//     */
//    @RequiresPermissions("system:order:export")
//    @Log(title = "交易所-会员充值记录", businessType = BusinessType.EXPORT)
//    @PostMapping("/export")
//    @ApiOperation(value = "查询列表",notes = "查询列表")
//    public void export(HttpServletResponse response, MemberRechargeOrder memberRechargeOrder)
//    {
//        List<MemberRechargeOrder> list = memberRechargeOrderService.selectMemberRechargeOrderList(memberRechargeOrder);
//        ExcelUtil<MemberRechargeOrder> util = new ExcelUtil<MemberRechargeOrder>(MemberRechargeOrder.class);
//        util.exportExcel(response, list, "交易所-会员充值记录数据");
//    }

    /**
     * 获取交易所-会员充值记录详细信息
     */
//    @RequiresPermissions("system:order:query")
    @GetMapping(value = "getInfo/{id}")
    @ApiOperation(value = "获取详细信息",notes = "获取详细信息")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(memberRechargeOrderService.selectMemberRechargeOrderById(id));
    }

    /**
     * 新增交易所-会员充值记录
     */
    @RequiresPermissions("system:order:add")
    @Log(title = "交易所-会员充值记录", businessType = BusinessType.INSERT)
    @PostMapping("add")
    @ApiOperation(value = "新增",notes = "新增")
    public AjaxResult add(@RequestBody MemberRechargeOrder memberRechargeOrder)
    {
        return toAjax(memberRechargeOrderService.insertMemberRechargeOrder(memberRechargeOrder));
    }

    /**
     * 修改交易所-会员充值记录
     */
//    @RequiresPermissions("system:order:edit")
    @Log(title = "交易所-会员充值记录", businessType = BusinessType.UPDATE)
    @PostMapping("edit")
    @ApiOperation(value = "修改",notes = "修改")
    public AjaxResult edit(@RequestBody MemberRechargeOrder memberRechargeOrder)
    {
        return toAjax(memberRechargeOrderService.updateMemberRechargeOrder(memberRechargeOrder));
    }

    /**
     * 删除交易所-会员充值记录
     */
//    @RequiresPermissions("system:order:remove")
    @Log(title = "交易所-会员充值记录", businessType = BusinessType.DELETE)
	@GetMapping("remove/{ids}")
    @ApiOperation(value = "删除",notes = "删除")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(memberRechargeOrderService.deleteMemberRechargeOrderByIds(ids));
    }
}
