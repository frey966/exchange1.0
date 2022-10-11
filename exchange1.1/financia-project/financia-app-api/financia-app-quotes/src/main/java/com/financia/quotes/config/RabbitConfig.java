package com.financia.quotes.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    /**
     * 交换机
     */
    public static final String EXCHANGE_TOPICS_TEST = "exchange_topics_test";

    /**
     * 队列
     */
    public static final String QUEUE_TEST = "queue_test";

    /**
     * 路由key
     */
    public static final String ROUTING_KEY_TEST="inform.#.test.#";


    @Bean(EXCHANGE_TOPICS_TEST)
    public Exchange EXCHANGE_TOPICS_TEST(){
        return ExchangeBuilder.topicExchange(EXCHANGE_TOPICS_TEST).durable(true).build();
    }

    @Bean(QUEUE_TEST)
    public Queue QUEUE_TEST(){
        return new Queue(QUEUE_TEST);
    }

    @Bean
    public Binding BINDING_QUEUE_TEST(@Qualifier(QUEUE_TEST) Queue queue,
                                              @Qualifier(EXCHANGE_TOPICS_TEST) Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY_TEST).noargs();
    }

}
