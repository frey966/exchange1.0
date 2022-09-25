package com.financia.business.member.controller;

import com.financia.business.member.service.MemberWalletNationalCurrencyService;
import com.financia.common.core.constant.UserConstants;
import com.financia.common.core.web.controller.BaseController;
import com.financia.common.core.web.domain.AjaxResult;
import com.financia.common.core.web.page.TableDataInfo;
import com.financia.exchange.Member;
import com.financia.exchange.MemberBusinessWallet;
import com.financia.exchange.MemberWalletNationalCurrency;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
@Api(tags = "会员管理-会员法币钱包")
@RestController
@RequestMapping("walletNationalCurrency")
@Slf4j
public class MemberWalletNationalCurrencyAdminController extends BaseController {

    @Autowired
    private MemberWalletNationalCurrencyService memberWalletNationalCurrencyService;


//    /**
//     * 查询会员-会员法币钱包信息列表
//     */
//    @GetMapping("/list")
//    public TableDataInfo list(MemberWalletNationalCurrency memberWalletNationalCurrency)
//    {
//        startPage();
//        List<MemberWalletNationalCurrency> list = memberWalletNationalCurrencyService.selectMemberWalletNationalCurrencyList(memberWalletNationalCurrency);
//        return getDataTable(list);
//    }

    /**
     * 获取会员法币钱包信息详细信息
     */
    @GetMapping(value = "getInfo/{id}")
    @ApiOperation(value = "会员法币钱包信息", notes = "会员法币钱包信息")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(memberWalletNationalCurrencyService.selectMemberWalletNationalCurrencyById(id));
    }

    /**
     * 获取会员法币钱包信息详细信息
     */
    @PostMapping(value = "getNationalCurrencyByMemberIdAndCoinId")
    @ApiOperation(value = "会员法币钱包信息", notes = "会员法币钱包信息")
    public MemberWalletNationalCurrency getNationalCurrencyByMemberIdAndCoinId(@RequestBody MemberWalletNationalCurrency memberWalletNationalCurrency)
    {
        return memberWalletNationalCurrencyService.selectNationalCurrencyByMemberIdAndCoinId(memberWalletNationalCurrency);
    }

    /**
     * 新增会员法币钱包信息
     */
    @PostMapping("addNationalCurrency")
    @ApiOperation(value = "新增会员法币钱包", notes = "新增会员法币钱包")
    public int addNationalCurrency(@RequestBody MemberWalletNationalCurrency memberWalletNationalCurrency)
    {
        return memberWalletNationalCurrencyService.insertMemberWalletNationalCurrency(memberWalletNationalCurrency);
    }

    /**
     * 会员法币钱包增加余额
     */
    @PostMapping("addMoney")
    @ApiOperation(value = "会员法币钱包增加余额", notes = "会员法币钱包增加余额")
    public int addMoney(@RequestBody MemberWalletNationalCurrency memberWalletNationalCurrency)
    {
        return memberWalletNationalCurrencyService.addMoney(memberWalletNationalCurrency);
    }

    /**
     * 会员法币钱包减少余额
     */
    @PostMapping("subMoney")
    @ApiOperation(value = "会员法币钱包减少余额", notes = "会员法币钱包减少余额")
    public int subMoney(@RequestBody MemberWalletNationalCurrency memberWalletNationalCurrency)
    {
        return memberWalletNationalCurrencyService.subMoney(memberWalletNationalCurrency);
    }



}
