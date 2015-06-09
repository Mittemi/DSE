package opPlanner.KLINIsys.service;

import opPlanner.KLINIsys.model.AuthResult;
import opPlanner.KLINIsys.model.LoginUser;
import opPlanner.KLINIsys.repository.LoginUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Michael on 28.04.2015.
 */
@Service
public class AuthService {

    @Autowired
    private LoginUserRepository loginUserRepository;

    /**
     *
     * @param username username
     * @param password plain text password
     * @return authentication result (true, false, includes the roles)
     */
    public AuthResult authenticate(String username, String password) {

        LoginUser lu = loginUserRepository.findByEmail(username);

        if(lu != null) {
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

            if(bCryptPasswordEncoder.matches(password,lu.getPassword())) {

                LinkedList<String> roles = getRoles(lu);

                return new AuthResult(true,roles);
            }
        }
        // not user found!
        return new AuthResult(false, new LinkedList<>());
    }

    /**
     * extracts the role as list of strings from the supplied user
     * @param lu
     * @return
     */
    private LinkedList<String> getRoles(LoginUser lu) {
        LinkedList<String> roles = new LinkedList<>();

        roles.add(lu.getClass().getSimpleName());
        return roles;
    }

    /**
     *
     * @param password the plain text password
     * @return the encoded password
     */
    public String encodePassword(String password) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder.encode(password);
    }

    /**
     *
     * @param username the user to get it's roles for
     * @return the list of roles (Hospital, Patient, Doctor)
     */
    public List<String> roles(String username) {
        LoginUser lu = loginUserRepository.findByEmail(username);

        if(lu != null) {
            return getRoles(lu);
        }
        return new LinkedList<String>();
    }
}
