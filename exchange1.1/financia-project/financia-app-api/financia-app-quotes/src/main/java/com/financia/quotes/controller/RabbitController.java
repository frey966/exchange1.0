package com.financia.quotes.controller;

import com.alibaba.fastjson.JSONObject;
import com.financia.common.core.web.domain.AjaxResult;
import com.financia.quotes.config.RabbitConfig;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


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

    @GetMapping(value = "/sendMessage")
    @ApiOperation(value = "发送mq消息",notes = "发送mq消息")
    public AjaxResult sendMessage(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name","张三");
        jsonObject.put("age",28);
        jsonObject.put("source","rabbit");
        log.info("开始发送MQ消息。。。");
        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_TOPICS_TEST, "inform.test", jsonObject.toJSONString());
        log.info("发送MQ消息成功。。。");
        return AjaxResult.success();
    }
}
