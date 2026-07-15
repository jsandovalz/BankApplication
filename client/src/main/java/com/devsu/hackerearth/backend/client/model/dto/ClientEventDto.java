package com.devsu.hackerearth.backend.client.model.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientEventDto implements Serializable{
    private Long clientId;
    private String name;
    private boolean isActive;
}
