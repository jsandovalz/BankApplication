package com.devsu.hackerearth.backend.account.messaging;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.devsu.hackerearth.backend.account.config.RabbitMQConfig;
import com.devsu.hackerearth.backend.account.model.ClientCache;
import com.devsu.hackerearth.backend.account.model.dto.ClientEventDto;
import com.devsu.hackerearth.backend.account.repository.ClientCacheRepository;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ClientEventListener {

    private final ClientCacheRepository clientCacheRepository;

    public ClientEventListener(ClientCacheRepository clientCacheRepository ) {
        this.clientCacheRepository = clientCacheRepository;
    }

    @RabbitListener(queues= RabbitMQConfig.CLIENT_QUEUE)
    public void handleClientEvent(ClientEventDto event) {
        clientCacheRepository.save(new ClientCache(event.getClientId(),event.getName(),event.isActive()));
        log.info("Evento de cliente recibido y cacheado: clientId={}, name={}",event.getClientId(),event.getName());
    }

    
}
