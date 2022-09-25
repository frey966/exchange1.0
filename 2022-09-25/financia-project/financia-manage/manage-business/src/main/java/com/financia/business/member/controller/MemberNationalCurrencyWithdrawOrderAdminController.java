package com.financia.business.member.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.financia.business.member.service.IMemberNationalCurrencyWithdrawOrderService;
import com.financia.common.security.utils.SecurityUtils;
import com.financia.exchange.MemberNationalCurrencyWithdrawOrder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
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
 * 基础业务-会员法币提现记录Controller
 * 
 * @author 花生
 * @date 2022-09-22
 */
@RestController
@RequestMapping("/memberNationalCurrencyWithdrawOrder")
@Api(tags = "财务管理-会员法币提现记录")
@Slf4j
public class MemberNationalCurrencyWithdrawOrderAdminController extends BaseController
{
    @Autowired
    private IMemberNationalCurrencyWithdrawOrderService memberNationalCurrencyWithdrawOrderService;

    /**
     * 查询基础业务-会员法币提现记录列表
     */
//    @RequiresPermissions("gen:order:list")
    @GetMapping("/list")
    @ApiOperation(value = "会员法币提现记录列表",notes = "会员法币提现记录列表")
    public TableDataInfo list(MemberNationalCurrencyWithdrawOrder memberNationalCurrencyWithdrawOrder)
    {
        startPage();
        List<MemberNationalCurrencyWithdrawOrder> list = memberNationalCurrencyWithdrawOrderService.selectMemberNationalCurrencyWithdrawOrderList(memberNationalCurrencyWithdrawOrder);
        return getDataTable(list);
    }

//    /**
//     * 导出基础业务-会员法币提现记录列表
//     */
//    @RequiresPermissions("gen:order:export")
//    @Log(title = "基础业务-会员法币提现记录", businessType = BusinessType.EXPORT)
//    @PostMapping("/export")
//    public void export(HttpServletResponse response, MemberNationalCurrencyWithdrawOrder memberNationalCurrencyWithdrawOrder)
//    {
//        List<MemberNationalCurrencyWithdrawOrder> list = memberNationalCurrencyWithdrawOrderService.selectMemberNationalCurrencyWithdrawOrderList(memberNationalCurrencyWithdrawOrder);
//        ExcelUtil<MemberNationalCurrencyWithdrawOrder> util = new ExcelUtil<MemberNationalCurrencyWithdrawOrder>(MemberNationalCurrencyWithdrawOrder.class);
//        util.exportExcel(response, list, "基础业务-会员法币提现记录数据");
//    }

    /**
     * 获取基础业务-会员法币提现记录详细信息
     */
//    @RequiresPermissions("gen:order:query")
    @GetMapping(value = "getInfo/{id}")
    @ApiOperation(value = "会员法币提现记录详情",notes = "会员法币提现记录详情")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(memberNationalCurrencyWithdrawOrderService.selectMemberNationalCurrencyWithdrawOrderById(id));
    }

//    /**
//     * 新增基础业务-会员法币提现记录
//     */
////    @RequiresPermissions("gen:order:add")
//    @Log(title = "基础业务-会员法币提现记录", businessType = BusinessType.INSERT)
//    @PostMapping
//    @ApiOperation(value = "会员法币提现记录新增",notes = "会员法币提现记录新增")
//    public AjaxResult add(@RequestBody MemberNationalCurrencyWithdrawOrder memberNationalCurrencyWithdrawOrder)
//    {
//        return toAjax(memberNationalCurrencyWithdrawOrderService.insertMemberNationalCurrencyWithdrawOrder(memberNationalCurrencyWithdrawOrder));
//    }

    /**
     * 修改基础业务-会员法币提现记录
     */
//    @RequiresPermissions("gen:order:edit")
    @Log(title = "基础业务-会员法币提现记录", businessType = BusinessType.UPDATE)
    @PostMapping("edit")
    @ApiOperation(value = "修改法币提现（审核）",notes = "修改法币提现（审核）")
    public AjaxResult edit(@RequestBody MemberNationalCurrencyWithdrawOrder memberNationalCurrencyWithdrawOrder)
    {
        return toAjax(memberNationalCurrencyWithdrawOrderService.updateMemberNationalCurrencyWithdrawOrder(memberNationalCurrencyWithdrawOrder));
    }

//    /**
//     * 删除基础业务-会员法币提现记录
//     */
////    @RequiresPermissions("gen:order:remove")
//    @Log(title = "基础业务-会员法币提现记录", businessType = BusinessType.DELETE)
//	@GetMapping("remove/{ids}")
//    public AjaxResult remove(@PathVariable Long[] ids)
//    {
//        return toAjax(memberNationalCurrencyWithdrawOrderService.deleteMemberNationalCurrencyWithdrawOrderByIds(ids));
//    }
}
