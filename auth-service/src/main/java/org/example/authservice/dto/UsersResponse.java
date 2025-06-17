package org.example.authservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UsersResponse {
    private String message;
    private String token;
    private String refreshToken;
}

