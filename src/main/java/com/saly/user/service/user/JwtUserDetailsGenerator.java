package com.saly.user.service.user;

import org.springframework.security.core.userdetails.UserDetails;

public interface JwtUserDetailsGenerator {
    String generateToken (UserDetails userDetails);
}
