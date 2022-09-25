package com.financia.system.controller;

import com.financia.system.domain.dto.PlaceOrderRequest;
import com.financia.system.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/order")
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/placeOrder")
    //public String placeOrder(@Validated @RequestBody PlaceOrderRequest request)
    public String placeOrder(@RequestBody PlaceOrderRequest request)
    {
        log.info("------------begin---------------------");
        orderService.placeOrder(request);
        return "下单成功";
    }

    @PostMapping("/test1")
    @ApiOperation("测试商品库存不足-异常回滚")
    public String test1()
    {
        // 商品单价10元，库存20个,用户余额50元，模拟一次性购买22个。 期望异常回滚
        orderService.placeOrder(new PlaceOrderRequest(1L, 1L, 22));
        return "下单成功";
    }

    @PostMapping("/test2")
    @ApiOperation("测试用户账户余额不足-异常回滚")
    public String test2()
    {
        // 商品单价10元，库存20个，用户余额50元，模拟一次性购买6个。 期望异常回滚
        orderService.placeOrder(new PlaceOrderRequest(1L, 1L, 6));
        return "下单成功";
    }

}
