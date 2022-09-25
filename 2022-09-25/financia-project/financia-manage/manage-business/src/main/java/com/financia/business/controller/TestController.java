package com.financia.business.controller;

import io.swagger.annotations.Api;
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
    public String string1()
    {
        return "manage-business-ok";
    }

    @GetMapping("/string2")
    public String string2()
    {
        return "manage-business-ok";
    }
}
