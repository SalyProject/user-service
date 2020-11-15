package com.saly.user.common.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.saly.user.common.utils.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class JwtUserDetailsFetcherImplTest {

    private String secret = "secret";
    private ObjectMapper objectMapper = mock(ObjectMapper.class);

    private JwtUserDetailsFetcherImpl fetcher = new JwtUserDetailsFetcherImpl(secret, objectMapper);

    @Test
    void fetchUserDetailSuccess() throws JsonProcessingException {
        // SETUP
        final String json = "json";
        final String token = JwtUtil.generateToken(secret, 60L, json).orElseThrow(RuntimeException::new);

        final SalyUserDetails salyUserDetails = new SalyUserDetails(UUID.randomUUID(), "name", Collections.emptySet());
        when(objectMapper.readValue(json, SalyUserDetails.class)).thenReturn(salyUserDetails);

        // ACT
        final Optional<SalyUserDetails> result = fetcher.fetchUserDetails(token);

        // VERIFY
        assertThat(result).contains(salyUserDetails);
    }

    @Test
    void fetchUserDetailFailedReadValue() throws JsonProcessingException {
        // SETUP
        final String json = "json";
        final String token = JwtUtil.generateToken(secret, 60L, json).orElseThrow(RuntimeException::new);

        when(objectMapper.readValue(json, SalyUserDetails.class)).thenThrow(JsonProcessingException.class);

        // ACT
        final Optional<SalyUserDetails> result = fetcher.fetchUserDetails(token);

        // VERIFY
        assertThat(result).isEmpty();
    }
}