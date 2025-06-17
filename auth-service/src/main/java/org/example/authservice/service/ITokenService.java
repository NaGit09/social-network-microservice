package org.example.authservice.service;

import java.util.UUID;

public interface ITokenService {
     void deactivateRefreshToken(String refreshToken);
     void updateRefreshToken(UUID userId, String newToken);
}
