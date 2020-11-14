package com.saly.user.service.user;

import lombok.Data;

@Data
public class AuthenticationRequest {
    private String username;
    private String password;
}
