package opPlanner.KLINIsys.controller;

import opPlanner.KLINIsys.DemoDataHelper;
import opPlanner.KLINIsys.model.*;
import opPlanner.KLINIsys.repository.*;
import opPlanner.KLINIsys.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.print.Doc;
import java.util.Date;

/**
 * Created by Michael on 28.04.2015.
 */
@RestController
public class DemoController {

    @Autowired
    private HospitalRepository hospitalRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private OpSlotRepository opSlotRepository;

    @Autowired
    private TimeWindowRepository timeWindowRepository;

    @Autowired
    private AuthService authService;

    @RequestMapping(value = "/demo", method = RequestMethod.GET, produces = "application/json")
    public String createDemoData() {

        Doctor doctor = DemoDataHelper.createDoctor(doctorRepository, timeWindowRepository, authService);
        Hospital hospital = DemoDataHelper.createHospital(hospitalRepository, authService);
        Patient patient = DemoDataHelper.createPatient(patientRepository,authService);
        OpSlot opSlot = DemoDataHelper.createOpSlot(opSlotRepository,doctor,hospital);

        return "Done";
    }
}
