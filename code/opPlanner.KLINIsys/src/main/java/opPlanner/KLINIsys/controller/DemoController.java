package opPlanner.KLINIsys.controller;

import opPlanner.KLINIsys.model.*;
import opPlanner.KLINIsys.repository.DoctorRepository;
import opPlanner.KLINIsys.repository.HospitalRepository;
import opPlanner.KLINIsys.repository.OpSlotRepository;
import opPlanner.KLINIsys.repository.PatientRepository;
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
    private AuthService authService;

    @RequestMapping(value = "/demo", method = RequestMethod.GET, produces = "application/json")
    public String createDemoData() {


        Hospital hospital = hospitalRepository.findByEmail("kh1@dse.at");
        if(hospital == null) {
            hospital = new Hospital();
            hospital.setName("Krankenhaus 1");
            hospital.setShortName("KH 1");
            hospital.setPassword(authService.encodePassword("password"));
            hospital.seteMail("kh1@dse.at");

            hospitalRepository.save(hospital);
        }

        Patient patient = patientRepository.findByEmail("p1@dse.at");
        if(patient == null) {
            patient = new Patient();
            patient.setName("Patient 1");
            patient.seteMail("p1@dse.at");
            patient.setX(48.208697);
            patient.setY(16.372265);
            patient.setPassword(authService.encodePassword("password"));

            patientRepository.save(patient);
        }

        Doctor doctor = doctorRepository.findByEmail("d1@dse.at");
        if(doctor == null) {
            doctor = new Doctor();
            doctor.seteMail("d1@dse.at");
            doctor.setPassword(authService.encodePassword("password"));

            doctorRepository.save(doctor);
        }

        OpSlot opSlot = new OpSlot();
        opSlot.setDoctor(doctor);
        opSlot.setHospital(hospital);
        opSlot.setType("Demo");
        opSlot.setSlotStart(new Date());
        opSlot.setSlotEnd(new Date());

        opSlotRepository.save(opSlot);

        return "Done";
    }
}
