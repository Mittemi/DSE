package opPlanner.OPmatcher.controller;

import opPlanner.OPmatcher.model.OPSlot;
import opPlanner.OPmatcher.repository.OPSlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.GeospatialIndex;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
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
     * @param x - longitude
     * @param y - latitude
     * @param perimeter - desired perimeter
     * @param opSlotType - type of op slot
     * @param freeDoctorSlots - time list of free slots provided by the doctor
     * @return matched op slot. If no OPSlot was found null is returned.
     */
    @RequestMapping(value = "/", method = RequestMethod.GET, produces = "application/json")
    public OPSlot findFreeSlot(double x, double y, int perimeter, String opSlotType, List<Entry<Date,Date>> freeDoctorSlots)
    {
        //TODO Query formulieren ohne generiertem Repository. (mit Kriterien: perimter, opSlotType und freeDoctorSlots)
        //TODO Rückgabe an Reservation und Implementieren der dortigen Logik.
        return null;
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
    public List<OPSlot> findFreeSlotList(double x, double y, int perimeter, String opSlotType, List<Entry<Date,Date>> freeDoctorSlots) {
        template.indexOps(OPSlot.class).ensureIndex(new GeospatialIndex("position"));
        List<OPSlot> slots = repo.findByPositionNear(new Point(x,y), new Distance(perimeter, Metrics.KILOMETERS) );
        return slots;
    }
}
