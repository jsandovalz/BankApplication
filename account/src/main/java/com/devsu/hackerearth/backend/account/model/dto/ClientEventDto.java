package com.devsu.hackerearth.backend.account.model.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ClientEventDto implements Serializable{
    private Long clientId;
    private String name;
    private boolean isActive;
    
}
