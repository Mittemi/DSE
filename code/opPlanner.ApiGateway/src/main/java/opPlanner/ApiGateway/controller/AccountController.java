package opPlanner.ApiGateway.controller;

import opPlanner.ApiGateway.AuthResult;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.core.MediaType;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Michael on 28.04.2015.
 */
@RestController
@RequestMapping("/account")
public class AccountController {

    @RequestMapping(value = "/details", method = RequestMethod.GET, produces = "application/json")
    public AuthResult details(Authentication auth) {
        List<String> roles = auth.getAuthorities().stream().map(x -> x.getAuthority()).collect(Collectors.toList());
        return new AuthResult(auth.isAuthenticated(), roles.toArray(new String[roles.size()]));
    }
}
