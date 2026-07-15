package com.devsu.hackerearth.backend.account.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devsu.hackerearth.backend.account.model.ClientCache;

public interface ClientCacheRepository extends JpaRepository<ClientCache, Long> {

}
