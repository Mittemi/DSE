package opPlanner.KLINIsys.controller;

import opPlanner.KLINIsys.model.AuthResult;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;

/**
 * Created by Michael on 28.04.2015.
 */
@RestController
public class AuthController {

    @RequestMapping(value = "/auth/{username}/password/{password}", method = RequestMethod.GET, produces = "application/json")
    public AuthResult auth(@PathVariable("username") String username, @PathVariable("password") String password) {
        return new AuthResult(true, new LinkedList<>());
    }
}

