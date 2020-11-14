package com.saly.user.common.auth;

import com.saly.user.common.auth.AuthorityImpl;
import com.saly.user.common.auth.Roles;
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
