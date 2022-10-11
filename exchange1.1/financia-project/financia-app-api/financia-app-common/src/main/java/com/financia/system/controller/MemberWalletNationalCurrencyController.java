package com.financia.system.controller;

import com.dtflys.forest.annotation.Post;
import com.financia.common.core.constant.UserConstants;
import com.financia.common.core.web.controller.BaseController;
import com.financia.common.core.web.domain.AjaxResult;
import com.financia.common.core.web.page.TableDataInfo;
import com.financia.exchange.Member;
import com.financia.exchange.MemberBusinessWallet;
import com.financia.exchange.MemberWalletNationalCurrency;
import com.financia.system.service.IMemberService;
import com.financia.system.service.MemberBusinessWalletService;
import com.financia.system.service.MemberLoginService;
import com.financia.system.service.MemberWalletNationalCurrencyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
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
@Api(tags = "APP会员法币钱包")
@RestController
@RequestMapping("/member/walletNationalCurrency")
@Slf4j
public class MemberWalletNationalCurrencyController extends BaseController {

    @Autowired
    private MemberWalletNationalCurrencyService memberWalletNationalCurrencyService;
    @Autowired
    private MemberLoginService memberLoginService;
    @Autowired
    private IMemberService memberService;


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

    /**
     * 验证交易密码
     * {
     *     "memberId":100,
     *     "jyPassword":123
     * }
     */
    @PostMapping("verifyJyPassword")
    @ApiOperation(value = "验证交易密码", notes = "验证交易密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "memberId", value = "会员id", dataTypeClass = Long.class),
            @ApiImplicitParam(name="jyPassword",value="交易密码",dataTypeClass = String.class)

    })
    public Map verifyJyPassword(@RequestParam("memberId") Long memberId,@RequestParam("jyPassword") String jyPassword)
    {
        Map resultMap=new HashMap();
        Member member = memberService.getById(memberId);
        if(ObjectUtils.isEmpty(member.getJyPassword())){
            resultMap.put("msg","未设置密码！");
            resultMap.put("code","-3");
        }else {
            //查询错误次数
            int count=memberLoginService.verifyJyPasswordFailureCount(memberId);
            if (count <= UserConstants.JY_PASSWORD_LOGIN_MAX){
                Boolean isok=memberLoginService.verifyJyPassword(memberId,jyPassword);
                if(isok){
                    resultMap.put("msg","密码正确");
                    resultMap.put("code","200");
                }else{
                    resultMap.put("count",count+1);
                    resultMap.put("msg","密码错误");
                    resultMap.put("code","-1");
                }
            }else{
                resultMap.put("count",count);
                resultMap.put("code","-2");
                resultMap.put("msg","错误超过"+UserConstants.JY_PASSWORD_LOGIN_MAX);
            }
        }
        return resultMap;
    }

}
