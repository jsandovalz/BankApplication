package com.devsu.hackerearth.backend.account.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration
public class RabbitMQConfig {

    public static final String CLIENT_EXCHANGE="client.exchange";
    public static final String CLIENT_QUEUE="account.client.queue";
    public static final String CLIENT_ROUTING_PATTERN="client.*";

    @Bean
    public TopicExchange clientExchange() {
        return new TopicExchange(CLIENT_EXCHANGE);
    }

    @Bean
    public Queue clientQueue() {
        return new Queue(CLIENT_QUEUE,true);
    }

    @Bean
    public Binding clientBinding(Queue clientQueue, TopicExchange clientExchange) {
        return BindingBuilder.bind(clientQueue).to(clientExchange).with(CLIENT_ROUTING_PATTERN);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
    
}
