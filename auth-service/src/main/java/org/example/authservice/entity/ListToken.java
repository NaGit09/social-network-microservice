package org.example.authservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

    @Entity
    @Table(name = "list_token")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public class ListToken {
        @Id
        @Column(columnDefinition = "BINARY(16)")
        private UUID id;

        @Column(name = "user_id")
        private UUID userId;

        @Column(name = "refresh_token")
        private String refreshToken;

        private Boolean active;

        @Column(name = "created_at")
        private LocalDateTime createdAt;

        @Column(name = "updated_at")
        private LocalDateTime updatedAt;

        // getters & setters hoặc dùng @Data nếu bạn dùng Lombok
    }


