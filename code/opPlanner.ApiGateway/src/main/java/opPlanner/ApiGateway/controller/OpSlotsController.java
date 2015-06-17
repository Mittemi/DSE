package opPlanner.ApiGateway.controller;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.sun.javafx.fxml.builder.URLBuilder;
import opPlanner.Shared.OpPlannerProperties;
import opPlanner.ApiGateway.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.util.MultiValueMap;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Michael on 18.04.2015.
 */
@RestController
@RequestMapping("/opslots")
public class OpSlotsController {

    private RestTemplate client;

    @Autowired
    private OpPlannerProperties config;

    public OpSlotsController() {
        client = new RestTemplate();
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET, produces = "application/json")
    @HystrixCommand(fallbackMethod = "indexFallback", threadPoolProperties = { @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "3000"), @HystrixProperty(name = "", value = "0")}, groupKey = Constants.GROUP_KEY_KLINISYS)
    public String index(Authentication auth, @RequestParam(value = "from", required = false) String from, @RequestParam(value = "to", required = false) String to, HttpServletResponse response) {

        Map<String, Object> param = new HashMap<>();
        String parameters = "";
        if (from != null && to != null) {
            parameters = "?from="+from +"&to="+to;
        }

        String url = null;
        System.out.print("OpSlot-List -- From: " + (from != null ? from : "null") + ", TO: " + (to != null ? to : "null"));
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
                response.setStatus(HttpServletResponse.SC_NOT_IMPLEMENTED);
                return null;
            }
        } else {
            url = config.getKlinisys().buildUrl("public/");
        }

        return client.getForObject(url+parameters, String.class, param);
    }

    /* fallback Hystrix */
    public String indexFallback(Authentication auth, String from, String to, HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
        return "[ ]";
    }

    @PreAuthorize("hasRole('Hospital')")
    @HystrixCommand(fallbackMethod = "createFallback", groupKey = Constants.GROUP_KEY_KLINISYS)
    @RequestMapping(value = "/create", method = RequestMethod.PUT, consumes = "application/json")
    public void create(Authentication auth, @RequestBody Object requestBody, HttpServletResponse response) {

        System.out.println("API: create slot");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Object> entity = new HttpEntity<>(requestBody,headers);

        client.put(config.getKlinisys().buildUrl("opslot/create/" + auth.getPrincipal() + "/"), entity);
    }

    public void createFallback(Authentication auth, Object requestBody, HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
    }


    @PreAuthorize("hasRole('Hospital')")
    @HystrixCommand(fallbackMethod = "deleteOpSlotFallback", groupKey = Constants.GROUP_KEY_KLINISYS)
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
    public void deleteOpSlot(Authentication auth, @PathVariable("id") Integer slotId, HttpServletResponse response){

        client.delete(config.getKlinisys().buildUrl("opslot/" + auth.getPrincipal() + "/{id}"), slotId);

        System.out.println("DeleteOpSlot: OK" + slotId);
    }

    /* fallback Hystrix */
    public void deleteOpSlotFallback(Authentication auth, Integer id, HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
    }

    @PreAuthorize("hasRole('Doctor')")
    @HystrixCommand(fallbackMethod = "createReservationFallback", groupKey = Constants.GROUP_KEY_RESERVATION)
    @RequestMapping(value = "/reservation", method = RequestMethod.POST)
    public void createReservation(Authentication auth, @RequestBody LinkedHashMap<String, Object> map, HttpServletResponse response) {

        map.put("doctorId", auth.getPrincipal());
        String reservationURI = config.getReservation().buildUrl("reservation/reserve?preferredStart={preferredStart}&preferredEnd={preferredEnd}&preferredPerimeter={preferredPerimeter}&opSlotType={opSlotType}&patientId={patientId}&doctorId={doctorId}");

        try {
            ResponseEntity responseEntity = client.postForEntity(reservationURI, null, ResponseEntity.class, map);
        }catch(HttpClientErrorException e) {
            // client doesn't care about 404 status code used by the server
            // gets notification anyway
            if(!e.getStatusCode().equals(HttpStatus.NOT_FOUND))
                throw e;
            response.setStatus(e.getStatusCode().value());
        }
    }

    /* fallback Hystrix */
    public void createReservationFallback(Authentication auth, LinkedHashMap<String, Object> map, HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
    }


    @PreAuthorize("hasRole('Doctor')")
    @HystrixCommand(fallbackMethod = "deleteReservationFallback", groupKey = Constants.GROUP_KEY_RESERVATION)
    @RequestMapping(value = "/reservation/{id}", method = RequestMethod.DELETE, produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
    public void deleteReservation(Authentication auth, @PathVariable("id") Integer slotId, HttpServletResponse response) {
        Map<String, Object> params = new HashMap<>();
        params.put("slotId", slotId);
        params.put("doctorId", auth.getPrincipal());
        client.delete(config.getReservation().buildUrl
                ("reservation/cancelByOpSlotId?opSlotId={slotId}&doctorId={doctorId}"), params);
    //todo test this
        System.out.println("Reservation: OK" + slotId);
    }

    /* fallback Hystrix */
    public void deleteReservationFallback(Authentication auth, Integer slotId, HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
    }
}
