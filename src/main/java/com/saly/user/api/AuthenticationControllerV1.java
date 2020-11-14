package com.saly.user.api;

import static com.saly.user.common.exception.ErrorCode.USER_NOT_ACTIVE;

import com.saly.user.service.user.JwtUserDetailsGenerator;
import com.saly.user.common.exception.BadRequestException;
import com.saly.user.common.exception.SallyException;
import com.saly.user.service.user.AuthenticationRequest;
import com.saly.user.service.user.AuthenticationResponse;
import com.saly.user.service.user.SalyUserDetailsService;
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

    private final JwtUserDetailsGenerator jwtUserDetailsGenerator;

    private final SalyUserDetailsService userDetailsService;

    public AuthenticationControllerV1(AuthenticationManager authenticationManager, JwtUserDetailsGenerator jwtUserDetailsGenerator,
                                      SalyUserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtUserDetailsGenerator = jwtUserDetailsGenerator;
        this.userDetailsService = userDetailsService;
    }

    @RequestMapping(value = "/public/api/v1/authenticate", method = RequestMethod.POST)
    public AuthenticationResponse createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) {
        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
        final UserDetails userDetails = userDetailsService.loadUserByUsernameWithoutPassword(authenticationRequest.getUsername());

        final String token = jwtUserDetailsGenerator.generateToken(userDetails);
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
