package com.miaostar.auditor.repository;

import com.miaostar.auditor.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByClientId(String clientId);
}
