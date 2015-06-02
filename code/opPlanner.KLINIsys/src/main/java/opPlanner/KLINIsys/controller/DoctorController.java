package opPlanner.KLINIsys.controller;

import opPlanner.KLINIsys.model.Doctor;
import opPlanner.KLINIsys.model.Patient;
import opPlanner.KLINIsys.model.TimeWindow;
import opPlanner.KLINIsys.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * Created by Michael on 28.04.2015.
 */
@RestController
@RequestMapping("/doctor")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @RequestMapping(value = "/", method = RequestMethod.GET, produces = "application/json")
    public Iterable<Doctor> index() {
        return doctorService.allDoctors();
    }

}
