package com.financia.exchange.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    // 声明交换机和队列
    public static final String QUEUE_INFORM_KLINE = "queue_inform_kline";
    public static final String QUEUE_INFORM_DEPTH = "queue_inform_depth";
    public static final String QUEUE_INFORM_BBO = "queue_inform_bbo";
    public static final String QUEUE_INFORM_TRADE = "queue_inform_trade";
    public static final String QUEUE_INFORM_DETAIL = "queue_inform_detail";

    public static final String EXCHANGE_TOPICS_INFORM="exchange_topics_inform";
    public static final String ROUTING_KEY_KLINE="inform.#.kline.#";
    public static final String ROUTING_KEY_DEPTH="inform.#.depth.#";
    public static final String ROUTING_KEY_BBO="inform.#.bbo.#";
    public static final String ROUTING_KEY_TRADE="inform.#.trade.#";
    public static final String ROUTING_KEY_DETAIL="inform.#.detail.#";


    @Bean(EXCHANGE_TOPICS_INFORM)
    public Exchange EXCHANGE_TOPICS_INFORM(){
        return ExchangeBuilder.topicExchange(EXCHANGE_TOPICS_INFORM).durable(true).build();
    }

    @Bean(QUEUE_INFORM_KLINE)
    public Queue QUEUE_INFORM_KLINE(){
        return new Queue(QUEUE_INFORM_KLINE);
    }

    @Bean(QUEUE_INFORM_DEPTH)
    public Queue QUEUE_INFORM_SMS(){
        return new Queue(QUEUE_INFORM_DEPTH);
    }

    @Bean(QUEUE_INFORM_BBO)
    public Queue QUEUE_INFORM_BBO(){
        return new Queue(QUEUE_INFORM_BBO);
    }

    @Bean(QUEUE_INFORM_TRADE)
    public Queue QUEUE_INFORM_TRADE(){
        return new Queue(QUEUE_INFORM_TRADE);
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
    public Binding BINDING_QUEUE_INFORM_DEPTH(@Qualifier(QUEUE_INFORM_DEPTH) Queue queue,
                                              @Qualifier(EXCHANGE_TOPICS_INFORM) Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY_DEPTH).noargs();
    }

    @Bean
    public Binding BINDING_QUEUE_INFORM_BBO(@Qualifier(QUEUE_INFORM_BBO) Queue queue,
                                            @Qualifier(EXCHANGE_TOPICS_INFORM) Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY_BBO).noargs();
    }

    @Bean
    public Binding BINDING_QUEUE_INFORM_TRADE(@Qualifier(QUEUE_INFORM_TRADE) Queue queue,
                                              @Qualifier(EXCHANGE_TOPICS_INFORM) Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY_TRADE).noargs();
    }

    @Bean
    public Binding BINDING_QUEUE_INFORM_DETAIL(@Qualifier(QUEUE_INFORM_DETAIL) Queue queue,
                                               @Qualifier(EXCHANGE_TOPICS_INFORM) Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY_DETAIL).noargs();
    }

}
