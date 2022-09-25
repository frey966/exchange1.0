package com.financia.exchange.controller;

import com.alibaba.fastjson.JSONObject;
import com.financia.common.core.web.domain.AjaxResult;
import com.financia.exchange.config.RabbitConfig;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


/**
 * ribbitmq测试控制类
 */
@Api(tags="ribbitmq测试接口")
@RestController
@RequestMapping("/ribbit")
@Slf4j
public class RabbitController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping(value = "/sendMessage1")
    @ApiOperation(value = "发送mq消息",notes = "发送mq消息")
    public AjaxResult sendMessage1(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name","张三");
        jsonObject.put("age",28);
        jsonObject.put("source","rabbit");
        log.info("开始发送MQ消息。。。");
        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_TOPICS_TEST, "inform.test", jsonObject.toJSONString());
        log.info("发送MQ消息成功。。。");
        return AjaxResult.success();
    }
    @GetMapping(value = "/getMessage1")
    @ApiOperation(value = "接收mq消息",notes = "接收mq消息")
    public AjaxResult getMessage1(){
        log.info("接收发送MQ消息。。。");
        Object object = rabbitTemplate.receiveAndConvert("inform.test");
        System.out.println(object.toString());
        log.info("发送MQ消息成功。。。");
        return AjaxResult.success();
    }

    @GetMapping(value = "/sendMessage2")
    @ApiOperation(value = "发送mq消息",notes = "发送mq消息")
    public AjaxResult sendMessage2(){
        Map<String,Object> map = new HashMap<>();
        map.put("key1","message1");
        map.put("key2","message2");
        log.info("开始发送MQ消息。。。");
        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_TOPICS_MAP, "inform.map", map);
        log.info("发送MQ消息成功。。。");
        return AjaxResult.success();
    }

    @GetMapping(value = "/getMessage2")
    @ApiOperation(value = "接收mq消息",notes = "接收mq消息")
    public AjaxResult getMessage(){
        log.info("接收发送MQ消息。。。");
        Object object = rabbitTemplate.receiveAndConvert("inform.map");
        System.out.println(object.getClass());
        log.info("发送MQ消息成功。。。");
        return AjaxResult.success();
    }
}
