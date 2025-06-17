package org.example.authservice.service;


import org.example.authservice.dto.UsersRegister;
import org.example.authservice.entity.users;

public interface IAuthService {
    public users login(String email , String password);
    public boolean logout();
    public boolean register(UsersRegister usersRegister);
    public boolean checkPassword(String email, String password);
    public boolean checkEmail(String email);

}
