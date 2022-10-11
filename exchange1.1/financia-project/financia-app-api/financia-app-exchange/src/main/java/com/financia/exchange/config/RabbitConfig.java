package com.financia.exchange.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    /**
     * 交换机
     */
    public static final String EXCHANGE_TOPICS_TEST = "exchange_topics_test_02";

    public static final String EXCHANGE_TOPICS_MAP = "exchange_topics_map";

    /**
     * 队列
     */
    public static final String QUEUE_TEST = "queue_test_02";

    public static final String QUEUE_TEST_MAP = "queue_map_01";

    /**
     * 路由key
     */
    public static final String ROUTING_KEY_TEST="inform.#.test.#";

    public static final String ROUTING_KEY_MAP="inform.#.map.#";


    @Bean(EXCHANGE_TOPICS_TEST)
    public Exchange EXCHANGE_TOPICS_TEST(){
        return ExchangeBuilder.topicExchange(EXCHANGE_TOPICS_TEST).durable(true).build();
    }

    @Bean(EXCHANGE_TOPICS_MAP)
    public Exchange EXCHANGE_TOPICS_MAP(){
        return ExchangeBuilder.topicExchange(EXCHANGE_TOPICS_MAP).durable(true).build();
    }

    @Bean(QUEUE_TEST)
    public Queue QUEUE_TEST(){
        return new Queue(QUEUE_TEST);
    }

    @Bean(QUEUE_TEST_MAP)
    public Queue QUEUE_TEST_MAP(){
        return new Queue(QUEUE_TEST_MAP);
    }

    @Bean
    public Binding BINDING_QUEUE_TEST(@Qualifier(QUEUE_TEST) Queue queue, @Qualifier(EXCHANGE_TOPICS_TEST) Exchange exchange)
    {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY_TEST).noargs();
    }

    @Bean
    public Binding BINDING_QUEUE_MAP(@Qualifier(QUEUE_TEST_MAP) Queue queue, @Qualifier(EXCHANGE_TOPICS_MAP) Exchange exchange)
    {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY_MAP).noargs();
    }

}
