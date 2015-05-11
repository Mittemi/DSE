package opPlanner.OPmatcher.controller;

import opPlanner.OPmatcher.dto.TimeSlot;
import opPlanner.OPmatcher.model.OPSlot;
import opPlanner.OPmatcher.repository.OPSlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.geo.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.GeospatialIndex;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.NearQuery;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.Map.*;

/**
 * Created by Thomas on 28.04.2015.
 */
@RestController
public class OPMatcherController {

    @Autowired
    OPSlotRepository repo;

    @Autowired
    MongoTemplate template;

    /**
     * Tries to find an op slot in a given time range and with a specified geo location
     *
     * @param x               - longitude
     * @param y               - latitude
     * @param perimeter       - desired perimeter
     * @param opSlotType      - type of op slot
     * @param freeDoctorSlots - time list of free slots provided by the doctor
     * @return matched op slot. If no OPSlot was found null is returned.
     */
    @RequestMapping(value = "/", method = RequestMethod.GET, produces = "application/json")
    public OPSlot findFreeSlot(double x, double y, int perimeter, TimeSlot preferredTimeWindow, String opSlotType, List<TimeSlot> freeDoctorSlots) {
        OPSlot chosenSlot = null;
        GeoResults<OPSlot> slots = findFreeSlotList(x, y, perimeter, preferredTimeWindow, opSlotType, freeDoctorSlots);
        if (slots.getContent().size() == 0) {
            return null;
        }
        //sort by start date
        Collections.sort(slots.getContent(),(GeoResult<OPSlot> slot1, GeoResult<OPSlot> slot2) -> slot1.getContent().getStart().compareTo(slot2.getContent().getStart()));
        chosenSlot = slots.getContent().get(0).getContent();    //choose first OPSlot
        //TODO thi: test sorting
        //TODO thi: notification, because of successful op slot matching
        return chosenSlot;
}

    /**
     * Adds a free op slot
     *
     * @param opSlot - op slot which should be added to the list of free slots
     */
    @RequestMapping(value = "/", method = RequestMethod.POST, produces = "application/json")
    public void addFreeOPSlot(OPSlot opSlot) {
        repo.save(opSlot);
    }

    /**
     * Deletes an free op slot (e.g. after reservation was made)
     *
     * @param opSlotId - id of op slot to delete
     */
    @RequestMapping(value = "/", method = RequestMethod.GET, produces = "application/json")
    public void deleteFreeOPSlot(String opSlotId) {
        repo.delete(opSlotId);
    }

    public GeoResults<OPSlot> findFreeSlotList(double x, double y, int preferredPerimeter, TimeSlot preferredTimeWindow, String opSlotType, List<TimeSlot> freeDoctorSlots) {
        template.indexOps(OPSlot.class).ensureIndex(new GeospatialIndex("position"));
        NearQuery nearQuery = NearQuery.near(x, y).maxDistance(new Distance(preferredPerimeter, Metrics.KILOMETERS));

        //filter by opSlotType
        if (opSlotType != null) {
            Query filterQuery = new Query(Criteria.where("type").is(opSlotType));
            nearQuery.query(filterQuery);
        }

        //filter by preferredTimeWindow
        if (preferredTimeWindow != null) {
            if (preferredTimeWindow.getStartTime() != null) {
                nearQuery.query(new Query(
                        Criteria.where("start").gte(preferredTimeWindow.getStartTime())));
                //TODO thi: test if query was not substituted
            }
            if (preferredTimeWindow.getEndTime() != null) {
                nearQuery.query(new Query(
                        Criteria.where("end").lte(preferredTimeWindow.getEndTime())));
            }
        }

        GeoResults<OPSlot> slots = template.geoNear(nearQuery, OPSlot.class);

        //filter by available doctor slots
        if (freeDoctorSlots != null && slots != null) {
            Iterator<GeoResult<OPSlot>> iter = slots.iterator();
            GeoResult<OPSlot> currentSlot;
            boolean slotOK = false;
            while (iter.hasNext()) {
                currentSlot = iter.next();
                for (TimeSlot doctorSlot : freeDoctorSlots) {
                    if (currentSlot.getContent().getStart().compareTo(doctorSlot.getStartTime()) >= 0
                            && currentSlot.getContent().getEnd().compareTo(doctorSlot.getEndTime()) <= 0) {
                        slotOK = true;
                        break;
                    }
                }
                //opSlot did not fit together with the doctors' free slots
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
