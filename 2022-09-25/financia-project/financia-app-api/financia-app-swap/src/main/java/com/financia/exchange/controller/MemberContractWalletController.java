package com.financia.exchange.controller;

import com.financia.exchange.service.MemberContractWalletService;
import com.financia.swap.MemberContractWallet;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

/**
 * 合约钱包接口
 */
@Slf4j
@RestController
@RequestMapping("/contractWallet")
@Api(tags = "合约钱包接口")
public class MemberContractWalletController {


    @Autowired
    private MemberContractWalletService memberContractWalletService;



    /**
     * 获取用户合约钱包保证金总额(feign使用)
     * @param memberId
     * @return
     */
    @GetMapping("total")
    @ApiOperation("获取用户合约钱包保证金总额(feign使用)")
    @ApiImplicitParams({
            @ApiImplicitParam(name="memberId",value="会员Id",dataType="long", paramType = "query",example="1", required = true),
    })
    public BigDecimal total(Long memberId) {
        // 查询合约钱包信息
        List<MemberContractWallet> wallets = memberContractWalletService.findAllByMemberId(memberId);
        return wallets.stream().filter(r -> ObjectUtils.isNotEmpty(r.getUsdtBuyPrincipalAmount())).map(MemberContractWallet::getUsdtBuyPrincipalAmount).reduce(BigDecimal.ZERO,BigDecimal::add);
    }

}
