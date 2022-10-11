package com.financia.business.member.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.financia.business.member.service.IMemberNationalCurrencyRechargeOrderAPPService;
import com.financia.common.security.utils.SecurityUtils;
import com.financia.exchange.MemberNationalCurrencyRechargeOrder;
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
 * 基础业务-会员法币充值记录Controller
 * 
 * @author 花生
 * @date 2022-09-19
 */
@RestController
@RequestMapping("/memberNationalCurrencyRechargeOrderAdmin")
@Api(tags="财务管理-会员法币充值记录")
public class MemberNationalCurrencyRechargeOrderAdminController extends BaseController
{
    @Autowired
    private IMemberNationalCurrencyRechargeOrderAPPService memberNationalCurrencyRechargeOrderAPPService;

    /**
     * 查询基础业务-会员法币充值记录列表
     */
//    @RequiresPermissions("gen:order:list")
    @GetMapping("/list")
    @ApiOperation(value = "会员法币充值列表",notes = "会员法币充值列表")
    public TableDataInfo list(MemberNationalCurrencyRechargeOrder memberNationalCurrencyRechargeOrderAPP)
    {
        startPage();
        List<MemberNationalCurrencyRechargeOrder> list = memberNationalCurrencyRechargeOrderAPPService.selectMemberNationalCurrencyRechargeOrderAPPList(memberNationalCurrencyRechargeOrderAPP);
        return getDataTable(list);
    }

//    /**
//     * 导出基础业务-会员法币充值记录列表
//     */
////    @RequiresPermissions("gen:order:export")
//    @Log(title = "基础业务-会员法币充值记录", businessType = BusinessType.EXPORT)
//    @PostMapping("/export")
//    public void export(HttpServletResponse response, MemberNationalCurrencyRechargeOrder memberNationalCurrencyRechargeOrderAPP)
//    {
//        List<MemberNationalCurrencyRechargeOrder> list = memberNationalCurrencyRechargeOrderAPPService.selectMemberNationalCurrencyRechargeOrderAPPList(memberNationalCurrencyRechargeOrderAPP);
//        ExcelUtil<MemberNationalCurrencyRechargeOrder> util = new ExcelUtil<MemberNationalCurrencyRechargeOrder>(MemberNationalCurrencyRechargeOrder.class);
//        util.exportExcel(response, list, "基础业务-会员法币充值记录数据");
//    }

    /**
     * 获取基础业务-会员法币充值记录详细信息
     */
//    @RequiresPermissions("gen:order:query")
    @GetMapping(value = "/{id}")
    @ApiOperation(value = "会员法币充值详情",notes = "会员法币充值详情")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(memberNationalCurrencyRechargeOrderAPPService.selectMemberNationalCurrencyRechargeOrderAPPById(id));
    }

//    /**
//     * 新增基础业务-会员法币充值记录
//     */
////    @RequiresPermissions("gen:order:add")
//    @ApiOperation(value = "会员法币充值",notes = "会员法币充值详情")
//    @Log(title = "基础业务-会员法币充值记录", businessType = BusinessType.INSERT)
//    @PostMapping
//    public AjaxResult add(@RequestBody MemberNationalCurrencyRechargeOrder memberNationalCurrencyRechargeOrderAPP)
//    {
//        return toAjax(memberNationalCurrencyRechargeOrderAPPService.insertMemberNationalCurrencyRechargeOrderAPP(memberNationalCurrencyRechargeOrderAPP));
//    }

    /**
     * 修改基础业务-会员法币充值记录
     */
//    @RequiresPermissions("gen:order:edit")
    @PostMapping("edit")
    @ApiOperation(value = "修改会员法币充值",notes = "修改会员法币充值")
    public AjaxResult edit(@RequestBody MemberNationalCurrencyRechargeOrder memberNationalCurrencyRechargeOrderAPP)
    {
        return toAjax(memberNationalCurrencyRechargeOrderAPPService.updateMemberNationalCurrencyRechargeOrderAPP(memberNationalCurrencyRechargeOrderAPP));
    }

//    /**
//     * 删除基础业务-会员法币充值记录
//     */
////    @RequiresPermissions("gen:order:remove")
//    @Log(title = "基础业务-会员法币充值记录", businessType = BusinessType.DELETE)
//	@DeleteMapping("/{ids}")
//    public AjaxResult remove(@PathVariable Long[] ids)
//    {
//        return toAjax(memberNationalCurrencyRechargeOrderAPPService.deleteMemberNationalCurrencyRechargeOrderAPPByIds(ids));
//    }
}
