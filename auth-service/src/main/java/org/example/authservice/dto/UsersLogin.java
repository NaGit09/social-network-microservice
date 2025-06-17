package org.example.authservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class UsersLogin {
    @Email(message = "Email invalid")
    @NotBlank(message = "Email is not blank")
    private String email;
    @Length(message = "Password length is least 6 character" , min = 6)
    private String password_hash;

}