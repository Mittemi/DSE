package opPlanner.KLINIsys.controller;

import opPlanner.KLINIsys.model.Doctor;
import opPlanner.KLINIsys.model.Hospital;
import opPlanner.KLINIsys.model.Patient;
import opPlanner.KLINIsys.repository.DoctorRepository;
import opPlanner.KLINIsys.repository.HospitalRepository;
import opPlanner.KLINIsys.repository.PatientRepository;
import opPlanner.KLINIsys.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
    private AuthService authService;

    @RequestMapping(value = "/demo", method = RequestMethod.GET, produces = "application/json")
    public String createDemoData() {


        if(hospitalRepository.findByEmail("kh1@dse.at") == null) {
            Hospital hospital = new Hospital();
            hospital.setName("Krankenhaus 1");
            hospital.setShortName("KH 1");
            hospital.setPassword(authService.encodePassword("password"));
            hospital.seteMail("kh1@dse.at");

            hospitalRepository.save(hospital);
        }

        if(patientRepository.findByEmail("p1@dse.at") == null) {
            Patient patient = new Patient();
            patient.setName("Patient 1");
            patient.seteMail("p1@dse.at");
            patient.setPassword(authService.encodePassword("password"));

            patientRepository.save(patient);
        }

        if(doctorRepository.findByEmail("d1@dse.at") == null) {
            Doctor doctor = new Doctor();
            doctor.seteMail("d1@dse.at");
            doctor.setPassword(authService.encodePassword("password"));

            doctorRepository.save(doctor);
        }

        return "Done";
    }
}
