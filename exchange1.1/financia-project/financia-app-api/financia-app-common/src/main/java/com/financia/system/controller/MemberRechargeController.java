package com.financia.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.financia.common.core.enums.DataStatus;
import com.financia.common.core.enums.RechargeStatus;
import com.financia.common.core.enums.TransStatus;
import com.financia.common.core.web.controller.BaseController;
import com.financia.common.core.web.domain.AjaxResult;
import com.financia.common.core.web.page.TableDataInfo;
import com.financia.common.redis.service.RedisService;
import com.financia.exchange.MemberRechargeOrder;
import com.financia.exchange.MemberWalletAddress;
import com.financia.system.SysDictData;
import com.financia.system.service.MemberRechargeOrderService;
import com.financia.system.service.MemberWalletAddressService;
import com.financia.system.service.SysDictDataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Api(tags="APP充值模块")
@RestController
@RequestMapping("/member")
public class MemberRechargeController extends BaseController {

    @Autowired
    private MemberWalletAddressService walletAddressService;

    @Autowired
    private MemberRechargeOrderService upOrderService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private SysDictDataService sysDictDataService;


    @GetMapping(value = "/recharge/rechargeOrderList")
    @ApiOperation(value = "充值单列表",notes = "充值单列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name="pageNum",value="页码",dataTypeClass = Long.class, paramType = "query",example="1"),
            @ApiImplicitParam(name="pageSize",value="页大小",dataTypeClass = Long.class, paramType = "query",example="10")
    })
    public TableDataInfo rechargeOrderList(Integer id)
    {
        Long userId = getUserId();
        List<MemberRechargeOrder> list = upOrderService.list(new QueryWrapper<MemberRechargeOrder>()
                .lambda()
                .eq(MemberRechargeOrder::getMemberId, userId)
                .eq(MemberRechargeOrder::getStatus, DataStatus.VALID.getCode()));
        return getDataTable(list);
    }

    @GetMapping(value = "/recharge/addressList")
    @ApiOperation(value = "充值地址列表",notes = "充值地址列表")
    public AjaxResult addressList(Integer id)
    {
        Long userId = getUserId();
        List<MemberWalletAddress> list = walletAddressService.list(new QueryWrapper<MemberWalletAddress>()
                .lambda().select(MemberWalletAddress::getAddress,MemberWalletAddress::getChainName,MemberWalletAddress::getTradeCount,MemberWalletAddress::getChainId,MemberWalletAddress::getId)
                .eq(MemberWalletAddress::getMemberId, userId).eq(MemberWalletAddress::getChainId,id));
        return AjaxResult.success(list);
    }

    @PostMapping(value = "/recharge/submit")
    @ApiOperation(value = "充值提交",notes = "充值提交")
    public AjaxResult upSubmit(Long walletAddressId)
    {
        MemberWalletAddress address = walletAddressService.getOne(new QueryWrapper<MemberWalletAddress>()
                .lambda()
                .eq(MemberWalletAddress::getId, walletAddressId)
                .eq(MemberWalletAddress::getMemberId, getUserId()));
        if (address == null) {
            error("walletAddressId error");
        }
        MemberRechargeOrder save = new MemberRechargeOrder();
        save.setMemberId(getUserId());
        save.setChain(address.getChainName());
//        save.setChainId(address.getChainId());
        save.setAddress(address.getAddress());
        save.setAddressId(address.getId());
        save.setStatus(DataStatus.VALID.getCode());
        save.setOrderStatus(TransStatus.unplayed.getCode());
        save.setRechargeStatus(RechargeStatus.unplayed.getCode());
        save.setCount(0);
        boolean rs = upOrderService.save(save);
        if (rs) {
            return AjaxResult.success();
        }
        return error();
    }

    @GetMapping(value = "/recharge/typeList")
    @ApiOperation(value = "公链类型",notes = "公链类型")
    public AjaxResult dictType(){
        String type="ryptocurrency_type";
        String key="app_ryptocurrency_type";
        List<SysDictData> dictDatas =redisService.getCacheObject(key);
        if(dictDatas == null){
            QueryWrapper<SysDictData> objectQueryWrapper = new QueryWrapper<>();
            objectQueryWrapper.lambda().like(SysDictData::getDictType,type);
            dictDatas=sysDictDataService.list(objectQueryWrapper);
            redisService.setCacheObject(key,dictDatas,10l, TimeUnit.MINUTES);
        }
        return AjaxResult.success(dictDatas);
    }
}
