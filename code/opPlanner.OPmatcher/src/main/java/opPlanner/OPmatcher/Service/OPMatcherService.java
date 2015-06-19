package opPlanner.OPmatcher.Service;

import opPlanner.OPmatcher.OPPlannerProperties;
import opPlanner.OPmatcher.dto.Patient;
import opPlanner.OPmatcher.dto.Reservation;
import opPlanner.OPmatcher.dto.TimeWindow;
import opPlanner.OPmatcher.model.OPSlot;
import opPlanner.OPmatcher.repository.OPSlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Metrics;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.GeospatialIndex;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.NearQuery;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Thomas on 02.06.2015.
 */
@Service
public class OPMatcherService {

    @Autowired
    OPSlotRepository repo;

    @Autowired
    MongoTemplate template;

    @Autowired
    private OPPlannerProperties config;

    private RestTemplate restClient;

    public OPMatcherService() {
        restClient = new RestTemplate();
    }


    public OPSlot findFreeSlot(Integer preferredPerimeter, TimeWindow preferredTimeWindow, String opSlotType,
                               String doctorId, String patientId) {
        OPSlot chosenSlot = null;
        List<TimeWindow> workSchedule = findWorkScheduleByDoctor(doctorId, preferredTimeWindow.getStartTime(), preferredTimeWindow.getEndTime());
        Patient patient = findPatient(patientId);
        if (patient == null || workSchedule == null || opSlotType == null) {
            return null;
        }
        //find the doctors' existing reservations
        List<Reservation> reservations = getReservationsOfDoctor(doctorId, preferredTimeWindow.getStartTime(),
                preferredTimeWindow.getEndTime());

        //op matching
        GeoResults<OPSlot> slots = findFreeSlotList(patient.getX(), patient.getY(), preferredPerimeter,
                preferredTimeWindow, opSlotType, workSchedule, reservations);

        if (slots.getContent().size() == 0) {
            return null;
        }
        chosenSlot = getEarliestOPSlot(slots);
        //TODO thi: notification, because of successful op slot matching
        return chosenSlot;
    }

    /**
     * does the op matching.
     * @param x
     * @param y
     * @param preferredPerimeter
     * @param preferredTimeWindow
     * @param opSlotType
     * @param freeDoctorSlots
     * @param doctorReservations - slots where the doctor is already booked for some surgeries.
     * @return
     */
    public GeoResults<OPSlot> findFreeSlotList(double x, double y, int preferredPerimeter,
                                               TimeWindow preferredTimeWindow, String opSlotType, List<TimeWindow>
                                                       freeDoctorSlots, List<Reservation> doctorReservations) {

        template.indexOps(OPSlot.class).ensureIndex(new GeospatialIndex("position"));
        NearQuery nearQuery = NearQuery.near(x, y).maxDistance(new Distance(preferredPerimeter, Metrics.KILOMETERS));
        Criteria criteria = new Criteria();
        List<Criteria> criteriaList = new LinkedList<>();
        Criteria startTimeCriteria = null;
        Criteria endTimeCriteria = null;

        //filter by opSlotType
        if (opSlotType != null) {
            criteria = Criteria.where("type").is(opSlotType);
        }

        //filter by preferredTimeWindow
        if (preferredTimeWindow != null) {
            if (preferredTimeWindow.getStartTime() != null) {
                startTimeCriteria = Criteria.where("start").gte(preferredTimeWindow.getStartTime());
                criteriaList.add(startTimeCriteria);
            }
            if (preferredTimeWindow.getEndTime() != null) {
                endTimeCriteria = Criteria.where("end").lte(preferredTimeWindow.getEndTime());
                criteriaList.add(endTimeCriteria);
            }
        }

        if (criteriaList.size() > 0) {
            criteria.andOperator(criteriaList.toArray(new Criteria[criteriaList.size()]));
        }
        nearQuery.query(new Query(criteria)); //add filter criteria to the geo near query
        GeoResults<OPSlot> slots = template.geoNear(nearQuery, OPSlot.class); //find nearest slots

        //filter by available doctor slots
        if (freeDoctorSlots != null && slots != null) {
            Iterator<GeoResult<OPSlot>> iter = slots.iterator();
            GeoResult<OPSlot> currentSlot;
            boolean slotOK = false;
            while (iter.hasNext()) {
                currentSlot = iter.next();
                for (TimeWindow doctorSlot : freeDoctorSlots) {
                    //determines whether the doctor is working on that timeslot and if no reservation exists for him
                    // at that time
                    if (currentSlot.getContent().getStart().compareTo(doctorSlot.getStartTime()) >= 0

                            && currentSlot.getContent().getEnd().compareTo(doctorSlot.getEndTime()) <= 0
                            && !isSlotAlreadyReserved(currentSlot.getContent().getStart(), currentSlot.getContent()
                            .getEnd(), doctorReservations)) {
                        slotOK = true;
                        break;
                    }
                }
                //opSlot did not fit with the doctors' free slots
                if (!slotOK) {
                    System.out.println("Doctor is not working! Slot: " + currentSlot);
                    iter.remove();
                }
                slotOK = false; //reset
            }
        }

        return slots;
    }

