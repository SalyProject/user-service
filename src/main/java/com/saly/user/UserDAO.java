package com.saly.user;

import java.util.Set;

public interface UserDAO {
    UserEntity create(String email, String encodedPassword, Set<UserRole> roles);
}
