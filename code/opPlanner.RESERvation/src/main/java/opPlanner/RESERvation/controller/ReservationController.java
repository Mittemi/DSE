package opPlanner.RESERvation.controller;

import opPlanner.RESERvation.OPPlannerProperties;
import opPlanner.RESERvation.dto.OPSlot;
import opPlanner.RESERvation.dto.Patient;
import opPlanner.RESERvation.model.Reservation;
import opPlanner.RESERvation.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
     * tries to reserve one op slot, if one was found by the opMatcher.
     * e.g. http://localhost:9002/reservation?preferredStart=2015-05-20%2010:00&preferredEnd=2015-05-27%2018:00&preferredPerimeter=500&opSlotType=eye&doctorId=d1@dse.at&patientId=p1@dse.at
     *
     * @param preferredStart
     * @param preferredEnd
     * @param preferredPerimeter
     * @param opSlotType
     * @param doctorId - unique doctor email address
     * @param patientId - unique patient email address
     * @return the found op slot which is being reserved after executing this method
     */
    @RequestMapping(value = "", method = RequestMethod.GET, produces = "application/json")
    public Reservation reserve(@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date preferredStart, @DateTimeFormat(pattern =
            "yyyy-MM-dd HH:mm") Date preferredEnd,
                        Integer preferredPerimeter, String opSlotType, String doctorId, String patientId) {

       // findFreeSlot(Integer preferredPerimeter, TimeWindow preferredTimeWindow, String opSlotType, String doctorId, String patientId)

        if (preferredStart == null || preferredEnd == null || preferredPerimeter == null || opSlotType == null || doctorId == null || patientId == null) {
            System.out.println("RESERvation - reserve(...): Missing parameter");
            return null;
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

        OPSlot opSlot = restClient.getForObject(url, OPSlot.class, params);

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

        //todo thi create method for retrieving reserved doctors' op slots. for checking availability

        return reservation;

        //TODO thi: implement reserve
        //TODO thi: how to call other REST services?
        //TODO thi: unique id, in order to avoid duplicates
    }
}
