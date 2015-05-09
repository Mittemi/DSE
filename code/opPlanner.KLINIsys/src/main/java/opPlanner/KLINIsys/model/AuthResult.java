package opPlanner.KLINIsys.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by Michael on 28.04.2015.
 */
public class AuthResult {

    private boolean isAuthenticated;

    private List<String> roles;

    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    public void setIsAuthenticated(boolean isAuthenticated) {
        this.isAuthenticated = isAuthenticated;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public AuthResult(boolean isAuthenticated, List<String> roles) {
        this.isAuthenticated = isAuthenticated;
        this.roles = roles;
    }
}
