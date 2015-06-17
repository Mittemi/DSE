package opPlanner.ApiGateway.controller;

import opPlanner.ApiGateway.AuthResult;
import opPlanner.Shared.OpPlannerProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Michael on 28.04.2015.
 */
@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private OpPlannerProperties config;

    @RequestMapping(value = "/details", method = RequestMethod.GET, produces = "application/json")
    public AuthResult details(Authentication auth) {

        System.out.println("IP: " + this.config.getKlinisys().getIpOrHostname());

        if(auth == null || !auth.isAuthenticated()) {
            return new AuthResult(false);
        }

        List<String> roles = auth.getAuthorities().stream().map(x -> x.getAuthority()).collect(Collectors.toList());
        return new AuthResult(auth.isAuthenticated(), roles.toArray(new String[roles.size()]));
    }
}
