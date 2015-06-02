package opPlanner.OPmatcher.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import opPlanner.OPmatcher.OPPlannerProperties;
import opPlanner.OPmatcher.dto.Patient;
import opPlanner.OPmatcher.dto.TimeWindow;
import opPlanner.OPmatcher.model.OPSlot;
import opPlanner.OPmatcher.repository.OPSlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.geo.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.GeospatialIndex;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
import org.springframework.data.mongodb.core.query.NearQuery;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.hal.Jackson2HalModule;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Thomas on 28.04.2015.
 */
@RestController
public class OPMatcherController {

    @Autowired
    OPSlotRepository repo;

    @Autowired
    MongoTemplate template;

    @Autowired
    private OPPlannerProperties config;

    private RestTemplate restClient;

    public OPMatcherController() {
        restClient = new RestTemplate();
    }

    /**
     * Tries to find an op slot in a given time range and with a specified geo location
     * @param preferredPerimeter
     * @param preferredTimeWindow
     * @param opSlotType
     * @param doctorId
     * @param patientId
     * @return matched op slot, if no one was found null will be returned. Null will also be returned if patient or
     * opSlotType is null or simply if the work schedule of the doctor could not be retrieved.
     */
    @RequestMapping(value = "/findFreeSlot", method = RequestMethod.GET, produces = "application/json")
    public OPSlot findFreeSlot(Integer preferredPerimeter, TimeWindow preferredTimeWindow, String opSlotType,
                                    String doctorId, String patientId) {
        OPSlot chosenSlot = null;
       List<TimeWindow> workSchedule = findWorkScheduleByDoctor(doctorId, preferredTimeWindow.getStartTime(), preferredTimeWindow.getEndTime());
        Patient patient = findPatient(patientId);
        if (patient == null || workSchedule == null || opSlotType == null) {
            return null;
        }
        GeoResults<OPSlot> slots = findFreeSlotList(patient.getX(), patient.getY(), preferredPerimeter,
                preferredTimeWindow, opSlotType, workSchedule);
        if (slots.getContent().size() == 0) {
            return null;
        }
        chosenSlot = getEarliestOPSlot(slots);
        //TODO thi: notification, because of successful op slot matching
        return chosenSlot;

    }

    /**
     * finds the earliest upcoming op slot
     * @param slots
     * @return earliest op slot, if {@param slots} was found, null is returned.
     */
    private OPSlot getEarliestOPSlot(GeoResults<OPSlot> slots) {
        OPSlot earliestOPSlot = null;
        for (GeoResult<OPSlot> slot : slots) {
            if (earliestOPSlot == null || slot.getContent().getStart().compareTo(earliestOPSlot.getStart()) < 0) {
                earliestOPSlot = slot.getContent();
            }
        }
        return earliestOPSlot;
    }

    /**
     * accesses klinisys to find the free doctor slots by given a doctorId
     * @param doctorId
     * @param preferredStart
     * @param preferredEnd
     * @return found timeschedule if it could be retrieved and doctorId is not null, otherwise null
     */
    private List<TimeWindow> findWorkScheduleByDoctor(String doctorId, Date preferredStart, Date preferredEnd) {
        Map<String, Object> params = new HashMap<>();
        params.put("email", doctorId);
        String url=null;
        if (doctorId == null) return null;
        if (preferredStart != null && preferredEnd != null) {
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            params.put("startTime", formatter.format(preferredStart));
            params.put("endTime", formatter.format(preferredEnd));

            //e.g. http://localhost:9000/timeWindows/search/findByDoctorAndTimeWindow?email=d1@dse.at&from=2015-06-23%2009:01&to=2015-06-26%2015:00
            url = "http://" + config.getKlinisys().getIpOrHostname()
                    + ":" + config.getKlinisys().getPort()
                    + "/" + config.getKlinisys().getTimeWindowUrl();
        }
        else {
            url = "http://" + config.getKlinisys().getIpOrHostname()
                    + ":" + config.getKlinisys().getPort()
                    + "/" + config.getKlinisys().getTimeWindowAltUrl();
        }

        TimeWindow[] timeWindows = restClient.getForObject(url, TimeWindow[].class, params);
       // url = "http://127.0.0.1:9000/timeWindows/search/findByDoctorAndTimeWindow?email=d1@dse.at&from=2015-06-24%2015:00&to=2016-05-05%2015:00";
       // String stringRet = restClient.getForObject(url,String.class);

        System.out.println("Work schedule of doctor : " + doctorId + " retrieved.");

        return Arrays.asList(timeWindows);
    }

