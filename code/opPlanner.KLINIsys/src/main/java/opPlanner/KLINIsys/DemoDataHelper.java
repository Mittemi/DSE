package opPlanner.KLINIsys;

import opPlanner.KLINIsys.model.Doctor;
import opPlanner.KLINIsys.model.Hospital;
import opPlanner.KLINIsys.model.OpSlot;
import opPlanner.KLINIsys.model.Patient;
import opPlanner.KLINIsys.repository.DoctorRepository;
import opPlanner.KLINIsys.repository.HospitalRepository;
import opPlanner.KLINIsys.repository.OpSlotRepository;
import opPlanner.KLINIsys.repository.PatientRepository;
import opPlanner.KLINIsys.service.AuthService;

import javax.print.Doc;
import java.util.Date;

/**
 * Created by Michael on 20.05.2015.
 */
public class DemoDataHelper {

    public static Hospital createHospital(HospitalRepository hospitalRepository, AuthService authService) {
        Hospital hospital = hospitalRepository.findByEmail("kh1@dse.at");
        if (hospital == null) {
            hospital = new Hospital();
            hospital.setName("Krankenhaus 1");
            hospital.setShortName("KH 1");
            hospital.setPassword(authService.encodePassword("password"));
            hospital.seteMail("kh1@dse.at");

            hospitalRepository.save(hospital);
        }

        return hospital;
    }

    public static Patient createPatient(PatientRepository patientRepository, AuthService authService) {

        Patient patient = patientRepository.findByEmail("p1@dse.at");
        if (patient == null) {
            patient = new Patient();
            patient.setName("Patient 1");
            patient.seteMail("p1@dse.at");
            patient.setX(48.208697);
            patient.setY(16.372265);
            patient.setPassword(authService.encodePassword("password"));

            patientRepository.save(patient);
        }
        return patient;

    }

    public static Doctor createDoctor(DoctorRepository doctorRepository, AuthService authService) {

        Doctor doctor = doctorRepository.findByEmail("d1@dse.at");
        if(doctor == null) {
            doctor = new Doctor();
            doctor.seteMail("d1@dse.at");
            doctor.setPassword(authService.encodePassword("password"));

            doctorRepository.save(doctor);
        }
        return doctor;
    }

    public static OpSlot createOpSlot(OpSlotRepository opSlotRepository, Doctor doctor, Hospital hospital) {
        OpSlot opSlot = new OpSlot();
        opSlot.setDoctor(doctor);
        opSlot.setHospital(hospital);

        opSlot.setSlotStart(new Date());
        opSlot.setSlotEnd(new Date());

        opSlot.setType("Demo");

        opSlotRepository.save(opSlot);
        return opSlot;
    }
}