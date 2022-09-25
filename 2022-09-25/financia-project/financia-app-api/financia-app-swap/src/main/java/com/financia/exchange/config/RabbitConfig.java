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
     * 声明合约U本位交换机
     */
    public static final String EXCHANGE_TOPICS_INFORM="exchange_swap_usdt_topics_inform";
    /**
     * 交易所定时任务消息推送交换机
     */
    public static final String EXCHANGE_INFORM="exchange_xxl_job";
    /**
     * 声明队列
     */
    public static final String QUEUE_INFORM_KLINE = "queue_swap_usdt_inform_kline";
    public static final String QUEUE_INFORM_DEPTH = "queue_swap_usdt_inform_depth";
    public static final String QUEUE_INFORM_TRADE = "queue_swap_usdt_inform_trade";
    public static final String QUEUE_INFORM_DETAIL = "queue_swap_usdt_inform_detail";

    public static final String QUEUE_EXCHANGE_CAPITAL_RATE = "queue_exchange_capital_rate";

    /**
     * 路由key
     */
    public static final String ROUTING_KEY_KLINE="inform.#.kline.#";
    public static final String ROUTING_KEY_DEPTH="inform.#.depth.#";
    public static final String ROUTING_KEY_TRADE="inform.#.trade.#";
    public static final String ROUTING_KEY_DETAIL="inform.#.detail.#";

    public static final String ROUTING_KEY_EXCHANGE_CAPITAL_RATE="exchange_capital_rate_key";

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
    public Binding BINDING_QUEUE_INFORM_TRADE(@Qualifier(QUEUE_INFORM_TRADE) Queue queue,
                                              @Qualifier(EXCHANGE_TOPICS_INFORM) Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY_TRADE).noargs();
    }

    @Bean
    public Binding BINDING_QUEUE_INFORM_DETAIL(@Qualifier(QUEUE_INFORM_DETAIL) Queue queue,
                                               @Qualifier(EXCHANGE_TOPICS_INFORM) Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY_DETAIL).noargs();
    }

    @Bean(EXCHANGE_INFORM)
    public Exchange EXCHANGE_INFORM(){
        return ExchangeBuilder.directExchange(EXCHANGE_INFORM).durable(true).build();
    }

    @Bean(QUEUE_EXCHANGE_CAPITAL_RATE)
    public Queue QUEUE_EXCHANGE_CAPITAL_RATE(){
        return new Queue(QUEUE_EXCHANGE_CAPITAL_RATE);
    }

    @Bean
    public Binding ROUTING_KEY_EXCHANGE_CAPITAL_RATE(@Qualifier(QUEUE_EXCHANGE_CAPITAL_RATE) Queue queue,
                                                     @Qualifier(EXCHANGE_INFORM) Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY_EXCHANGE_CAPITAL_RATE).noargs();
    }

}
