package opPlanner.ApiGateway;

import opPlanner.Shared.OpPlannerProperties;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Michael on 28.04.2015.
 *
 * The spring security provider for authentication against the klinisys
 *
 */
public class CustomAuthenticationProvider implements AuthenticationProvider {

    public CustomAuthenticationProvider(OpPlannerProperties config) {
        this.restClient = new RestTemplate();
        this.config = config;
    }
    private OpPlannerProperties config;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String name = authentication.getName();
        String password = authentication.getCredentials().toString();

        AuthHystrixCommand authHystrixCommand = new AuthHystrixCommand(name, password);
        AuthResult result = authHystrixCommand.execute();

        if(result.isAuthenticated()) {

            List<String> list = Arrays.asList(result.getRoles());
            List<GrantedAuthority> grantedAuths = list.stream().map((x) -> new SimpleGrantedAuthority(x)).collect(Collectors.toList());
            Authentication auth = new UsernamePasswordAuthenticationToken(name, password, grantedAuths);
            SecurityContextHolder.getContext().setAuthentication(auth);
            return auth;
        }
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class) || authentication.equals(AnonymousAuthenticationToken.class);
    }

    /**
     * used as fallback mechanism for the login, in case the klinisys is not responding within the set timeout we fallback to not loggedin
     */
    private class AuthHystrixCommand extends com.netflix.hystrix.HystrixCommand<AuthResult> {

        private String name;
        private String password;

        protected AuthHystrixCommand(String name, String password) {
            super(() -> Constants.GROUP_KEY_AUTH);
            this.name = name;
            this.password = password;
        }

        @Override
        protected AuthResult getFallback() {
            return fallback(name,password);
        }

        @Override
        protected AuthResult run() throws Exception {
            return auth(this.name, this.password);
        }
    }

    private RestTemplate restClient;

    /**
     * authenticates against klinisys
     * this method is used as part of the hystrix command
     * @param username username
     * @param password plain text password
     * @return auth result
     */
    public AuthResult auth(String username, String password) {

        Map<String, Object> params = new HashMap<>();
        params.put("user", username);
        params.put("password", password);

        String url = config.getKlinisys().buildUrl("/auth/{user}/password/{password}");
        System.out.println("Call: " + url);
        AuthResult result  = restClient.getForObject(url, AuthResult.class, params);
        return result;
    }

    // for hystrix fallback
    public AuthResult fallback(String username, String password) {
        System.out.println("Hystrix fallback for authentication!!");
        AuthResult result = new AuthResult(false);
        return result;
    }
}
