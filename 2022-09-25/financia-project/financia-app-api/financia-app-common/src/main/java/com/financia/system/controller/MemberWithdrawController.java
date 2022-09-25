package com.financia.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.financia.common.core.constant.ExchangeConstants;
import com.financia.common.core.enums.DataStatus;
import com.financia.common.core.enums.WalletType;
import com.financia.common.core.utils.StringUtils;
import com.financia.common.core.web.controller.BaseController;
import com.financia.common.core.web.domain.AjaxResult;
import com.financia.common.redis.service.RedisService;
import com.financia.exchange.MemberBusinessWallet;
import com.financia.exchange.MemberWalletWithdrawAddress;
import com.financia.system.service.MemberBusinessWalletService;
import com.financia.system.service.MemberWalletWithdrawAddressService;
import com.financia.system.service.MemberWithdrawOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "APP提现模块")
@RestController
@RequestMapping("/member")
public class MemberWithdrawController extends BaseController {

    @Autowired
    private MemberWithdrawOrderService withdrawOrderService;

    @Autowired
    private MemberWalletWithdrawAddressService walletWithdrawAddressService;

    @Autowired
    private MemberBusinessWalletService businessWalletService;

    @Autowired
    private RedisService redisService;

    @PostMapping(value = "/withdraw/addAddress")
    @ApiOperation(value = "添加提现地址", notes = "添加提现地址 请求: {'chainId': '链类型ID',address: '公链地址'}")
    public AjaxResult addAddress(@RequestBody Map<String, String> param) {
        Long userId = getUserId();
        if (param.get("chainId") == null || StringUtils.isEmpty(param.get("chainId"))) {
            return error("chainId is  not null");
        }
        if (param.get("address") == null || StringUtils.isEmpty(param.get("address"))) {
            return error("address is  not null");
        }
        if (param.get("remark") == null || StringUtils.isEmpty(param.get("remark"))) {
            return error("remark is  not null");
        }
        if (createWalletWithdrawAddress(param, userId)) {
            return success();
        }
        return error(" error ");
    }


    @PostMapping(value = "/withdraw/apply")
    @ApiOperation(value = "提现申请", notes = "提现申请 请求: {'remark': '备注',money: '金额',jyPassword: '交易密码', 'address': '地址','chainId': '链类型ID'}")
    public AjaxResult apply(@RequestBody Map<String, String> param) {
        Long userId = getUserId();
        if (param.get("money") == null || StringUtils.isEmpty(param.get("money"))) {
            return error("money is  not null");
        }
        if (param.get("jyPassword") == null || StringUtils.isEmpty(param.get("jyPassword"))) {
            return error("jyPassword is  not null");
        }
        if (param.get("chainId") == null || StringUtils.isEmpty(param.get("chainId"))) {
            return error("chain is  not null");
        }
        if (param.get("address") == null || StringUtils.isEmpty(param.get("address"))) {
            return error("chain is  not null");
        }
        BigDecimal bigDecimal = new BigDecimal(param.get("money"));
//        int count = walletWithdrawAddressService.count(new QueryWrapper<MemberWalletWithdrawAddress>()
//                .lambda()
//                .eq(MemberWalletWithdrawAddress::getAddress, param.get("address"))
//                .eq(MemberWalletWithdrawAddress::getChainId, param.get("chainId")));
//        if (count == 0){
//            createWalletWithdrawAddress(param, userId);
//        }
        String key = "enableTrade" + userId;
        Boolean enableTrade = redisService.getCacheObject(key);
        if (enableTrade == null) {
            AjaxResult apply = withdrawOrderService.apply(userId, param.get("remark"), bigDecimal,
                    param.get("jyPassword"), Integer.parseInt(param.get("chainId")), param.get("address"));
            return apply;
        } else {
            return error("After 24 hours of changing the payment password, it cannot be reproduced from");

        }

    }

    private boolean createWalletWithdrawAddress(Map<String, String> param, Long userId) {
        MemberWalletWithdrawAddress memberWalletWithdrawAddress = new MemberWalletWithdrawAddress();
        memberWalletWithdrawAddress.setAddress(param.get("address"));
        memberWalletWithdrawAddress.setMemberId(userId);
        memberWalletWithdrawAddress.setRemark(param.get("remark"));
        memberWalletWithdrawAddress.setChainId(Integer.parseInt(param.get("chainId")));
        memberWalletWithdrawAddress.setSumMoney(new BigDecimal(0));
        memberWalletWithdrawAddress.setTradeCount(0);
        memberWalletWithdrawAddress.setStatus(DataStatus.VALID.getCode());
        return walletWithdrawAddressService.save(memberWalletWithdrawAddress);
    }

    @GetMapping(value = "/withdraw/addressList")
    @ApiOperation(value = "提现地址列表", notes = "提现地址列表")
    public AjaxResult addressList(Integer id) {
        Long userId = getUserId();
        List<MemberWalletWithdrawAddress> list = walletWithdrawAddressService.list(new QueryWrapper<MemberWalletWithdrawAddress>()
                .lambda()
                .eq(MemberWalletWithdrawAddress::getMemberId, userId)
                .eq(MemberWalletWithdrawAddress::getChainId, id).eq(MemberWalletWithdrawAddress::getStatus, DataStatus.VALID.getCode()));
        return AjaxResult.success(list);
    }

    @GetMapping(value = "/withdraw/balance")
    @ApiOperation(value = "余额查询", notes = "余额查询")
    public AjaxResult balance() {
        Long userId = getUserId();
        MemberBusinessWallet one = businessWalletService.getOne(new QueryWrapper<MemberBusinessWallet>()
                .lambda()
                .eq(MemberBusinessWallet::getMemberId, userId)
                .eq(MemberBusinessWallet::getType, WalletType.BALANCE.getCode()));
        BigDecimal money = one.getMoney();
        return AjaxResult.success(money);
    }

    @GetMapping(value = "/withdraw/procedureRate")
    @ApiOperation(value = "手续费查询", notes = "手续费查询")
    public AjaxResult procedureRate() {
        Map<String, BigDecimal> rs = new HashMap<>();
        rs.put("rate", ExchangeConstants.WITHDRAWAL_PROCEDURE_RATE);
        rs.put("mini", ExchangeConstants.WITHDRAWAL_PROCEDURE_MINI);
        return AjaxResult.success(rs);
    }

    @PostMapping(value = "/withdraw/addressDelete")
    @ApiOperation(value = "删除", notes = "删除")
    public AjaxResult addressDelete(@RequestBody Map<String, Integer> param) {
        MemberWalletWithdrawAddress update = new MemberWalletWithdrawAddress();
        update.setId(param.get("id"));
        update.setStatus(DataStatus.DELETED.getCode());
        boolean b = walletWithdrawAddressService.updateById(update);
        if (b) {
            return AjaxResult.success();
        }
        return error("No data");
    }


}
