package com.saly.user.common.auth;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class SalyUserDetails implements UserDetails {
    protected UUID userId;
    protected String username;
    protected Set<UserRole> roles;
    protected List<GrantedAuthority> authorities;

    public SalyUserDetails(UUID userId, String username, Set<UserRole> roles) {
        this.userId = userId;
        this.username = username;
        this.roles = roles;
        this.authorities = roles.stream().map(UserRole::asAuthority).collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
