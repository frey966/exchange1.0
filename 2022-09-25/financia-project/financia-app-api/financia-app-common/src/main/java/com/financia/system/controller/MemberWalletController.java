package com.financia.system.controller;

import com.financia.common.core.enums.BusinessSubType;
import com.financia.common.core.web.controller.BaseController;
import com.financia.common.core.web.domain.AjaxResult;
import com.financia.exchange.MemberBusinessWallet;
import com.financia.system.service.MemberBusinessWalletService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 *
 */
@Api(tags = "APP会员钱包模块")
@RestController
@RequestMapping("/member/wallet")
@Slf4j
public class MemberWalletController extends BaseController {

    @Autowired
    private MemberBusinessWalletService businessWalletService;


    @PostMapping(value = "/updateSubBalance")
    @ApiOperation(value = "会员余额扣减", notes = "会员余额扣减")
    public Boolean updateSubBalance(Long memberId, BigDecimal money, String busId, BusinessSubType businessSubType, String mark) {
        boolean b = businessWalletService.updateSubBalance(memberId, money,busId,businessSubType,mark);
        return b;
    }

    @PostMapping(value = "/updateAddBalance")
    @ApiOperation(value = "会员余额增加", notes = "会员余额增加")
    public Boolean updateAddBalance(Long memberId, BigDecimal money, String busId, BusinessSubType businessSubType, String mark) {
        boolean b = businessWalletService.updateAddBalance(memberId, money,busId,businessSubType,mark);
        return b;
    }

    @GetMapping(value = "/convertFiatCurrency")
    @ApiOperation(value = "法币转换", notes = "法币转换")
    public AjaxResult convertFiatCurrency(@RequestParam("amount") BigDecimal amount) {
        Long userId = getUserId();
        return AjaxResult.success(businessWalletService.convertFiatCurrency(userId, amount));
    }

    @GetMapping(value = "/convertFiatCurrencyFeign")
    @ApiOperation(value = "法币转换", notes = "法币转换")
    public BigDecimal convertFiatCurrencyFeign(@RequestParam("amount") BigDecimal amount) {
        Long userId = getUserId();
        return businessWalletService.convertFiatCurrency(userId, amount);
    }


    @PostMapping(value = "/getBalance")
    @ApiOperation(value = "会员余额", notes = "会员余额")
    public AjaxResult getBalance() {
        Long userId = getUserId();
        return AjaxResult.success(businessWalletService.getBalance(userId));
    }

    @PostMapping(value = "/getMemberBusinessWalletByMemberId")
    @ApiOperation(value = "会员充值钱包详情", notes = "会员充值钱包详情")
    public MemberBusinessWallet getMemberBusinessWalletByMemberId(Long memberId) {
        return businessWalletService.getMemberBusinessWalletByMemberId(memberId);
    }

    @PostMapping(value = "/fiaCurrencyDetail")
    @ApiOperation(value = "会员法币详情", notes = "会员法币详情")
    public AjaxResult getFiaCurrencyDetail() {
        Long userId = getUserId();
        return AjaxResult.success(businessWalletService.getFiaCurrencyList(userId));
    }

    @GetMapping(value = "/transferCurrencyList")
    @ApiOperation(value = "会员转账货币列表", notes = "会员转账货币列表")
    public AjaxResult getTransferCurrencyDetail() {
        Long userId = getUserId();
        return AjaxResult.success(businessWalletService.getTransferCurrencyDetail(userId));
    }

    @PostMapping(value = "/getTotalAssets")
    @ApiOperation(value = "会员余额", notes = "会员余额")
    public AjaxResult getTotalAssets() {
        Long userId = getUserId();
        return AjaxResult.success(businessWalletService.getTotalAssets(userId));
    }


}
