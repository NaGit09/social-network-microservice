package org.example.authservice.service;


import org.example.authservice.dto.UsersRegister;
import org.example.authservice.entity.users;

import java.util.Optional;

public interface IAuthService {
    public Optional<users> login(String email , String password);
    public boolean register(UsersRegister usersRegister);
    public boolean checkPassword(String email, String password);
    public boolean checkEmail(String email);

}
