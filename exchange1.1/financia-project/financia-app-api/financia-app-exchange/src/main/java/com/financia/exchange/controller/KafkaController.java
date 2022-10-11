package com.financia.exchange.controller;

import com.alibaba.fastjson.JSONObject;
import com.financia.common.core.web.domain.AjaxResult;
import com.financia.exchange.config.RabbitConfig;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * kafka测试控制类
 */
@Api(tags="kafka测试接口")
@RestController
@RequestMapping("/kafka")
@Slf4j
public class KafkaController {

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @GetMapping(value = "/sendMessage")
    @ApiOperation(value = "发送消息",notes = "发送消息")
    public AjaxResult sendMessage(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name","张三");
        jsonObject.put("age",28);
        jsonObject.put("source","kafka");
        log.info("开始发送Kafka消息。。。");
        kafkaTemplate.send("kafka-test-message", jsonObject.toJSONString());
        log.info("发送Kafka消息成功。。。");
        return AjaxResult.success();
    }
}
