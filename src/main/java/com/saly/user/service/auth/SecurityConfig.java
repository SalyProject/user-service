package com.saly.user.service.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, jsr250Enabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final SalyUserDetailsService userDetailsService;
    private final JwtTokenResolver jwtTokenResolver;

    public SecurityConfig(SalyUserDetailsService userDetailsService, JwtTokenResolver jwtTokenResolver) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenResolver = jwtTokenResolver;
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(encoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public AuthenticationFilter authenticationFilter() {
        return new AuthenticationFilter(jwtTokenResolver);
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new SalyAuthenticationEntryPoint();
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable()
                .httpBasic().disable()
                .authorizeRequests()
                .antMatchers("/protected/api/**").authenticated()
                .anyRequest().permitAll()
                .and()
                .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint())
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // Add a filter to validate the tokens with every request
        httpSecurity.addFilterBefore(authenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }


}
