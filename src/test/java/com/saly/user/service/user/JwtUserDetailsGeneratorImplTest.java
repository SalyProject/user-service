package com.saly.user.service.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.saly.user.common.exception.SallyException;
import com.saly.user.common.utils.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;

class JwtUserDetailsGeneratorImplTest {

    private String secret = "secret";
    private long tokenValidity = 60L;
    private ObjectMapper objectMapper = mock(ObjectMapper.class);

    private JwtUserDetailsGeneratorImpl generator = new JwtUserDetailsGeneratorImpl(secret, tokenValidity, objectMapper);

    @Test
    void testGenerateTokenSuccess() throws JsonProcessingException {
        //  SETUP
        final String json = "userDetailsJson";
        final UserDetails userDetails = mock(UserDetails.class);

        when(objectMapper.writeValueAsString(userDetails)).thenReturn(json);

        final String token = generator.generateToken(userDetails);

        assertThat(token).isNotBlank();
        final String jwtToken = JwtUtil.generateToken(secret, tokenValidity, json)
                .orElse(null);

        assertThat(token).isEqualTo(jwtToken);

        final String tokenSubject = JwtUtil.fetchSubject(token, secret).orElse(null);
        assertThat(tokenSubject).isEqualTo(json);
    }

    @Test
    void testGenerateTokenFailedConvertToJson() throws JsonProcessingException {
        //  SETUP
        final UserDetails userDetails = mock(UserDetails.class);

        when(objectMapper.writeValueAsString(userDetails)).thenThrow(JsonProcessingException.class);

        // VERIFY
        assertThrows(SallyException.class, () -> generator.generateToken(userDetails));
    }
}