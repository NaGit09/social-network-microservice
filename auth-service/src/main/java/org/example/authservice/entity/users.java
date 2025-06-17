package org.example.authservice.entity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class users {

 @Id
 @Column(columnDefinition = "BINARY(16)")
 private UUID id;

 @Column(nullable = false, unique = true)
 private String username;

 @Column(nullable = false, unique = true)
 private String email;

 @Column(nullable = false)
 private String password_hash;

 private String full_name;

 private String avatar_url;

 private String role;

 @Column(nullable = false)
 private boolean active;

 @Column(nullable = false, updatable = false)
 private LocalDateTime created_at;

 @Column(nullable = false)
 private LocalDateTime updated_at;

 // JPA callback: auto set timestamps when insert
 @PrePersist
 protected void onCreate() {
  this.created_at = LocalDateTime.now();
  this.updated_at = LocalDateTime.now();
 }

 // JPA callback: auto update timestamp when update
 @PreUpdate
 protected void onUpdate() {
  this.updated_at = LocalDateTime.now();
 }
}