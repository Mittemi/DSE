package opPlanner.ApiGateway.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import opPlanner.ApiGateway.Constants;
import opPlanner.ApiGateway.OpPlannerProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Michael on 18.04.2015.
 */
@RestController
@RequestMapping("/opslots")     //important otherwise the security checks won't work
public class OpSlotsController {

    private RestTemplate client;

    @Autowired
    private OpPlannerProperties config;

    public OpSlotsController() {
        client = new RestTemplate();
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET, produces = "application/json")
    @HystrixCommand(fallbackMethod = "indexFallback", groupKey = Constants.GROUP_KEY_KLINISYS)
    public String index(Authentication auth, @RequestParam(value = "from", required = false) String from, @RequestParam(value = "to", required = false) String to) {

        Map<String, Object> param = new HashMap<>();
        Map<String, Object> request = new HashMap<>();
        if (from != null) {
            request.put("from", from);
        }
        if (to != null) {
            request.put("to", to);
        }
        String url = null;
        System.out.print("OpSlot-List -- From: " + (from != null ? from : "null") + ", TO: " + (to != null ? to : ""));
        if (auth != null && auth.isAuthenticated()) {

            param.put("mail", auth.getPrincipal());
            url = config.getKlinisys().buildUrl("{type}/{mail}/");

            if (auth.getAuthorities().stream().anyMatch(x -> x.getAuthority().equals("Hospital"))) {
                System.out.println(" Group: Hospital, Mail: " + auth.getPrincipal());
                param.put("type", "hospital");
            } else if (auth.getAuthorities().stream().anyMatch(x -> x.getAuthority().equals("Doctor"))) {
                System.out.println(" Group: Doctor, Mail: " + auth.getPrincipal());
                param.put("type", "doctor");
            } else if (auth.getAuthorities().stream().anyMatch(x -> x.getAuthority().equals("Patient"))) {
                System.out.println(" Group: Patient, Mail: " + auth.getPrincipal());
                param.put("type", "patient");
            } else {
                return "501";
            }
        } else {
            url = config.getKlinisys().buildUrl("public/");
        }

        return client.postForObject(url, request, String.class, param);
    }

    /* fallback Hystrix */
    public String indexFallback(Authentication auth, String from, String to) {
        return "Service currently down! Try again later...";
    }


    @PreAuthorize("hasRole('Hospital')")
    @HystrixCommand(fallbackMethod = "deleteOpSlotFallback", groupKey = Constants.GROUP_KEY_KLINISYS)
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
    public String deleteOpSlot(Authentication auth, @PathVariable("id") Integer slotId){

        client.delete(config.getKlinisys().buildUrl("opslot/{id}"), slotId);

        System.out.println("DeleteOpSlot: OK" + slotId);

        return "OK";
    }

    /* fallback Hystrix */
    public String deleteOpSlotFallback(Authentication auth, Integer id) {
        return "That didn't work out! Try again later!";
    }


    @PreAuthorize("hasRole('Doctor')")
    @HystrixCommand(fallbackMethod = "deleteReservationFallback", groupKey = Constants.GROUP_KEY_RESERVATION)
    @RequestMapping(value = "/reservation/{id}", method = RequestMethod.DELETE, produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
    public String deleteReservation(Authentication auth, @PathVariable("id") Integer slotId) {

        client.delete(config.getReservation().buildUrl("reservation/{id}"), slotId);

        System.out.println("Reservation: OK" + slotId);

        return "OK";
    }

    /* fallback Hystrix */
    public String deleteReservationFallback(Authentication auth, Integer id) {
        return "That didn't work out! Try again later!";
    }
}
