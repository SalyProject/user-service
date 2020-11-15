package com.saly.user.common.utils;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class JwtUtilTest {

    @Test
    void testFetchSubjectInvalidSecret() {
        // SETUP
        final String secret = "secret";
        final long tokenValidity = 60L;

        final String json = "{field: value}";
        final String jwtToken = JwtUtil.generateToken(secret, tokenValidity, json).orElse(null);

        // ACT
        final Optional<String> result = JwtUtil.fetchSubject(jwtToken, "invalid");

        // VERIFY
        assertThat(result).isEmpty();
    }

    @Test
    void testFetchSubjectInvalidInputs() {
        // ACT
        final Optional<String> result = JwtUtil.fetchSubject(null, null);

        // VERIFY
        assertThat(result).isEmpty();
    }

    @Test
    void testGenerateTokenFail() {
        final Optional<String> result = JwtUtil.generateToken(null, -3L, null);

        assertThat(result).isEmpty();
    }
}