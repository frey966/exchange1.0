package com.financia.exchange.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class RabbitConfig {

    /**
     * 声明超级杠杆交换机
     */
    public static final String EXCHANGE_TOPICS_INFORM="exchange_super_leverage_topics_inform";
    /**
     * 声明超级杠杆队列
     */
    public static final String QUEUE_INFORM_KLINE = "queue_super_leverage_inform_kline";
    public static final String QUEUE_INFORM_DETAIL = "queue_super_leverage_inform_detail";


    public static final String ROUTING_KEY_KLINE="inform.#.kline.#";
    public static final String ROUTING_KEY_DETAIL="inform.#.detail.#";

    @Bean(EXCHANGE_TOPICS_INFORM)
    public Exchange EXCHANGE_TOPICS_INFORM(){
        return ExchangeBuilder.topicExchange(EXCHANGE_TOPICS_INFORM).durable(true).build();
    }

    @Bean(QUEUE_INFORM_KLINE)
    public Queue QUEUE_INFORM_KLINE(){
        return new Queue(QUEUE_INFORM_KLINE);
    }

    @Bean(QUEUE_INFORM_DETAIL)
    public Queue QUEUE_INFORM_DETAIL(){
        return new Queue(QUEUE_INFORM_DETAIL);
    }


    @Bean
    public Binding BINDING_QUEUE_INFORM_KLINE(@Qualifier(QUEUE_INFORM_KLINE) Queue queue,
                                              @Qualifier(EXCHANGE_TOPICS_INFORM) Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY_KLINE).noargs();
    }

    @Bean
    public Binding BINDING_QUEUE_INFORM_DETAIL(@Qualifier(QUEUE_INFORM_DETAIL) Queue queue,
                                               @Qualifier(EXCHANGE_TOPICS_INFORM) Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY_DETAIL).noargs();
    }

}
