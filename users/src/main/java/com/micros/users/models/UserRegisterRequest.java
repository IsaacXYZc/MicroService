package com.micros.users.models;

import lombok.Data;
import lombok.Getter;

@Getter
@Data
public class UserRegisterRequest {
    private String username;
    private String email;
    private String password;
}
