package com.financia.gen.controller;

import com.financia.gen.domain.GenTable;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags="ไปฃ็ ็ๆ-test")
@RequestMapping("/test")
@RestController
public class TestController {

    @GetMapping("/string")
    public String genList(GenTable genTable)
    {
        return "ok";
    }
}
