package com.financia.gateway.test;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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

    @GetMapping("/string1")
    @ApiOperation(value = "发送mq消息1",notes = "发送mq消息")
    public String string1()
    {
        return "app-exchange-ok";
    }

    @GetMapping("/string2")
    @ApiOperation(value = "发送mq消息2",notes = "发送mq消息")
    public String string2()
    {
        return "app-exchange-ok";
    }
}
