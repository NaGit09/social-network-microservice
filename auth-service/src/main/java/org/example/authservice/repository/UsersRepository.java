package org.example.authservice.repository;

import org.example.authservice.entity.users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface UsersRepository extends JpaRepository<users, UUID> {

    boolean existsByEmail(String email);

    @Query("SELECT u.password_hash FROM users u WHERE u.email = :email")
    String getPasswordByEmail(@Param("email") String email);

    Optional<users> findByEmail(String email);


    Optional<users> findById(UUID id);
}
