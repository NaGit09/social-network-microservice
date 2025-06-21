package org.example.authservice.service;

import lombok.RequiredArgsConstructor;
import org.example.authservice.entity.ListToken;

import org.example.authservice.repository.ListTokenRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final  ListTokenRepository listTokenRepository;

    // Vô hiệu hóa refresh token khi logout
    public void deactivateRefreshToken(String refreshToken) {
        listTokenRepository.findByRefreshToken(refreshToken).ifPresent(token -> {
            token.setActive(false);
            token.setUpdatedAt(LocalDateTime.now());
            listTokenRepository.save(token);
        });
    }

    // Cập nhật refresh token mới
    public void updateRefreshToken(UUID userId, String newToken) {
        listTokenRepository.findByUserId(userId).ifPresentOrElse(existingToken -> {
            existingToken.setRefreshToken(newToken);
            existingToken.setUpdatedAt(LocalDateTime.now());
            existingToken.setActive(true);
            listTokenRepository.save(existingToken);
        }, () -> {
            // Nếu chưa có, tạo mới
            ListToken newEntry = new ListToken();
            newEntry.setId(UUID.randomUUID());
            newEntry.setUserId(userId);
            newEntry.setRefreshToken(newToken);
            newEntry.setActive(true);
            newEntry.setCreatedAt(LocalDateTime.now());
            newEntry.setUpdatedAt(LocalDateTime.now());
            listTokenRepository.save(newEntry);
        });
    }
}
