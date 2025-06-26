package org.example.authservice.utils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.example.authservice.entity.users;
import org.springframework.stereotype.Component;


import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;
@Component
@RequiredArgsConstructor
public class GenerateUser {
    public static users generateUserRegister(String email, String username, String password) {
        return users.builder()
                .id(UUID.randomUUID())
                .email(email)
                .username(username)
                .password_hash(password)
                .active(false)
                .build();
    }


}
