package com.saly.user.common.auth;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import javax.annotation.security.RolesAllowed;

@RestController
public class AuthTestController {

    @RequestMapping(path = "/protected/api/authtest")
    public String protectedEndpoint(final Principal principal) {
        return principal.getName();
    }

    @RolesAllowed({Roles.CUSTOMER})
    @RequestMapping(path = "/protected/api/authtest/customer")
    public String protectedCustomerEndpoint(final Principal principal) {
        return principal.getName();
    }
}
