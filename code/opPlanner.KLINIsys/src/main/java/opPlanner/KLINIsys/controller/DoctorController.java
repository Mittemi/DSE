package opPlanner.KLINIsys.controller;

import opPlanner.KLINIsys.model.Doctor;
import opPlanner.KLINIsys.model.Patient;
import opPlanner.KLINIsys.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
