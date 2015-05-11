package opPlanner.RESERvation.controller;

import opPlanner.RESERvation.dto.Patient;
import opPlanner.RESERvation.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * Created by Thomas on 10.05.2015.
 */
@RestController
public class ReservationController {

    @Autowired
    ReservationRepository repo;

    @Autowired
    MongoTemplate template;

    @RequestMapping(value = "/", method = RequestMethod.GET, produces = "application/json")
    public void reserve(Date preferredStart, Date preferredEnd, int preferredPerimeter, String DoctorId, Patient Patient) {
        //TODO thi: implement reserve
        //TODO thi: how to call other REST services?
    }
}
