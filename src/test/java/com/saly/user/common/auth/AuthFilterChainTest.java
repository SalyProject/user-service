package com.saly.user.common.auth;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.saly.user.AbstractWebMvcTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@WebMvcTest(AuthTestController.class)
public class AuthFilterChainTest extends AbstractWebMvcTest {

    @Test
    void testWithoutToken() throws Exception {
        mockMvc.perform(get("/protected/api/authtest"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testWithInvalidHeader() throws Exception {
        final MockHttpServletRequestBuilder requestBuilder = get("/protected/api/authtest")
                .header("Authorization", "invalid");

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testWithInvalidToken() throws Exception {
        final String invalidToken = "invalidToken";

        final MockHttpServletRequestBuilder requestBuilder = get("/protected/api/authtest")
                .header("Authorization", "Bearer " + invalidToken);

        when(jwtTokenResolver.fetchUserDetails(invalidToken)).thenReturn(Optional.empty());

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testWithValidToken() throws Exception {
        final String token = "token";

        final MockHttpServletRequestBuilder requestBuilder = get("/protected/api/authtest")
                .header("Authorization", "Bearer " + token);


        final UUID userId = UUID.randomUUID();
        final String username = "username";
        final SalyUserDetails userDetails = new SalyUserDetails(userId, username, Set.of());

        when(jwtTokenResolver.fetchUserDetails(token)).thenReturn(Optional.of(userDetails));

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(username));
    }

    @Test
    void testWithInvalidRole() throws Exception {
        final String token = "token";

        final MockHttpServletRequestBuilder requestBuilder = get("/protected/api/authtest/customer")
                .header("Authorization", "Bearer " + token);


        final UUID userId = UUID.randomUUID();
        final String username = "username";
        final SalyUserDetails userDetails = new SalyUserDetails(userId, username, Set.of());

        when(jwtTokenResolver.fetchUserDetails(token)).thenReturn(Optional.of(userDetails));

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    void testWithValidRole() throws Exception {
        final String token = "token";

        final MockHttpServletRequestBuilder requestBuilder = get("/protected/api/authtest/customer")
                .header("Authorization", "Bearer " + token);


        final UUID userId = UUID.randomUUID();
        final String username = "username";
        final SalyUserDetails userDetails = new SalyUserDetails(userId, username, Set.of(UserRole.CUSTOMER));

        when(jwtTokenResolver.fetchUserDetails(token)).thenReturn(Optional.of(userDetails));

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(username));
    }

}
