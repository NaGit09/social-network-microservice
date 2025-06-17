package org.example.authservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class message {
    public Integer status;
    public String message;

}
