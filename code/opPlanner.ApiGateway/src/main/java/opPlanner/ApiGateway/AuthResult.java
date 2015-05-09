package opPlanner.ApiGateway;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * Created by Michael on 28.04.2015.
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class AuthResult {
    @JsonProperty("authenticated")
    private boolean isAuthenticated;
    @JsonProperty("roles")
    private String[] roles;

    public AuthResult(boolean isAuthenticated) {
        this(isAuthenticated, new String[0]);
    }

    public AuthResult(boolean isAuthenticated,String[] roles) {
        this.isAuthenticated = isAuthenticated;
        this.roles = roles;
    }

    // required for json support

    public AuthResult() {

    }

    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    public void setIsAuthenticated(boolean isAuthenticated) {
        this.isAuthenticated = isAuthenticated;
    }

    public String[] getRoles() {
        return roles;
    }

    public void setRoles(String[] roles) {
        this.roles = roles;
    }
}