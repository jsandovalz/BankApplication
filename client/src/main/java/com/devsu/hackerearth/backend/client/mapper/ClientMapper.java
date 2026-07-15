package com.devsu.hackerearth.backend.client.mapper;

import org.springframework.stereotype.Component;

import com.devsu.hackerearth.backend.client.model.Client;
import com.devsu.hackerearth.backend.client.model.dto.ClientDto;

@Component
public class ClientMapper {

    public ClientDto toDto(Client client) {
		return new ClientDto(client.getId(), client.getDni(), 
            client.getName(), client.getPassword(), client.getGender(),
            client.getAge(),client.getAddress(), client.getPhone(),client.isActive());

	}

	public Client toEntity(ClientDto clientDto) {
		Client client = new Client();
		client.setId(clientDto.getId());
		client.setDni(clientDto.getDni());
		client.setName(clientDto.getName());
		client.setPassword(clientDto.getPassword());
		client.setGender(clientDto.getGender());
		client.setAddress(clientDto.getAddress());
		client.setPhone(clientDto.getPhone());
		client.setActive(clientDto.isActive());
        
		return client;
	}
}
