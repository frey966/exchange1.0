package com.financia.exchange.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.financia.common.core.web.domain.AjaxResult;
import com.financia.currency.Currency;
import com.financia.currency.MemberCryptoCurrencyWallet;
import com.financia.exchange.MemberBusinessWallet;
import com.financia.exchange.feign.client.MemberService;
import com.financia.exchange.service.*;
import com.financia.exchange.vo.CurrencyWalletVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 币币钱包接口
 */
@Slf4j
@RestController
@RequestMapping("/wallet")
@Api(tags = "币币钱包接口")
public class WalletController {

    @Autowired
    private CurrencyService currencyService;

    @Autowired
    private MemberCurrencyWalletService memberCurrencyWalletService;

    @Autowired
    private MemberBusinessWalletService memberBusinessWalletService;

    @Autowired
    private MemberService memberService;

    /**
     * 获取币币持仓
     * @param memberId
     * @param coinId
     * @return
     */
    @GetMapping("currencyBalance")
    @ApiOperation("获取币币持仓")
    @ApiImplicitParams({
            @ApiImplicitParam(name="memberId",value="会员Id",dataType="long", paramType = "query",example="1", required = true),
            @ApiImplicitParam(name="coinId",value="当前币种Id",dataType="long", paramType = "query",example="1", required = true)
    })
    public AjaxResult currentCoinBalance(Long memberId, Long coinId) {
        // 判断交易对是否存在
        Currency coin = currencyService.getById(coinId);
        if(coin == null) {
            // return MessageResult.error(500, msService.getMessage("TRADING_PAIR_NOT_EXIST"));
            return AjaxResult.error("trading pair does not exist");
        }
        // 判断用户是否存在
//        Member member = memberService.getMemberInfo(memberId);
//        if (member == null) {
//            //return MessageResult.error(500, msService.getMessage("USER_PLACED_ORDER_NOT_EXIST"));
//            return AjaxResult.error("The users does not exist");
//        }
        // 查询当前交易对币币钱包信息
        MemberCryptoCurrencyWallet currentCurrencyWallet = memberCurrencyWalletService.findByCoinIdAndMemberId(memberId, coinId);
        if(currentCurrencyWallet == null) {
            currentCurrencyWallet = new MemberCryptoCurrencyWallet();
            currentCurrencyWallet.setMemberId(memberId);
            currentCurrencyWallet.setCoinId(coinId);
            currentCurrencyWallet.setCoinSymbol(coin.getPair().split("/")[0]);
            currentCurrencyWallet.setBalanceMoney(BigDecimal.ZERO);
            currentCurrencyWallet.setFrezzeMoney(BigDecimal.ZERO);
            currentCurrencyWallet.setStatus(1);
            currentCurrencyWallet.setCreateTime(new Date());
            memberCurrencyWalletService.save(currentCurrencyWallet);
            currentCurrencyWallet = memberCurrencyWalletService.findByCoinIdAndMemberId(memberId, coinId);
        }

        // 获取其他非0持仓
        List<MemberCryptoCurrencyWallet> allCurrencyWalletList = memberCurrencyWalletService.findByMemberId(memberId);
        List<MemberCryptoCurrencyWallet> otherCurrencyWalletList = allCurrencyWalletList.stream().filter(r -> (r.getBalanceMoney().compareTo(BigDecimal.ZERO) > 0 || r.getFrezzeMoney().compareTo(BigDecimal.ZERO) > 0) && r.getCoinId().compareTo(coinId) != 0).collect(Collectors.toList());

        // 封装返回数据
        CurrencyWalletVo currencyWalletVo = new CurrencyWalletVo();
        currencyWalletVo.setOtherCurrencyWalletList(otherCurrencyWalletList);
        currencyWalletVo.setCurrentCurrencyWallet(currentCurrencyWallet);

        return AjaxResult.success(currencyWalletVo);
    }

    /**
     * 获取加密货币余额
     * @param memberId
     * @return
     */
    @GetMapping("cryptoCurrencyBalance")
    @ApiOperation("获取加密货币余额")
    @ApiImplicitParams({
            @ApiImplicitParam(name="memberId",value="会员Id",dataType="long", paramType = "query",example="1", required = true)
    })
    public AjaxResult currentCoinBalance(Long memberId) {
        // 判断用户是否存在
//        Member member = memberService.getMemberInfo(memberId);
//        if (member == null) {
//            //return MessageResult.error(500, msService.getMessage("USER_PLACED_ORDER_NOT_EXIST"));
//            return AjaxResult.error("The users does not exist");
//        }
        // 获取加密货币余额
        List<MemberCryptoCurrencyWallet> allCurrencyWalletList = memberCurrencyWalletService.findByMemberId(memberId);
        return AjaxResult.success(allCurrencyWalletList);
    }

}
