package com.saly.user.service.user;

import com.saly.user.common.auth.UserRole;

import java.util.Optional;
import java.util.Set;

public interface UserDAO {
    UserEntity create(String email, String encodedPassword, Set<UserRole> roles);

    Optional<UserEntity> findByEmail(String email);
}