    private Patient findPatient(String patientId) {
        if (patientId == null)
            return null;
        Map<String, Object> params = new HashMap<>();
        params.put("email", patientId);
        String url = "http://" + config.getKlinisys().getIpOrHostname()
                + ":" + config.getKlinisys().getPort()
                + "/" + config.getKlinisys().getPatientUrl();

        Patient patient = restClient.getForObject(url, Patient.class, params);

        System.out.println("Retrieved patient: " + patient);

        return patient;
    }


    /**
     * Adds a free op slot
     *
     * @param opSlot - op slot which should be added to the list of free slots
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST, produces = "application/json")
    public void addFreeOPSlot(OPSlot opSlot) {
        repo.save(opSlot);
    }

    /**
     * Deletes an free op slot (e.g. after reservation was made)
     *
     * @param opSlotId - id of op slot to delete
     */
    @RequestMapping(value = "/delete/{opSlotId}", method = RequestMethod.DELETE, produces = "application/json")
    public void deleteFreeOPSlot(@PathVariable("opSlotId") String opSlotId) {
        repo.delete(opSlotId);
    }

    private Criteria appendCriteriaWithAND(Criteria mainCriteria, Criteria appendix) {
        if (mainCriteria == null) {
            return appendix;
        }
        return mainCriteria.andOperator(appendix);
    }

    public GeoResults<OPSlot> findFreeSlotList(double x, double y, int preferredPerimeter, TimeWindow preferredTimeWindow, String opSlotType, List<TimeWindow> freeDoctorSlots) {
        template.indexOps(OPSlot.class).ensureIndex(new GeospatialIndex("position"));
        NearQuery nearQuery = NearQuery.near(x, y).maxDistance(new Distance(preferredPerimeter, Metrics.KILOMETERS));

        //filter by opSlotType
        Criteria criteria = Criteria.where("type").is(opSlotType);
        Criteria startTimeCriteria = null;
        Criteria endTimeCriteria = null;


        //filter by preferredTimeWindow
        if (preferredTimeWindow != null) {
            if (preferredTimeWindow.getStartTime() != null) {
              startTimeCriteria = Criteria.where("start").gte(preferredTimeWindow.getStartTime());
                //nearQuery.query(new Query(
                       // Criteria.where("start").gte(preferredTimeWindow.getStartTime())));
                //TODO thi: test if query was not substituted
            }
            if (preferredTimeWindow.getEndTime() != null) {
                endTimeCriteria = Criteria.where("end").lte(preferredTimeWindow.getEndTime());
                //nearQuery.query(new Query(
                //        Criteria.where("end").lte(preferredTimeWindow.getEndTime())));
            }
        }

        criteria = criteria.andOperator(startTimeCriteria, endTimeCriteria);
        nearQuery.query(new Query(criteria)); //add filter criteria to the geo near query
        GeoResults<OPSlot> slots = template.geoNear(nearQuery, OPSlot.class);

        //filter by available doctor slots
        if (freeDoctorSlots != null && slots != null) {
            Iterator<GeoResult<OPSlot>> iter = slots.iterator();
            GeoResult<OPSlot> currentSlot;
            boolean slotOK = false;
            while (iter.hasNext()) {
                currentSlot = iter.next();
                for (TimeWindow doctorSlot : freeDoctorSlots) {
                    if (currentSlot.getContent().getStart().compareTo(doctorSlot.getStartTime()) >= 0
                            && currentSlot.getContent().getEnd().compareTo(doctorSlot.getEndTime()) <= 0) {
                        slotOK = true;
                        break;
                    }
                }
                //opSlot did not fit with the doctors' free slots
                if (!slotOK) {
                    iter.remove();
                }
                slotOK = false; //reset
            }
        }

        return slots;
    }

    /**
     * Tries to find many op slots in a given time range and with a specified geo location
     * @param x - longitude
     * @param y - latitude
     * @param perimeter - desired perimeter
     * @param opSlotType - type of op slot
     * @param freeDoctorSlots - time list of free slots provided by the doctor
     * @return matched op slot. If no OPSlot was found null is returned.
     */
//    public List<OPSlot> findFreeSlotList(double x, double y, int perimeter, String opSlotType, List<Entry<Date,Date>> freeDoctorSlots) {
//        template.indexOps(OPSlot.class).ensureIndex(new GeospatialIndex("position"));
//        List<OPSlot> slots = repo.findByPositionNear(new Point(x,y), new Distance(perimeter, Metrics.KILOMETERS) );
//        return slots;
//    }

}
