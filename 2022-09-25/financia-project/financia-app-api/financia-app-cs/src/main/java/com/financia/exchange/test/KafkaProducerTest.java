package com.financia.exchange.test;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class KafkaProducerTest {

    public static void main(String[] args) {

        // 1. 创建用于连接Kafka的Properties配置
        Properties props = new Properties();
        props.put("bootstrap.servers", "18.142.229.189:9092");
        props.put("acks", "all");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        // 2. 创建一个生产者对象KafkaProducer
        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(props);
        // 3. 调用send发送1-100消息到指定Topic test
        for(int i = 0; i < 100; ++i) {
            try {
                // 获取返回值Future，该对象封装了返回值
                Future<RecordMetadata> future = producer.send(new ProducerRecord<String, String>("test", null, i + ""));
                // 调用一个Future.get()方法等待响应
                future.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

        // 5. 关闭生产者
        producer.close();
    }

}
