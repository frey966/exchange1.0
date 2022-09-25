package com.financia.business.member.controller;

import com.financia.business.member.service.MemberCryptoCurrencyWalletService;
import com.financia.common.core.utils.StringUtils;
import com.financia.common.core.web.controller.BaseController;
import com.financia.common.core.web.domain.AjaxResult;
import com.financia.common.core.web.page.TableDataInfo;
import com.financia.currency.MemberCryptoCurrencyWallet;
import com.financia.exchange.Member;
import icu.mhb.mybatisplus.plugln.core.JoinLambdaWrapper;
import icu.mhb.mybatisplus.plugln.core.JoinWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 会员-币币钱包Controller
 *
 * @author
 * @date
 */
@RestController
@RequestMapping("/membercurrencywallet")
@Api(tags="会员管理-会员信息管理--币币钱包信息")
public class MemberCurrencyWalletController extends BaseController
{

    @Autowired
    private MemberCryptoCurrencyWalletService cryptoCurrencyWalletService;

    /**
     * 币币钱包列表
     */
    @GetMapping("/list")
    @ApiOperation(value = "查询列表",notes = "查询列表")
    public TableDataInfo list(MemberCryptoCurrencyWallet cryptoCurrencyWallet)
    {
        startPage();
        JoinLambdaWrapper<MemberCryptoCurrencyWallet> wrapper = new JoinLambdaWrapper<>(MemberCryptoCurrencyWallet.class);
        JoinWrapper<Member, MemberCryptoCurrencyWallet> joinWrapper = wrapper.leftJoin(Member.class, Member::getId, MemberCryptoCurrencyWallet::getMemberId)
                                                                        .select(Member::getUsername);
        if (cryptoCurrencyWallet.getMemberId() != null){
            wrapper.eq(MemberCryptoCurrencyWallet::getMemberId,cryptoCurrencyWallet.getMemberId());
        }
        if (!StringUtils.isEmpty(cryptoCurrencyWallet.getUsername())) {
            joinWrapper.eq(Member::getUsername,cryptoCurrencyWallet.getUsername());
        }
        wrapper.orderByDesc(MemberCryptoCurrencyWallet::getCreateTime);
        joinWrapper.end();
        List<MemberCryptoCurrencyWallet> list = cryptoCurrencyWalletService.joinList(wrapper,MemberCryptoCurrencyWallet.class);
        return getDataTable(list);
    }

    @ApiOperation(value = "币币钱包详情",notes = "币币钱包详情")
    @GetMapping("/info/{id}")
    public AjaxResult orderInfo(@PathVariable("id") Long id)
    {
        JoinLambdaWrapper<MemberCryptoCurrencyWallet> wrapper = new JoinLambdaWrapper<>(MemberCryptoCurrencyWallet.class);
        wrapper.leftJoin(Member.class,Member::getId, MemberCryptoCurrencyWallet::getMemberId)
               .select(Member::getUsername).end();
        wrapper.eq(MemberCryptoCurrencyWallet::getId,id);
        MemberCryptoCurrencyWallet memberCryptoCurrencyWallet = cryptoCurrencyWalletService.joinGetOne(wrapper, MemberCryptoCurrencyWallet.class);
        return AjaxResult.success(memberCryptoCurrencyWallet);
    }
}
