package com.financia.quotes.listener;

import com.alibaba.fastjson.JSONObject;
import com.financia.quotes.config.RabbitConfig;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.stereotype.Component;

/**
 * ribbitmq消息监听器
 */
@Slf4j
@Component
public class RabbitListener {

    @org.springframework.amqp.rabbit.annotation.RabbitListener(queues = {RabbitConfig.QUEUE_TEST})
    public void receiveMessage(String msg, Message message, Channel channel) throws Exception {
        JSONObject jsonObject = JSONObject.parseObject(msg);
        log.info("成功接收到rabbitMQ消息："+jsonObject.toJSONString());
        // 做些什么....
    }

}
