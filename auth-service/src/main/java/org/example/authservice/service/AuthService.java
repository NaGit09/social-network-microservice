package org.example.authservice.service;
import lombok.RequiredArgsConstructor;
import org.example.authservice.dto.UsersRegister;
import org.example.authservice.entity.users;
import org.example.authservice.repository.UsersRepository;
import org.example.authservice.utils.GenerateUser;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {
    public final UsersRepository usersRepository;

    @Override
    public users login(String email, String password) {
        if (checkEmail(email) && checkPassword(email, password))
                return null ;
        return usersRepository.getByEmail(email);
    }

    @Override
    public boolean logout() {
        return false;
    }

    @Override
    public boolean register(UsersRegister usersRegister) {
        String email = usersRegister.getEmail();
        String password = usersRegister.getPassword_hash();
        String username = usersRegister.getUsername();
        if(usersRepository.existsByEmail(email)) {
            return false;
        }
        // SAVE USER INTO DB
        users u = GenerateUser.generateUserRegister(email,password,username);
        usersRepository.save(u);
        return true;
    }
    @Override
    public boolean checkPassword(String email, String password) {
        return password.equals(usersRepository.getPasswordByEmail(email));
    }

    @Override
    public boolean checkEmail(String email) {
        return usersRepository.existsByEmail(email);
    }

    public users findUserById(UUID uuid) {
        return usersRepository.getUserById((uuid));
    }
}
