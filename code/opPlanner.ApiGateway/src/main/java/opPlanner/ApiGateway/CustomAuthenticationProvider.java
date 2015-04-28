package opPlanner.ApiGateway;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Michael on 28.04.2015.
 */
public class CustomAuthenticationProvider implements AuthenticationProvider {

    public CustomAuthenticationProvider(OpPlannerProperties config) {
        this.restClient = new RestTemplate();
        this.config = config;
    }
    private OpPlannerProperties config;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        System.out.println("Auth user!!!");

        String name = authentication.getName();
        String password = authentication.getCredentials().toString();
        AuthHystrixCommand authHystrixCommand = new AuthHystrixCommand(name, password);
        AuthResult result = authHystrixCommand.execute();

        if(result.isAuthenticated()) {

            List<String> list = Arrays.asList(result.getRoles());
            List<GrantedAuthority> grantedAuths = list.stream().map((x) -> new SimpleGrantedAuthority(x)).collect(Collectors.toList());
            Authentication auth = new UsernamePasswordAuthenticationToken(name, password, grantedAuths);
            return auth;
        }else {
            return null;
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

    private class AuthHystrixCommand extends com.netflix.hystrix.HystrixCommand<AuthResult> {

        private String name;
        private String password;

        protected AuthHystrixCommand(String name, String password) {
            super(() -> "auth");
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

    public AuthResult auth(String username, String password) {

        Map<String, Object> params = new HashMap<>();
        params.put("user", username);
        params.put("password", password);

        String url = "http://" + config.getKlinisys().getIpOrHostname() + ":" + config.getKlinisys().getPort() + "/auth/{user}/password/{password}";
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
