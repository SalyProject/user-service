package com.saly.user.service.auth;

import com.saly.user.service.user.UserRole;
import lombok.Getter;

import java.util.Set;
import java.util.UUID;

@Getter
public class SalyUserDetailsWithPassword extends SalyUserDetails {
    private String password;

    public SalyUserDetailsWithPassword(UUID userId, String username, Set<UserRole> roles, String password) {
        super(userId, username, roles);
        this.password = password;
    }

    public String getPassword() {
        return password;
    }
}
