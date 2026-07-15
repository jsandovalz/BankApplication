package com.devsu.hackerearth.backend.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.devsu.hackerearth.backend.client.controller.ClientController;
import com.devsu.hackerearth.backend.client.model.Client;
import com.devsu.hackerearth.backend.client.model.dto.ClientDto;
import com.devsu.hackerearth.backend.client.repository.ClientRepository;
import com.devsu.hackerearth.backend.client.service.ClientService;

@SpringBootTest
public class sampleTest {

	private ClientService clientService = mock(ClientService.class);
	private ClientController clientController = new ClientController(clientService);

    @Autowired
    private ClientService realClientService;

    @Autowired
    private ClientRepository clientRepository;

    @Test
    void createClientTest() {
        // Arrange
        ClientDto newClient = new ClientDto(1L, "Dni", "Name", "Password", "Gender", 1, "Address", "9999999999", true);
        ClientDto createdClient = new ClientDto(1L, "Dni", "Name", "Password", "Gender", 1, "Address", "9999999999", true);
        when(clientService.create(newClient)).thenReturn(createdClient);

        // Act
        ResponseEntity<ClientDto> response = clientController.create(newClient);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(createdClient, response.getBody());
    }

    //F5: Unit test - entidad dominio Client
    @Test
    void clientDomainTest() {
        Client client = new Client();
        client.setName("Ricardo");
        client.setDni("1245");
        client.setPassword("Password");
        client.setGender("Male");
        client.setAge(10);
        client.setAddress("Address");
        client.setPhone("999999");
        client.setActive(true);

        assertEquals("Ricardo", client.getName());
        assertEquals("1245", client.getDni());
        assertEquals("Password",client.getPassword());
        assertTrue(client.isActive());
    }

    //F6: Prueba de integracion, flujo completo service -> repository -> H2
    @Test
    void createClientIntegrationTest() {

        ClientDto newClient = new ClientDto(null,"1245","Ricardo",
            "Password","M",30,"Calle siempre viva 123","9999999",true);
        
            ClientDto created = realClientService.create(newClient);

            assertNotNull(created.getId());
            assertTrue(clientRepository.existsByDni("1245"));
            assertEquals("Ricardo", created.getName());
            
    }
}
