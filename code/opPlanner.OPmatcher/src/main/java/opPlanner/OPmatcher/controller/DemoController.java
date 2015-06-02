package opPlanner.OPmatcher.controller;

import opPlanner.OPmatcher.dto.TimeWindow;
import opPlanner.OPmatcher.model.OPSlot;
import opPlanner.OPmatcher.repository.OPSlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Thomas on 29.05.2015.
 */
@RestController
public class DemoController {

    @Autowired
    OPSlotRepository repo;

    @Autowired
    OPMatcherController opMatcher;

    private static final Point AKHWIEN = new Point(48.220589, 16.346794);
    private static final Point AKHLINZ = new Point(48.302743, 14.305328);
    private static final Point LKHGRAZ = new Point(47.081800, 15.466207);

    GregorianCalendar from1;
    GregorianCalendar from2;
    GregorianCalendar from3;
    GregorianCalendar to1;
    GregorianCalendar to2;
    GregorianCalendar to3;

    @RequestMapping(value = "/findFreeDemoSlot", method = RequestMethod.GET, produces = "application/json")
    public List<OPSlot> findFreeSlot(Integer preferredPerimeter, TimeWindow preferredTimeWindow, String opSlotType,
                                    String doctorId, String patientId) {
        OPSlot chosenSlot = null;
        System.out.println(preferredPerimeter + " - " + preferredTimeWindow + " - " + opSlotType + " - " + doctorId +
                "-" + patientId);
       /*GregorianCalendar from1 = new GregorianCalendar();
        from1.set(2015, 04, 20, 11, 00);
       GregorianCalendar to1 = new GregorianCalendar();
        to1.set(2015, 07, 28, 12, 00);
        List<TimeWindow> workSchedule = findWorkScheduleByDoctor("d1@dse.at",from1.getTime(), to1.getTime());*/

        // List<TimeWindow> workSchedule = findWorkScheduleByDoctor(doctorId, preferredTimeWindow.getStartTime(),
        //        preferredTimeWindow.getEndTime());
       /* Patient patient = findPatient(patientId);
        if (patient == null || workSchedule == null) {
            return null;
        }
        GeoResults<OPSlot> slots = findFreeSlotList(patient.getX(), patient.getY(), preferredPerimeter,
                preferredTimeWindow, opSlotType, workSchedule);
        GeoResults<OPSlot> slots = findFreeSlotList(patient.getX(), patient.getY(), preferredPerimeter,
                preferredTimeWindow, opSlotType, null);
        if (slots.getContent().size() == 0) {
            return null;
        }
        //sort by start date
        Collections.sort(slots.getContent(),(GeoResult<OPSlot> slot1, GeoResult<OPSlot> slot2) -> slot1.getContent().getStart().compareTo(slot2.getContent().getStart()));
        chosenSlot = slots.getContent().get(0).getContent();    //choose first OPSlot
        //TODO thi: test sorting
        //TODO thi: notification, because of successful op slot matching
        return chosenSlot;*/
        List<OPSlot> slots = new LinkedList<>();
        slots.add(new OPSlot(1,2,3, new Date(), new Date(),"eye"));
        return slots;
    }

    @RequestMapping(value = "/findDemoData", method = RequestMethod.GET, produces = "application/json")
    public List<OPSlot> getDemoData() {

        return repo.findAll();
    }

    @RequestMapping(value = "/demo", method = RequestMethod.GET, produces = "application/json")
    public String createDemoData() {
        from1 = new GregorianCalendar();
        from1.set(2015, 05, 23, 11, 00);
        from2 = new GregorianCalendar();
        from2.set(2015, 05, 24, 11, 00);
        from3 = new GregorianCalendar();
        from3.set(2015, 05, 25, 11, 00);
        to1 = new GregorianCalendar();
        to1.set(2015, 05, 23, 12, 00);
        to2 = new GregorianCalendar();
        to2.set(2015, 05, 24, 12, 00);
        to3 = new GregorianCalendar();
        to3.set(2015, 05, 25, 12, 00);

        OPSlot slotAKHVienna = new OPSlot(1, AKHWIEN.getX(), AKHWIEN.getY(), from1.getTime(), to1.getTime(), "eye");	//akh wien
        repo.save(slotAKHVienna);
        OPSlot slotAKHLinz = new OPSlot(2, AKHLINZ.getX(), AKHLINZ.getY(), from2.getTime(), to2.getTime(), "neuro");	//akh linz
        repo.save(slotAKHLinz);
        OPSlot slotLKHGraz = new OPSlot(3, LKHGRAZ.getX(), LKHGRAZ.getY() , from3.getTime(), to3.getTime(), "eye");		//lkh graz
        repo.save(slotLKHGraz);
        OPSlot slotLKHGraz2 = new OPSlot(3, LKHGRAZ.getX(), LKHGRAZ.getY() , from1.getTime(), to1.getTime(), "eye"); //lkh graz
        repo.save(slotLKHGraz2);

        return "OPMatcher demo data init done";
    }
}
