package opPlanner.ApiGateway.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheKey;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheResult;
import opPlanner.ApiGateway.Constants;
import opPlanner.Shared.OpPlannerProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * Created by Michael on 15.06.2015.
 */
@RestController
@RequestMapping("/patient")
public class PatientController {

    private RestTemplate client;

    @Autowired
    private OpPlannerProperties config;

    public PatientController() {
        client = new RestTemplate();
    }

    @PreAuthorize("hasRole('Doctor')")
    @HystrixCommand(fallbackMethod = "listFallback", groupKey = Constants.GROUP_KEY_KLINISYS)
    @RequestMapping(value = "/", method = RequestMethod.GET, produces = org.springframework.http
            .MediaType.APPLICATION_JSON_VALUE)
    public String list(@RequestParam(required = false) String filter) {

        return client.getForObject(config.getKlinisys().buildUrl("patient/autoComplete?filter={filter}"), String.class, filter);
    }

    public String listFallback(String filter) {
        return "[ ]";
    }
}
