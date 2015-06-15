package opPlanner.ApiGateway;

import opPlanner.Shared.OpPlannerProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Created by Michael on 04.04.2015.
 */
@Configuration
@EnableWebSecurity()
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Order(1)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private OpPlannerProperties plannerProperties;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //TODO: enable CSRF
        http.authenticationProvider(new CustomAuthenticationProvider(plannerProperties)).httpBasic().and().anonymous().and().csrf().disable().logout().invalidateHttpSession(true).logoutUrl("/logout").logoutSuccessUrl("/account/details");
    }
}