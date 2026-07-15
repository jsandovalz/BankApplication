package com.devsu.hackerearth.backend.account.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class ClientCache {

    @Id
    private Long clientId;

    private String name;

    @Column(name="is_active")
    private boolean isActive;
    
}
