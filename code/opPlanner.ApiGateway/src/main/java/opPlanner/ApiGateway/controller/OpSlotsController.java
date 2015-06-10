package opPlanner.ApiGateway.controller;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.sun.javafx.fxml.builder.URLBuilder;
import opPlanner.Shared.OpPlannerProperties;
import opPlanner.ApiGateway.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

    @RequestMapping(value = "/list", method = RequestMethod.POST, produces = "application/json")
    @HystrixCommand(fallbackMethod = "indexFallback", groupKey = Constants.GROUP_KEY_KLINISYS)
    public String index(Authentication auth, @RequestParam(value = "from", required = false) String from, @RequestParam(value = "to", required = false) String to, HttpServletResponse response) {

        Map<String, Object> param = new HashMap<>();
        Map<String, Object> request = new HashMap<>();
        if (from != null) {
            request.put("from", from);
        }
        if (to != null) {
            request.put("to", to);
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

        return client.postForObject(url, request, String.class, param);
    }

    /* fallback Hystrix */
    public String indexFallback(Authentication auth, String from, String to, HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
        return "Service currently down! Try again later...";
    }

    @PreAuthorize("hasRole('Hospital')")
    @HystrixCommand(fallbackMethod = "createFallback", groupKey = Constants.GROUP_KEY_KLINISYS)
    @RequestMapping(value = "/create", method = RequestMethod.PUT, consumes = "application/json")
    public String create(Authentication auth, @RequestBody Object requestBody, HttpServletResponse response) {

        System.out.println("API: create slot");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Object> entity = new HttpEntity<>(requestBody,headers);

        client.put(config.getKlinisys().buildUrl("opslot/create/" + auth.getPrincipal() + "/"), entity);

        return "OK";
    }

    public String createFallback(Authentication auth, Object requestBody, HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
        return "That didn't work out! Try again later!";
    }


    @PreAuthorize("hasRole('Hospital')")
    @HystrixCommand(fallbackMethod = "deleteOpSlotFallback", groupKey = Constants.GROUP_KEY_KLINISYS)
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
    public String deleteOpSlot(Authentication auth, @PathVariable("id") Integer slotId, HttpServletResponse response){

        client.delete(config.getKlinisys().buildUrl("opslot/" + auth.getPrincipal() + "/{id}"), slotId);

        System.out.println("DeleteOpSlot: OK" + slotId);

        return "OK";
    }

    /* fallback Hystrix */
    public String deleteOpSlotFallback(Authentication auth, Integer id, HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
        return "That didn't work out! Try again later!";
    }

    //http://localhost:9002/reservation/reserve?preferredStart=2014-05-20%2010:00&preferredEnd=2016-06-27%2018:00
    // &preferredPerimeter=500&opSlotType=Demo&doctorId=d1@dse.at&patientId=p1@dse.at

    @PreAuthorize("hasRole('Doctor')")
    @HystrixCommand(fallbackMethod = "createReservationFallback", groupKey = Constants.GROUP_KEY_RESERVATION)
    @RequestMapping(value = "/reservation", method = RequestMethod.POST, produces = org.springframework.http
            .MediaType.APPLICATION_JSON_VALUE)
    public String createReservation(Authentication auth, String patientId, Date preferredStart, Date preferredEnd,
                                    Integer preferredPerimeter, String opSlotType, HttpServletResponse response) {

        String doctorMail = (String)auth.getPrincipal();
        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        map.add("patientId", patientId);
        map.add("preferredStart", formatter.format(preferredStart));
        map.add("preferredEnd", formatter.format(preferredEnd));
        map.add("preferredPerimeter", preferredPerimeter.toString());
        map.add("opSlotType", opSlotType);
        URI reservationURI;
        ResponseEntity<String> result;
        try {
            reservationURI = new URI(config.getReservation().buildUrl("reservation/reserve"));
            result = client.postForEntity(reservationURI, map, String.class);
        } catch (URISyntaxException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return "NOK (URI Problems)";
        } catch (HttpClientErrorException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return "NOK (Reservation was not sucessful)";
        }

        System.out.println(result.getBody());

        return "OK";
    }

    /* fallback Hystrix */
    public String createReservationFallback(Authentication auth, Integer id, HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
        return "That didn't work out! Try again later!";
    }


    @PreAuthorize("hasRole('Doctor')")
    @HystrixCommand(fallbackMethod = "deleteReservationFallback", groupKey = Constants.GROUP_KEY_RESERVATION)
    @RequestMapping(value = "/reservation/{id}", method = RequestMethod.DELETE, produces = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
    public String deleteReservation(Authentication auth, @PathVariable("id") Integer slotId, HttpServletResponse response) {
        Map<String, Object> params = new HashMap<>();
        params.put("slotId", slotId);
        params.put("doctorId", auth.getPrincipal());
        client.delete(config.getReservation().buildUrl
                ("reservation/cancelByOpSlotId?opSlotId={slotId}&doctorId={doctorId}"), params);
    //todo test this
        System.out.println("Reservation: OK" + slotId);

        return "OK";
    }

    /* fallback Hystrix */
    public String deleteReservationFallback(Authentication auth, Integer id, HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
        return "That didn't work out! Try again later!";
    }
}
