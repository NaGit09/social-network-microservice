package org.example.authservice.repository;

import org.example.authservice.entity.ListToken;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ListTokenRepository extends JpaRepository<ListToken, UUID> {

    Optional<ListToken> findByUserId(UUID userId);

    Optional<ListToken> findByRefreshToken(String refreshToken);

    void deleteByUserId(UUID userId);
}

