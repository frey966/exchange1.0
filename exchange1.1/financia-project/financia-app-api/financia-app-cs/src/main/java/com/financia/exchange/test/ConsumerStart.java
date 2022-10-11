package com.financia.exchange.test;

import com.fasterxml.jackson.databind.deser.std.StringDeserializer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class ConsumerStart {

    private static final String brokerList="18.142.229.189:9092";
    private static final String topic="test";
    private static final String groupId="group.demo";
    public static void main(String[] args) {
        try {
            Properties props = new Properties();
            props.put(ConsumerConfig.GROUP_ID_CONFIG,groupId);//分组ID
            //props.put("bootstrap.servers", brokerList);
            props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, brokerList);//broker地址列表
            //props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
            props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());//反序列化器
            //props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
            props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
            KafkaConsumer consumer= new KafkaConsumer<String,String>(props);
            consumer.subscribe(Collections.singletonList(topic));//topic列表
            while (true){
                ConsumerRecords<String,String> records=consumer.poll(Duration.ofMillis(3000));
                for(ConsumerRecord<String,String> record:records){
                    System.out.println(record.topic()+":"+record.offset()+":"+record.value());
                }
            }
        }catch (Exception e){
            e.printStackTrace();

        }
    }

}
