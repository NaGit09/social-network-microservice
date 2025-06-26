package org.example.authservice.service;
import lombok.RequiredArgsConstructor;
import org.example.authservice.dto.UsersRegister;
import org.example.authservice.entity.users;
import org.example.authservice.repository.UsersRepository;
import org.example.authservice.utils.GenerateUser;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {
    public final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public boolean register(UsersRegister usersRegister) {
        String email = usersRegister.getEmail();
        String username = usersRegister.getUsername();
        String rawPassword = usersRegister.getPassword_hash();

        if(usersRepository.existsByEmail(email)) {
            return false;
        }

        String encodedPassword = passwordEncoder.encode(rawPassword);
        System.out.println("Raw Password: " + rawPassword);
        System.out.println("Encoded Password: " + encodedPassword);
        // SAVE USER INTO DB
        users u = GenerateUser.generateUserRegister(email, username, encodedPassword);
        usersRepository.save(u);
        return true;
    }
    @Override
    public Optional<users> login(String email, String password) {
        if (!checkEmail(email)) {
            System.out.println("Email already exists");
            return Optional.empty();
        }
        if (!checkPassword(email, password)) {
            System.out.println("Password doesn't match");
            return Optional.empty();
        }
        return usersRepository.findByEmail(email);
    }
    @Override
    public boolean checkEmail(String email) {
        return usersRepository.existsByEmail(email);
    }
    public boolean checkPassword(String email, String rawPassword) {
        String encodedPassword = usersRepository.getPasswordByEmail(email);
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
    public Optional<users> findUserById(UUID uuid) {
        return usersRepository.findById((uuid));
    }
}
