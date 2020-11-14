package com.saly.user.common.auth;

import static java.util.Optional.ofNullable;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.saly.user.common.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class AuthenticationFilter extends OncePerRequestFilter {

    private final JwtUserDetailsFetcher jwtUserDetailsFetcher;

    public AuthenticationFilter(JwtUserDetailsFetcher jwtUserDetailsFetcher) {
        this.jwtUserDetailsFetcher = jwtUserDetailsFetcher;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

       if (Objects.isNull(SecurityContextHolder.getContext().getAuthentication())) {
           fetchJwtToken(request)
                   .flatMap(jwtUserDetailsFetcher::fetchUserDetails)
                   .ifPresent(userDetails -> authenticate(request, userDetails));
       }

       chain.doFilter(request, response);
    }


    private void authenticate(final HttpServletRequest request, final SalyUserDetails userDetails) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        usernamePasswordAuthenticationToken
                .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
    }

    private Optional<String> fetchJwtToken(HttpServletRequest request) {
        return ofNullable(request.getHeader("Authorization"))
                .filter(header -> header.startsWith("Bearer "))
                .map(header -> header.substring(7));
    }
}
