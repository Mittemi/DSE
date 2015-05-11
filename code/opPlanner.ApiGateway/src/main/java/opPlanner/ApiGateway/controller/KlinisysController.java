package opPlanner.ApiGateway.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import opPlanner.ApiGateway.OpPlannerProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.websocket.server.PathParam;
import java.beans.Statement;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Michael on 18.04.2015.
 */
@RestController
@RequestMapping("/opslots")     //important otherwise the security checks won't work
public class KlinisysController {

    // Hystrix group
    private static final String groupKey = "klinisys";

    private RestTemplate client;

    @Autowired
    private OpPlannerProperties config;


    public KlinisysController() {
        client = new RestTemplate();
    }

   // @PreAuthorize("permitAll()")
    @RequestMapping(value = "/list/", method = RequestMethod.GET, produces = "application/json")
    @HystrixCommand(fallbackMethod = "fallback", groupKey = groupKey)
    public String index(Authentication auth) {

        if (auth != null && auth.isAuthenticated()) {
            if (auth.getAuthorities().stream().anyMatch(x -> x.getAuthority().equals("Hospital"))) {
                return "IsHospital";


            } else if (auth.getAuthorities().stream().anyMatch(x->x.getAuthority().equals("Doctor"))) {
                return "IsDoctor";
            } else if (auth.getAuthorities().stream().anyMatch(x->x.getAuthority().equals("Patient"))) {
                return "IsPatient";
            }
        } else {
            return "Public";
        }
        return "501";
    }

    private String request(String relativeUrl, Map<String, Object> params) {
        if(params == null)
            params = new HashMap<>();
        return client.getForObject("http://" + config.getKlinisys().getBaseUrl() + "" + relativeUrl, String.class, params);
    }

    @PreAuthorize("hasRole('Hospital')")
    @HystrixCommand(fallbackMethod = "fallback", groupKey = groupKey)
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
    public String deleteOpSlot(Authentication auth, @PathVariable("id") Integer slotId){

        //TODO: impl, additional permission check
        return "DeleteOpSlot: OK" + slotId;
    }

    @PreAuthorize("hasRole('Doctor')")
    @HystrixCommand(fallbackMethod = "fallback", groupKey = groupKey)
    @RequestMapping(value = "/reservation/{id}", method = RequestMethod.DELETE, produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
    public String deleteReservation(Authentication auth, @PathVariable("id") Integer slotId) {

        //TODO: impl, additional permission check
        return "DeleteReservation: OK " + slotId;
    }

    /**
     * Fallback if service is not available or something fails!
     * @return
     */
    public String fallback(Authentication auth) {
        return "Klinisys is down, retry later!";
    }

    public String fallback(Authentication auth, Integer id) {
        return "That didn't work out! Try again later!";
    }
}
