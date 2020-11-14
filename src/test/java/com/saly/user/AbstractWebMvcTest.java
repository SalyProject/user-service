package com.saly.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.saly.user.common.auth.JwtUserDetailsFetcher;
import com.saly.user.service.user.SalyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

public abstract class AbstractWebMvcTest {
    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected SalyUserDetailsService userDetailsService;
    @MockBean
    protected JwtUserDetailsFetcher jwtTokenResolver;
}
