package opPlanner.RESERvation.controller;

import opPlanner.RESERvation.OPPlannerProperties;
import opPlanner.RESERvation.dto.NotificationDTO;
import opPlanner.RESERvation.dto.OPSlot;
import opPlanner.RESERvation.model.Reservation;
import opPlanner.RESERvation.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

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


    /**
     * finds a list of reservations by considering a given patient id and a time window
     * @param patientId
     * @param start
     * @param end
     * @return
     */
    @RequestMapping(value = "/findReservationsByPatientIdAndTW", method = RequestMethod.GET, produces = "application/json")
    public List<Reservation> findReservationsByPatientIdAndTW(@RequestParam(value="patientId")String patientId, @RequestParam
            (value="start")@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")Date start, @RequestParam
                                                                (value="end")
                                                        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")Date end) {
        List<Reservation> reservations = repo.findByPatientAndTimeWindow(patientId, start, end);
        return reservations;
    }


    /**
     * finds a list of reservations of one patient with given patient id.
     * @param patientId
     * @return
     */
    @RequestMapping(value = "/findReservationsByPatientId", method = RequestMethod.GET, produces = "application/json")
    public List<Reservation> findReservationsByPatientId(@RequestParam(value="patientId")String patientId) {
        List<Reservation> reservations = repo.findByPatientId(patientId);

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
            sendReservationFailureMessage("reservation", patientId, doctorId, preferredStart, preferredEnd);
            return new ResponseEntity<String>("missing parameter.", HttpStatus.BAD_REQUEST);
        }

        //set request parameter for the op matcher
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
            sendReservationFailureMessage("reservation", patientId, doctorId, preferredStart, preferredEnd);
            return new ResponseEntity<String>("OP Matcher does not work properly: \n" + e.getMessage(), HttpStatus
                    .NOT_FOUND);
        } catch (ResourceAccessException e) {
            sendReservationFailureMessage("reservation", patientId, doctorId, preferredStart, preferredEnd);
            return new ResponseEntity<String>("OP Matcher is not accessible: \n" + e.getMessage(), HttpStatus
                    .NOT_FOUND);
        }
        OPSlot opSlot = response.getBody();
        if (opSlot == null) {
            //send a success message to all involved parties (doctor, patient)
            sendReservationFailureMessage("reservation", patientId, doctorId, preferredStart, preferredEnd);
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

        //send a success message to all involved parties (hospital, doctor, patient)
        String message = "The reservation with of op slot "
                + reservation.getOpSlotId()
                +" (" + reservation.getStart() + "-" + reservation.getEnd() +") "
                +" suceeded. Hospital: " + opSlot.getHospitalId() + " Doctor: " +  reservation.getDoctorId() + " " +
                "Patient: " + reservation.getPatientId();
        sendNotification("Reservation succeeded", patientId, message);
        sendNotification("Reservation succeeded", doctorId, message);
        sendNotification("Reservation succeeded", opSlot.getHospitalId(), message);

        return new ResponseEntity<String>("reservation done", HttpStatus.OK);
    }


    /**
     * cancels an existing reservation
     * @param opSlotId
     * @return cancelled reservation, or null if the corresponding reservation or op slot were not found.
     */
    @RequestMapping(value = "/cancelByOpSlotId", method = RequestMethod.DELETE, produces = "application/json")
    public ResponseEntity<Reservation> cancelReservation(@RequestParam String opSlotId, @RequestParam String doctorId) {

        Reservation reservation = null;
        Map<String, Object> params = new HashMap<>();

        //check weather the doctor created the op slot
        reservation = repo.findByOpSlotId(opSlotId);
        if (reservation == null || !doctorId.equals(reservation.getDoctorId())) {
            sendCancellationFailureMessage(opSlotId, doctorId, null);
            return new ResponseEntity<Reservation>(reservation, HttpStatus.NOT_ACCEPTABLE);
        }

        params.put("opSlotId", opSlotId);
        String url = "http://" + config.getOpMatcher().getIpOrHostname()
                + ":" + config.getOpMatcher().getPort()
                + "/" + config.getOpMatcher().getAddOPSlotByIdUrl();

        //request the op matcher to add the op slot to the free op slots again
        ResponseEntity<String> entity;
        try {
            entity = restClient.getForEntity(url, String.class, params);
        } catch (HttpClientErrorException e) {
            sendCancellationFailureMessage(opSlotId, doctorId, reservation.getPatientId());
            return new ResponseEntity<Reservation>(reservation, HttpStatus.NOT_FOUND);
        }
        String body = entity.getBody();

        System.out.println("Reservation: reservation with op slot : " +body+ " cancelled.");

        //perform actual delete
        List<Reservation> deletedReservations = repo.deleteReservationByOpSlotId(opSlotId);
        if (deletedReservations.size() != 1) {
            sendCancellationFailureMessage(opSlotId, doctorId, reservation.getPatientId());
            return new ResponseEntity<Reservation>(reservation, HttpStatus.NOT_FOUND);
        }
        reservation = deletedReservations.get(0);
        sendCancellationSuccessMessage(opSlotId, doctorId, reservation.getPatientId());

        return new ResponseEntity<Reservation>(reservation, HttpStatus.OK);
    }
    // todo: thi
    @RequestMapping(value = "/findReservationsByOPSlots", method = RequestMethod.POST, produces = "application/json")
    public List<Reservation> findReservationsByOPSlots(@RequestBody String[] opSlotIds) {
        List<Reservation> reservations = repo.findByOpSlotIdIn(Arrays.asList(opSlotIds));
        return reservations;
    }

    @RequestMapping(value = "/findReservationsByDoctorId", method = RequestMethod.GET, produces = "application/json")
    public List<Reservation> findReservationsByDoctorId(@RequestParam(value="doctorId") String doctorId) {
        List<Reservation> reservations = repo.findByDoctorId(doctorId);
        return reservations;
    }

    @RequestMapping(value = "/findReservationsByDoctorIdAndTW", method = RequestMethod.GET, produces =
            "application/json")
    public List<Reservation> findReservationsByDoctorId(@RequestParam(value="doctorId")String doctorId, @RequestParam
            (value="start", required = false)@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")Date start, @RequestParam
            (value="end", required = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date end) {
        List<Reservation> reservations = repo.findByDoctorAndTimeWindow(doctorId, start, end);
        return reservations;
    }

    @RequestMapping(value = "/findAllReservations", method = RequestMethod.GET, produces = "application/json")
    public List<Reservation> findAllReservations() {
        List<Reservation> reservations = repo.findAll();
        return reservations;
    }

    /**
     * concatenates a failure message and sends it to the NOTifier.
     * @param patientId
     * @param doctorId
     * @param preferredStart
     * @param preferredEnd
     * @param activity (e.g. reservation or cancellation)
     */
    private void sendReservationFailureMessage(String activity, String patientId, String doctorId,
                                               Date preferredStart, Date preferredEnd) {
        String message = "The "+ activity+ " of an op slot with between "
                + preferredStart +" and " +  preferredEnd
                +" failed. Doctor: " +  doctorId + " " +
                "Patient: " + patientId;
        sendNotification("Reservation failed", patientId, message);
        sendNotification("Reservation failed", doctorId, message);
    }

    /**
     * sends a cancellation failure message to the NOTifier
     * @param doctorId not null
     * @param patientId nullable
     */
    private void sendCancellationFailureMessage(String opSlotId, String doctorId, String patientId) {
        String message = "The cancellation of a reservation with opSlotId: "
                + opSlotId
                +" failed. Doctor: " +  doctorId;
        sendNotification("Reservation failed", doctorId, message);
        if (patientId != null) {
            message += " Patient: " +patientId;
            sendNotification("Cancellation failed", patientId, message);
        }
    }

    /**
     * sends a success message of cancellation to the NOTifier
     * @param opSlotId
     * @param doctorId
     * @param patientId
     */
    private void sendCancellationSuccessMessage(String opSlotId, String doctorId, String patientId) {
        String message = "The cancellation of a reservation with opSlotId: "
                + opSlotId
                +" suceeded. Doctor: " +  doctorId
                +" Patient: " +patientId;
        sendNotification("Cancellation succeded.", doctorId, message);
        sendNotification("Cancellation succeded.", patientId, message);
    }

    @RequestMapping(value = "/demo")
    public void demo() {
        sendNotification("asdf", "asdf", "asdf");
    }

    /**
     * sends a message to the NOTifier, via REST-Service call
     * @param subject
     * @param message
     * @param recipient
     */
    private void sendNotification(String subject, String recipient, String message) {
        NotificationDTO notificationDTO = new NotificationDTO(recipient, message, subject);
        String url = "http://" + config.getNotifier().getIpOrHostname()
                + ":" + config.getNotifier().getPort()
                + "/" + config.getNotifier().getCreateUrl();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<NotificationDTO> entity = new HttpEntity<>(notificationDTO, headers);

        restClient.put(url, entity);
        //System.out.println(responseEntity.getBody());
    }


}
