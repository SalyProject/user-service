package com.saly.user.service.auth;

import com.saly.user.service.user.UserRole;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

@Getter
@Setter
@NoArgsConstructor
public class AuthorityImpl implements GrantedAuthority {
    private UserRole userRole;

    public AuthorityImpl(UserRole userRole) {
        this.userRole = userRole;
    }

    @Override
    public String getAuthority() {
        return userRole.getRole();
    }
}
