package opPlanner.ApiGateway.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import opPlanner.ApiGateway.OpPlannerProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * Created by Michael on 18.04.2015.
 */
@RestController
@RequestMapping("/klinisys")
public class KlinisysController {

    // Hystrix group
    private static final String groupKey = "klinisys";

    private RestTemplate client;

    @Autowired
    private OpPlannerProperties config;

    public KlinisysController() {
        client = new RestTemplate();
    }

    @HystrixCommand(fallbackMethod = "fallback", groupKey = groupKey)
    @RequestMapping(value = "/", method = RequestMethod.GET, produces = "application/json")
    public String index() {

        return client.getForObject("http://" + config.getKlinisys().getIpOrHostname() + ":" + config.getKlinisys().getPort() + "/", String.class);
    }

    @RequestMapping(value = "/cuser", method = RequestMethod.GET, produces = "application/json")
    public String user() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }

    /**
     * Fallback if service is not available or something fails!
     * @return
     */
    public String fallback() {
        return "Klinisys is down, retry later!";
    }
}
