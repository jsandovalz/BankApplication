package com.devsu.hackerearth.backend.client.config;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String CLIENT_EXCHANGE="client.exchange";
    public static final String CLIENT_UPDATED_ROUTING_KEY="client.updated";
    public static final String CLIENT_DELETED_ROUTING_KEY="client.deleted";

    @Bean
    public TopicExchange clientExchange() {
        return new TopicExchange(CLIENT_EXCHANGE);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }    
}
