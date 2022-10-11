package com.financia.exchange.listener;

import com.alibaba.fastjson.JSONObject;
import com.financia.exchange.config.RabbitConfig;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * ribbitmq消息监听器
 */
@Slf4j
@Component
public class RabbitListener {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    /**
     *  通过json 传递参数
     * @param msg
     * @param message
     * @param channel
     * @throws Exception
     */
    @org.springframework.amqp.rabbit.annotation.RabbitListener(queues = {RabbitConfig.QUEUE_TEST})
    public void receiveMessage(String msg, Message message, Channel channel) throws Exception {
        JSONObject jsonObject = JSONObject.parseObject(msg);
        log.info("-----成功接收到rabbitMQ消息----json-------："+jsonObject.toJSONString());
        // 做些什么....
        messagingTemplate.convertAndSend("/topic/swap/kline1/", msg);
    }

    /**
     * 通过map 传递参数
     * @param msg
     * @param message
     * @param channel
     * @throws Exception
     */
    @org.springframework.amqp.rabbit.annotation.RabbitListener(queues = {RabbitConfig.QUEUE_TEST_MAP})
    public void receiveMessageMap(HashMap msg, Message message, Channel channel) throws Exception {
        System.out.println("----------map----------------"+msg);
        // 做些什么....
    }

}
