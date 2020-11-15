package com.saly.user.service.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.saly.user.common.auth.UserRole;
import com.saly.user.common.exception.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@ExtendWith(MockitoExtension.class)
class SalyUserDetailsServiceTest {

    @Mock
    private UserDAO userDAO;

    @InjectMocks
    private SalyUserDetailsService service;

    @Test
    void testLoadUserByUsernameSuccess() {
        // SETUP
        final String email = "email";
        final String password = "password";
        final UUID userId = UUID.randomUUID();
        final Set<UserRole> roles = Set.of(UserRole.CUSTOMER);

        final Set<GrantedAuthority> grantedAuthorities = roles.stream()
                .map(UserRole::asAuthority)
                .collect(Collectors.toSet());

        final UserEntity userEntity = new UserEntity();
        userEntity.setId(userId);
        userEntity.setPassword(password);
        userEntity.setEmail(email);
        userEntity.setRoles(roles);

        when(userDAO.findByEmail(email)).thenReturn(Optional.of(userEntity));

        // ACT
        final UserDetails userDetails = service.loadUserByUsername(email);

        // VERIFY
        assertThat(userDetails).isNotNull();
        assertThat(userDetails.getUsername()).isEqualTo(email);
        assertThat(userDetails.getPassword()).isEqualTo(password);
        assertThat(userDetails.isAccountNonExpired()).isTrue();
        assertThat(userDetails.isAccountNonLocked()).isTrue();
        assertThat(userDetails.isCredentialsNonExpired()).isTrue();
        assertThat(userDetails.isEnabled()).isTrue();

        assertThat(userDetails.getAuthorities().size()).isEqualTo(grantedAuthorities.size());
        assertThat(userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority))
                .containsAll(grantedAuthorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet()));
    }

    @Test
    void testLoadUserByUsernameNotFoundUser() {
        // SETUP
        final String email = "email";

        when(userDAO.findByEmail(email)).thenReturn(Optional.empty());

        // ACT
        assertThrows(NotFoundException.class, () -> service.loadUserByUsername(email));
    }

    @Test
    void testLoadUserByUsernameWithoutPasswordSuccess() {
        // SETUP
        final String email = "email";
        final String password = "password";
        final UUID userId = UUID.randomUUID();
        final Set<UserRole> roles = Set.of(UserRole.CUSTOMER);

        final Set<GrantedAuthority> grantedAuthorities = roles.stream()
                .map(UserRole::asAuthority)
                .collect(Collectors.toSet());

        final UserEntity userEntity = new UserEntity();
        userEntity.setId(userId);
        userEntity.setPassword(password);
        userEntity.setEmail(email);
        userEntity.setRoles(roles);

        when(userDAO.findByEmail(email)).thenReturn(Optional.of(userEntity));

        // ACT
        final UserDetails userDetails = service.loadUserByUsernameWithoutPassword(email);

        // VERIFY
        assertThat(userDetails).isNotNull();
        assertThat(userDetails.getUsername()).isEqualTo(email);
        assertThat(userDetails.getPassword()).isNull();
        assertThat(userDetails.isAccountNonExpired()).isTrue();
        assertThat(userDetails.isAccountNonLocked()).isTrue();
        assertThat(userDetails.isCredentialsNonExpired()).isTrue();
        assertThat(userDetails.isEnabled()).isTrue();

        assertThat(userDetails.getAuthorities().size()).isEqualTo(grantedAuthorities.size());
        assertThat(userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority))
                .containsAll(grantedAuthorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet()));
    }


    @Test
    void testLoadUserByUsernameWithoutPasswordNotFoundUser() {
        // SETUP
        final String email = "email";

        when(userDAO.findByEmail(email)).thenReturn(Optional.empty());

        // ACT
        assertThrows(NotFoundException.class, () -> service.loadUserByUsernameWithoutPassword(email));
    }
}