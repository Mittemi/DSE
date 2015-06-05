package opPlanner.RESERvation.controller;

import opPlanner.RESERvation.OPPlannerProperties;
import opPlanner.RESERvation.dto.OPSlot;
import opPlanner.RESERvation.dto.Patient;
import opPlanner.RESERvation.model.Reservation;
import opPlanner.RESERvation.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import javax.xml.ws.Response;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Thomas on 10.05.2015.
 */
@RestController
@RequestMapping("/reservation")
public class ReservationController {

    @Autowired
    ReservationRepository repo;

    @Autowired
    OPPlannerProperties config;

    RestTemplate restClient;

    @Autowired
    MongoTemplate template;

    public ReservationController()
    {
        restClient = new RestTemplate();
    }


    @RequestMapping(value = "/findReservationsByPatientIdAndTW", method = RequestMethod.GET, produces = "application/json")
    public List<Reservation> findReservationsByPatientId(@RequestParam(value="patientId")String patientId, @RequestParam
            (value="start", required = false)@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")Date start, @RequestParam
                                                                (value="end", required = false)
                                                        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")Date end) {
        List<Reservation> reservations = repo.findByPatientAndTimeWindow(patientId,start,end);
        return reservations;
    }

    /**
     * tries to reserve one op slot, if one was found by the opMatcher.
     * e.g. http://localhost:9002/reservation?preferredStart=2015-05-20%2010:00&preferredEnd=2015-05-27%2018:00&preferredPerimeter=500&opSlotType=eye&doctorId=d1@dse.at&patientId=p1@dse.at
     *
     * @param preferredStart
     * @param preferredEnd
     * @param preferredPerimeter
     * @param opSlotType
     * @param doctorId - unique doctor email address
     * @param patientId - unique patient email address
     * @return the found op slot which is being reserved after executing this method, if no one was found the
     * reservation will not be processed and null is returned.
     */
    @RequestMapping(value = "/reserve", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<String> reserve(@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date preferredStart,
                                       @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date preferredEnd,
                        Integer preferredPerimeter, String opSlotType, String doctorId, String patientId) {

        if (preferredStart == null || preferredEnd == null || preferredPerimeter == null
                || opSlotType == null || doctorId == null || patientId == null) {
            System.out.println("RESERvation - reserve(...): Missing parameter");
            return new ResponseEntity<String>("missing parameter.", HttpStatus.BAD_REQUEST);
        }

        Map<String, Object> params = new HashMap<>();
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        params.put("startTime", formatter.format(preferredStart));
        params.put("endTime", formatter.format(preferredEnd));
        params.put("preferredPerimeter", preferredPerimeter);
        params.put("opSlotType", opSlotType);
        params.put("doctorId", doctorId);
        params.put("patientId", patientId);
        String url = "http://" + config.getOpMatcher().getIpOrHostname()
                + ":" + config.getOpMatcher().getPort()
                + "/" + config.getOpMatcher().getFindOPSlotUrl();

        //request the op matcher to find one suitable slot
        ResponseEntity<OPSlot> response;
        try {
            response = restClient.getForEntity(url, OPSlot.class, params);
        } catch (HttpServerErrorException e) {
            //todo thi: notify about failed reservation
            return new ResponseEntity<String>("OP Matcher does not work properly: \n" + e.getMessage(), HttpStatus
                    .NOT_FOUND);
        } catch (ResourceAccessException e) {
            return new ResponseEntity<String>("OP Matcher is not accessible: \n" + e.getMessage(), HttpStatus
                    .NOT_FOUND);
        }
        OPSlot opSlot = response.getBody();
        if (opSlot == null) {
            //todo thi: notify about failed reservation
            return new ResponseEntity<String>("no op slot could be found", HttpStatus.NOT_FOUND);
        }

        //save the opSlot as reserved
        Reservation reservation = new Reservation(opSlot.getId(), opSlot.getStart(), opSlot.getEnd(), patientId,
                doctorId);
        repo.save(reservation);

        //delete the free op slot after successful reservation
        url = "http://" + config.getOpMatcher().getIpOrHostname()
                + ":" + config.getOpMatcher().getPort()
                + "/" + config.getOpMatcher().getDeleteFreeOPSlotUrl();
        params = new HashMap<>();
        params.put("opSlotId", opSlot.getId());
        restClient.delete(url, params);

        //todo thi: notify about successful reservation

        return new ResponseEntity<String>("reservation done", HttpStatus.OK);
    }


    /**
     * cancels an existing reservation
     * @param opSlotId
     * @return cancelled reservation, or null if the corresponding reservation or op slot were not found.
     */
    @RequestMapping(value = "/cancelByOpSlotId", method = RequestMethod.DELETE, produces = "application/json")
    public ResponseEntity<Reservation> cancelReservation(@RequestParam String opSlotId) {
        //todo thi: send notification

        Reservation reservation = null;
        Map<String, Object> params = new HashMap<>();
        params.put("opSlotId", opSlotId);
        String url = "http://" + config.getOpMatcher().getIpOrHostname()
                + ":" + config.getOpMatcher().getPort()
                + "/" + config.getOpMatcher().getAddOPSlotByIdUrl();

        //request the op matcher to add the op slot to the free op slots again
        ResponseEntity<String> entity;
        try {
            entity = restClient.getForEntity(url, String.class, params);
        } catch (HttpClientErrorException e) {
            return new ResponseEntity<Reservation>(reservation, HttpStatus.NOT_FOUND);
        }
        String body = entity.getBody();

        System.out.println("Reservation: reservation with op slot : " +body+ " cancelled.");

        //perform actual delete
        List<Reservation> deletedReservations = repo.deleteReservationByOpSlotId(opSlotId);
        if (deletedReservations.size() != 1) {
            return new ResponseEntity<Reservation>(reservation, HttpStatus.NOT_FOUND);
        }
        reservation = deletedReservations.get(0);
        return new ResponseEntity<Reservation>(reservation, HttpStatus.OK);
    }
    // todo: a list as get parameter won't work in reality... an url has to be shorter than 2000 chars!!!!
    // todo: thi
    @RequestMapping(value = "/findReservationsByOPSlots", method = RequestMethod.GET, produces = "application/json")
    public List<Reservation> findReservationsByOPSlots(@RequestParam(value="opSlotId")List<String> opSlotIds) {
        List<Reservation> reservations = repo.findByOpSlotIdIn(opSlotIds);
        return reservations;
    }

    @RequestMapping(value = "/findReservationsByDoctorId", method = RequestMethod.GET, produces = "application/json")
    public List<Reservation> findReservationsByDoctorId(@RequestParam(value="doctorId")String doctorId) {
        List<Reservation> reservations = repo.findByDoctorId(doctorId);
        return reservations;
    }

    @RequestMapping(value = "/findReservationsByDoctorIdAndTW", method = RequestMethod.GET, produces =
            "application/json")
    public List<Reservation> findReservationsByDoctorId(@RequestParam(value="doctorId")String doctorId, @RequestParam
            (value="start", required = false)@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")Date start, @RequestParam
            (value="end", required = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")Date end) {
        List<Reservation> reservations = repo.findByDoctorAndTimeWindow(doctorId, start, end);
        return reservations;
    }

    @RequestMapping(value = "/findAllReservations", method = RequestMethod.GET, produces = "application/json")
    public List<Reservation> findAllReservations() {
        List<Reservation> reservations = repo.findAll();
        return reservations;
    }


}