    /**
     * persists one added free op slot
     * @param opSlot
     */
    public void addFreeOPSlot(OPSlot opSlot) {
        repo.save(opSlot);
    }

    /**
     * removes one free op slot
     * @param opSlotId
     */
    public boolean deleteFreeOPSlot(String opSlotId) {

        boolean exist = repo.exists(opSlotId);

        if(exist) {
            System.out.println("Slot removed " + opSlotId);
            repo.delete(opSlotId);
        } else {
            System.out.println("Unmanaged slot -> continue");  
        }
        return true;
    }


    /**
     * requests RESERvation for all reservations of the doctor
     * @param doctorId
     * @param start
     * @param end
     * @return
     */
    private List<Reservation> getReservationsOfDoctor(String doctorId, Date start, Date end) {
        Map<String, Object> params = new HashMap<>();
        params.put("doctorId", doctorId);
        String url=null;
        Reservation[] reservations;
        if (doctorId == null) return null;
        if (start != null && end != null) {
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            params.put("start", formatter.format(start));
            params.put("end", formatter.format(end));

            url = "http://" + config.getReservation().getIpOrHostname()
                    + ":" + config.getReservation().getPort()
                    + "/" + config.getReservation().getFindReservationsByDoctorIdAndTWURL();

            reservations = restClient.getForObject(url, Reservation[].class, params);
        }
        else {
            url = "http://" + config.getReservation().getIpOrHostname()
                    + ":" + config.getReservation().getPort()
                    + "/" + config.getReservation().getReservationByDoctorUrl();

            reservations = restClient.getForObject(url, Reservation[].class, params);
        }
        System.out.println("Reservations of doctor : " + doctorId + " retrieved.");
        return Arrays.asList(reservations);
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

        System.out.println("Work schedule of doctor : " + doctorId + " retrieved.");

        return Arrays.asList(timeWindows);
    }

    /**
     * retrieves the patient with the given patientId from the KLINIsys service
     * @param patientId
     * @return
     */
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
     * retrieves the op slot with the given id from KLINISys and saves it to the OPMatcher data storage.
     * @param opSlotId
     * @return added op slot, null if no op slot could be found from KLiniSys and therefore not be added.
     */
    public OPSlot addFreeOPSlotById(String opSlotId) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", opSlotId);
        String url = "http://" + config.getKlinisys().getIpOrHostname()
                + ":" + config.getKlinisys().getPort()
                + "/" + config.getKlinisys().getOpSlotUrl();

        OPSlot opSlot = restClient.getForObject(url, OPSlot.class, params);
        if (opSlot != null) {
            System.out.println("Slot added " + opSlotId);
            repo.save(opSlot);
        }
        return opSlot;
    }

    private boolean isSlotAlreadyReserved(Date slotStart, Date slotEnd, List<Reservation> reservations) {
        if (reservations == null) {
            return  false;
        }
        for (Reservation reservation : reservations) {
            if (overlap(slotStart, slotEnd, reservation.getStart(), reservation.getEnd())) {
                return true;
            }
        }
        return false;
    }

    private boolean overlap(Date start1, Date end1, Date start2, Date end2){
        return start1.getTime() <= end2.getTime() && start2.getTime() <= end1.getTime();
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
