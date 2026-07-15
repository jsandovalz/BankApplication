package com.devsu.hackerearth.backend.client.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsu.hackerearth.backend.client.exceptions.DuplicateResourceException;
import com.devsu.hackerearth.backend.client.exceptions.ResourceNotFoundException;
import com.devsu.hackerearth.backend.client.mapper.ClientMapper;
import com.devsu.hackerearth.backend.client.messaging.ClientEventPublisher;
import com.devsu.hackerearth.backend.client.model.Client;
import com.devsu.hackerearth.backend.client.model.dto.ClientDto;
import com.devsu.hackerearth.backend.client.model.dto.ClientEventDto;
import com.devsu.hackerearth.backend.client.model.dto.PartialClientDto;
import com.devsu.hackerearth.backend.client.repository.ClientRepository;

@Service
public class ClientServiceImpl implements ClientService {

	private final ClientRepository clientRepository;
	private final ClientEventPublisher clientEventPublisher ;
	private final ClientMapper clientMapper;

	public ClientServiceImpl(ClientRepository clientRepository, ClientEventPublisher clientEventPublisher,
		ClientMapper clientMapper) {

		this.clientRepository = clientRepository;
		this.clientEventPublisher = clientEventPublisher;
		this.clientMapper = clientMapper;
	}

	@Override
	public List<ClientDto> getAll() {
		// Get all clients
		return clientRepository.findAll().stream()
			.map(clientMapper::toDto)
			.collect(Collectors.toList());
	}

	@Override
	public ClientDto getById(Long id) {
		// Get clients by id
		return clientMapper.toDto(findOrThrow(id));
	}

	@Override
	@Transactional
	public ClientDto create(ClientDto clientDto) {
		// Create client
		if(clientRepository.existsByDni(clientDto.getDni())) {
			throw new DuplicateResourceException("Ya existe un cliente con el DNI: "+clientDto.getDni());
		}
		
		Client client = clientMapper.toEntity(clientDto);
		client.setId(null);

		Client saved = clientRepository.save(client);

		//TODO: COMENTAR SI NO ESTA INSTALADO RABBITMQ
		publishEvent(saved);
		return clientMapper.toDto(saved);
	}

	@Override
	@Transactional
	public ClientDto update(ClientDto clientDto) {
		// Update client
		Client existing = findOrThrow(clientDto.getId());

		existing.setDni(clientDto.getDni());
		existing.setName(clientDto.getName());
		existing.setPassword(clientDto.getPassword());
		existing.setGender(clientDto.getGender());
		existing.setAge(clientDto.getAge());
		existing.setAddress(clientDto.getAddress());
		existing.setPhone(clientDto.getPhone());
		existing.setActive(clientDto.isActive());

		Client saved = clientRepository.save(existing);

		//TODO: COMENTAR SI NO ESTA INSTALADO RABBITMQ
		publishEvent(saved);
		return clientMapper.toDto(saved);
	}

	@Override
	@Transactional
    public ClientDto partialUpdate(Long id, PartialClientDto partialClientDto) {
        // Partial update account
		Client existing = findOrThrow(id);
		existing.setActive(partialClientDto.isActive()); 

		Client saved = clientRepository.save(existing);
		
		//TODO: COMENTAR SI NO ESTA INSTALADO RABBITMQ
		publishEvent(saved);
		return clientMapper.toDto(saved);
    }

	@Override
	@Transactional
	public void deleteById(Long id) {
		// Delete client
		Client existing = findOrThrow(id);
		clientRepository.deleteById(id);
		clientEventPublisher.publishClientDeleted(
			new ClientEventDto(existing.getId(), existing.getName(),false)
		);
	}

	private Client findOrThrow(Long id) {
		return clientRepository.findById(id)
			.orElseThrow(()-> new ResourceNotFoundException("Cliente no encontrado con id: "+id));
	}

	private void publishEvent(Client client) {
		clientEventPublisher.publishClientUpdated(
			new ClientEventDto(client.getId(),client.getName(), client.isActive())
		);
	}
}
