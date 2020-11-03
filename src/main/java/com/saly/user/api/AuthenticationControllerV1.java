package com.saly.user.api;

import static com.saly.user.common.exception.ErrorCode.USER_NOT_ACTIVE;

import com.saly.user.common.exception.BadRequestException;
import com.saly.user.common.exception.SallyException;
import com.saly.user.service.auth.AuthenticationRequest;
import com.saly.user.service.auth.AuthenticationResponse;
import com.saly.user.service.auth.JwtTokenResolver;
import com.saly.user.service.auth.SalyUserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class AuthenticationControllerV1 {

    private final AuthenticationManager authenticationManager;

    private final JwtTokenResolver jwtTokenUtil;

    private final SalyUserDetailsService userDetailsService;

    public AuthenticationControllerV1(AuthenticationManager authenticationManager, JwtTokenResolver jwtTokenUtil,
                                      SalyUserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDetailsService = userDetailsService;
    }

    @RequestMapping(value = "/public/api/v1/authenticate", method = RequestMethod.POST)
    public AuthenticationResponse createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) {
        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
        final UserDetails userDetails = userDetailsService.loadUserByUsernameWithoutPassword(authenticationRequest.getUsername());

        final String token = jwtTokenUtil.generateToken(userDetails);
        return new AuthenticationResponse(token);
    }

    private void authenticate(String username, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new SallyException(USER_NOT_ACTIVE);
        } catch (BadCredentialsException e) {
            throw new BadRequestException("Incorrect credentials");
        }
    }

}
