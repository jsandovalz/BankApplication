package com.devsu.hackerearth.backend.client.messaging;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.devsu.hackerearth.backend.client.config.RabbitMQConfig;
import com.devsu.hackerearth.backend.client.model.dto.ClientEventDto;

@Component
public class ClientEventPublisher {
    private final RabbitTemplate rabbitTemplate;
    
    public ClientEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishClientUpdated(ClientEventDto event) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.CLIENT_EXCHANGE, 
            RabbitMQConfig.CLIENT_UPDATED_ROUTING_KEY, event);
        
    }

    public void publishClientDeleted(ClientEventDto event) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.CLIENT_EXCHANGE,
            RabbitMQConfig.CLIENT_DELETED_ROUTING_KEY,event);
    }
}
