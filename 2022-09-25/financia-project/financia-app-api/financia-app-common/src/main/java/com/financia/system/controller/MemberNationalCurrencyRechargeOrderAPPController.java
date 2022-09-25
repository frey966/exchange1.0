package com.financia.system.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.financia.exchange.MemberNationalCurrencyRechargeOrder;
import com.financia.system.service.IMemberNationalCurrencyRechargeOrderAPPService;
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
 * @date 2022-09-18
 */
@Api(tags="APP法币充值模块")
@RestController
@RequestMapping("/memberNationalCurrencyRechargeOrder")
public class MemberNationalCurrencyRechargeOrderAPPController extends BaseController
{
    @Autowired
    private IMemberNationalCurrencyRechargeOrderAPPService memberNationalCurrencyRechargeOrderAPPService;

    /**
     * 查询基础业务-会员法币充值记录列表
     */
    @ApiOperation(value = "会员法币充值账单列表")
    @GetMapping("/list")
    public TableDataInfo list(MemberNationalCurrencyRechargeOrder memberNationalCurrencyRechargeOrderAPP)
    {
        startPage();
        memberNationalCurrencyRechargeOrderAPP.setMemberId(getUserId().toString());
        List<MemberNationalCurrencyRechargeOrder> list = memberNationalCurrencyRechargeOrderAPPService.selectMemberNationalCurrencyRechargeOrderAPPList(memberNationalCurrencyRechargeOrderAPP);
        return getDataTable(list);
    }

    /**
     * 会员法币充值账单详情
     */
    @GetMapping(value = "/{id}")
    @ApiOperation(value = "会员法币充值账单详情")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(memberNationalCurrencyRechargeOrderAPPService.selectMemberNationalCurrencyRechargeOrderAPPById(id));
    }

    /**
     * 创建会员法币账单
     */
    @ApiOperation(value = "创建会员法币账单")
    @PostMapping
    public AjaxResult add(@RequestBody MemberNationalCurrencyRechargeOrder memberNationalCurrencyRechargeOrderAPP)
    {
        return toAjax(memberNationalCurrencyRechargeOrderAPPService.insertMemberNationalCurrencyRechargeOrderAPP(memberNationalCurrencyRechargeOrderAPP));
    }


}
