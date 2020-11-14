package com.saly.user.common.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.saly.user.common.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
public class JwtUserDetailsFetcherImpl implements JwtUserDetailsFetcher {
    private final String jwtAuthSecret;
    private final ObjectMapper objectMapper;

    public JwtUserDetailsFetcherImpl(@Value("jwt.secret") String jwtAuthSecret, ObjectMapper objectMapper) {
        this.jwtAuthSecret = jwtAuthSecret;
        this.objectMapper = objectMapper;
    }

    @Override
    public Optional<SalyUserDetails> fetchUserDetails(String token) {
        return JwtUtil.fetchSubject(token, jwtAuthSecret)
                .flatMap(this::mapUserDetailsFromString);
    }

    private Optional<SalyUserDetails> mapUserDetailsFromString(final String userDetails) {
        try {
            return Optional.ofNullable(objectMapper.readValue(userDetails, SalyUserDetails.class));
        } catch (Exception e) {
            log.warn(e.getMessage());
            return Optional.empty();
        }
    }
}
