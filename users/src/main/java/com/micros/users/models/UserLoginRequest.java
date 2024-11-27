package com.micros.users.models;

import lombok.Data;
import lombok.Getter;

@Getter
@Data
public class UserLoginRequest {
    private String username;
    private String password;
}
