package com.saly.user.service.auth;

import com.saly.user.common.exception.NotFoundException;
import com.saly.user.service.user.UserDAO;
import com.saly.user.service.user.UserEntity;
import com.saly.user.service.user.UserRole;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
public class SalyUserDetailsService implements UserDetailsService {

    private final UserDAO userDAO;

    public SalyUserDetailsService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userDAO.findByEmail(email)
                .map(this::mapUserDetails)
                .orElseThrow(() -> new NotFoundException("User not found by email " + email));
    }

    @Transactional
    public UserDetails loadUserByUsernameWithoutPassword(String email) throws UsernameNotFoundException {
        return userDAO.findByEmail(email)
                .map(this::mapUserDetailsWithoutPassword)
                .orElseThrow(() -> new NotFoundException("User not found by email " + email));
    }

    private UserDetails mapUserDetails(UserEntity userEntity) {
        return new SalyUserDetailsWithPassword(
                userEntity.getID().getId(),
                userEntity.getEmail(),
                Set.of(UserRole.CUSTOMER),
                userEntity.getPassword()
        );
    }

    private UserDetails mapUserDetailsWithoutPassword(final UserEntity userEntity) {
        return new SalyUserDetails(
                userEntity.getID().getId(),
                userEntity.getEmail(),
                Set.of(UserRole.CUSTOMER)
        );
    }

}
