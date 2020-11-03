package com.saly.user.service.user;

import com.saly.user.service.auth.AuthorityImpl;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

@Getter
@AllArgsConstructor
public enum UserRole {
    CUSTOMER(Roles.CUSTOMER);

    private final String role;

    public GrantedAuthority asAuthority() {
        return new AuthorityImpl(this);
    }

}
