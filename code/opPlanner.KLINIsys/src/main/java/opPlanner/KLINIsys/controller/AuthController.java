package opPlanner.KLINIsys.controller;

import opPlanner.KLINIsys.model.AuthResult;
import opPlanner.KLINIsys.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import java.util.LinkedList;
import java.util.List;

/**
 * Created by Michael on 28.04.2015.
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @RequestMapping(value = "/{username}/password/{password}", method = RequestMethod.GET, produces = "application/json")
    public AuthResult auth(@PathVariable("username") String username, @PathVariable("password") String password) {
        return authService.authenticate(username, password);
    }

    @RequestMapping(value = "/{username}/roles", method = RequestMethod.GET, produces = "application/json")
    public List<String> getRoles(@PathVariable("username") String username) {
        return authService.roles(username);
    }
}

