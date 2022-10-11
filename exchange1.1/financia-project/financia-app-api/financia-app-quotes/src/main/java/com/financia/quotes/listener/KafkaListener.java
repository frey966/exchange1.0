package com.financia.quotes.listener;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * kafka消息监听器
 */
@Slf4j
@Component
public class KafkaListener {

    @org.springframework.kafka.annotation.KafkaListener(topics = {"test-message"})
    public void receiveMessage(String content) {
        JSONObject jsonObject = JSONObject.parseObject(content);
        log.info("成功接收到kafka消息："+jsonObject.toJSONString());
        // 做些什么....
    }

}
