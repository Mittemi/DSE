package opPlanner.KLINIsys;

import opPlanner.KLINIsys.model.*;
import opPlanner.KLINIsys.repository.*;
import opPlanner.KLINIsys.service.AuthService;

import javax.print.Doc;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

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

    public static Doctor createDoctor(DoctorRepository doctorRepository, TimeWindowRepository timeWindowRepository,
                                      AuthService authService) {

        Doctor doctor = doctorRepository.findByEmail("d1@dse.at");
        if(doctor == null) {
            doctor = new Doctor();
            doctor.seteMail("d1@dse.at");
            doctor.setPassword(authService.encodePassword("password"));

            doctorRepository.save(doctor);

            GregorianCalendar from1 = new GregorianCalendar();
            from1.set(2015, 05, 23, 9, 00);
            GregorianCalendar from2 = new GregorianCalendar();
            from2.set(2015, 05, 24, 10, 00);
            GregorianCalendar  from3 = new GregorianCalendar();
            from3.set(2015, 05, 25, 11, 00);
            GregorianCalendar  to1 = new GregorianCalendar();
            to1.set(2015, 05, 23, 13, 00);
            GregorianCalendar  to2 = new GregorianCalendar();
            to2.set(2015, 05, 24, 14, 00);
            GregorianCalendar to3 = new GregorianCalendar();
            to3.set(2015, 05, 25, 15, 00);

            TimeWindow slot1 = new TimeWindow(from1.getTime(), to1.getTime());
            slot1.setDoctor(doctor);
            TimeWindow slot2 = new TimeWindow(from2.getTime(), to2.getTime());
            slot2.setDoctor(doctor);
            TimeWindow slot3 = new TimeWindow(from3.getTime(), to3.getTime());
            slot3.setDoctor(doctor);
            List<TimeWindow> workSchedule = new ArrayList<>();
            doctor.setWorkSchedule(workSchedule);

            doctorRepository.save(doctor);
            timeWindowRepository.save(slot1);
            timeWindowRepository.save(slot2);
            timeWindowRepository.save(slot3);
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