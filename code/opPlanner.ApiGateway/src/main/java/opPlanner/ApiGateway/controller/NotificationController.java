package opPlanner.ApiGateway.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import opPlanner.ApiGateway.Constants;
import opPlanner.Shared.OpPlannerProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * Created by Michael on 05.06.2015.
 */
@RestController
@RequestMapping("/notification")
public class NotificationController {

    private RestTemplate client;

    @Autowired
    private OpPlannerProperties config;

    public NotificationController() {
        client = new RestTemplate();
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET, produces = "application/json")
    @HystrixCommand(fallbackMethod = "indexFallback", groupKey = Constants.GROUP_KEY_NOTIFICATION)
    @PreAuthorize("isAuthenticated()")
    public String index(Authentication auth) {

        return client.getForObject(config.getNotifier().buildUrl("list/{eMail}/"),String.class, auth.getPrincipal());
    }

    /* Hystrix Fallback */
    public String indexFallback(Authentication auth) {
        return "Notification Service currently not available!";
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    @HystrixCommand(fallbackMethod = "deleteFallback", groupKey = Constants.GROUP_KEY_NOTIFICATION)
    @PreAuthorize("isAuthenticated()")
    public void delete(String id, Authentication auth) {
        client.delete(config.getNotifier().buildUrl("delete/" + auth.getPrincipal() + "/" + id));
    }

    public void deleteFallback(String id, Authentication auth) {

    }
}
