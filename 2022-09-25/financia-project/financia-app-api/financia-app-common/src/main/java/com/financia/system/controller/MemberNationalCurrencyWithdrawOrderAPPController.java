package com.financia.system.controller;

import com.financia.common.core.web.controller.BaseController;
import com.financia.common.core.web.domain.AjaxResult;
import com.financia.common.core.web.page.TableDataInfo;
import com.financia.common.log.annotation.Log;
import com.financia.common.log.enums.BusinessType;
import com.financia.exchange.MemberNationalCurrencyWithdrawOrder;
import com.financia.system.service.IMemberNationalCurrencyWithdrawOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 基础业务-会员法币提现记录Controller
 * 
 * @author 花生
 * @date 2022-09-22
 */
@RestController
@RequestMapping("/memberNationalCurrencyWithdrawOrderApp")
@Api(tags = "APP法币提现模块")
@Slf4j
public class MemberNationalCurrencyWithdrawOrderAPPController extends BaseController
{
    @Autowired
    private IMemberNationalCurrencyWithdrawOrderService memberNationalCurrencyWithdrawOrderService;

    /**
     * 查询基础业务-会员法币提现记录列表
     */
    @GetMapping("/list")
    @ApiOperation(value = "会员法币提现记录列表",notes = "会员法币提现记录列表")
    public TableDataInfo list(MemberNationalCurrencyWithdrawOrder memberNationalCurrencyWithdrawOrder)
    {
        startPage();
        List<MemberNationalCurrencyWithdrawOrder> list = memberNationalCurrencyWithdrawOrderService.selectMemberNationalCurrencyWithdrawOrderList(memberNationalCurrencyWithdrawOrder);
        return getDataTable(list);
    }


    /**
     * 获取基础业务-会员法币提现记录详细信息
     */
    @GetMapping(value = "getInfo/{id}")
    @ApiOperation(value = "会员法币提现记录详情",notes = "会员法币提现记录详情")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(memberNationalCurrencyWithdrawOrderService.selectMemberNationalCurrencyWithdrawOrderById(id));
    }

    /**
     * 创建会员法币提现账单
     * {
     *       "memberId": "100",
     *       "type": 1,
     *       "collectionNumber": "12312313",
     *       "collectionName": "张三",
     *       "bankName": "中国银行",
     *       "money": 15,
     *       "coinId": "1",
     * }
     */
    @PostMapping("add")
    @ApiOperation(value = "创建会员法币提现账单",notes = "创建会员法币提现账单")
    public AjaxResult add(@RequestBody MemberNationalCurrencyWithdrawOrder memberNationalCurrencyWithdrawOrder)
    {
        return toAjax(memberNationalCurrencyWithdrawOrderService.insertMemberNationalCurrencyWithdrawOrder(memberNationalCurrencyWithdrawOrder));
    }



}
