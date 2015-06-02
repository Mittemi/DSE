package opPlanner.KLINIsys.controller;

import opPlanner.KLINIsys.model.Patient;
import opPlanner.KLINIsys.repository.PatientRepository;
import opPlanner.KLINIsys.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Michael on 28.04.2015.
 */
@RestController
@RequestMapping("/patient")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @RequestMapping(value = "/", method = RequestMethod.GET, produces = "application/json")
    public Iterable<Patient> index() {
        return patientService.allPatients();
    }

    @RequestMapping(value = "/findPatientByEmail", method = RequestMethod.GET, produces = "application/json")
    public Patient getPatientByEmail(@RequestParam("email")String eMail) {
        return patientService.getPatientByEmail(eMail);
    }
}
