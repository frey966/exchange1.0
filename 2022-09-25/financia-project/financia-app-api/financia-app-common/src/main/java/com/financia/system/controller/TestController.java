package com.financia.system.controller;

import com.financia.system.service.MemberRechargeOrderService;
import com.financia.system.service.MemberWithdrawOrderService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 暂时不能删除
 * 用来测试
 */
@Api(tags="test-网关测试")
@RequestMapping("/test")
@RestController
public class TestController {

    @Autowired
    private MemberRechargeOrderService memberRechargeOrderService;

    @Autowired
    private MemberWithdrawOrderService withdrawOrderService;

    @GetMapping("/string1")
    public String string1()
    {
        return "app-common-ok111";
    }

    @GetMapping("/string2")
    public String string2()
    {
        return "app-common-ok";
    }

    @GetMapping("/updateRecharge")
    public String update()
    {
        memberRechargeOrderService.updateAllUp(1);
        return "成功";
    }
    @GetMapping("/updateAllTransferStatus")
    public String updateAllTransferStatus()
    {
        memberRechargeOrderService.updateAllTransferStatus();
        return "成功";
    }

    @GetMapping("/updateWithdraw")
    public String updateWithdraw()
    {
        withdrawOrderService.updateWithdraw();
        return "成功";
    }

}
