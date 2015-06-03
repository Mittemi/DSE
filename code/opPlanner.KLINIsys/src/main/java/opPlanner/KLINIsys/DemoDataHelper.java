package opPlanner.KLINIsys;

import opPlanner.KLINIsys.model.*;
import opPlanner.KLINIsys.repository.*;
import opPlanner.KLINIsys.service.AuthService;
import org.springframework.data.geo.Point;

import javax.print.Doc;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by Michael on 20.05.2015.
 */
public class DemoDataHelper {

    private static final Point AKHWIEN = new Point(48.220589, 16.346794);
    private static final Point AKHLINZ = new Point(48.302743, 14.305328);
    private static final Point LKHGRAZ = new Point(47.081800, 15.466207);

    public static void createMoreHospitalsWithOPSlots(HospitalRepository hospitalRepository,
                                                      OpSlotRepository opSlotRepository, AuthService authService) {

        GregorianCalendar from1 = new GregorianCalendar();
        from1.set(2015, 05, 23, 9, 55);
        GregorianCalendar   from2 = new GregorianCalendar();
        from2.set(2015, 05, 24, 10, 55);
        GregorianCalendar  from3 = new GregorianCalendar();
        from3.set(2015, 05, 25, 11, 55);
        GregorianCalendar  to1 = new GregorianCalendar();
        to1.set(2015, 05, 23, 11, 05);
        GregorianCalendar  to2 = new GregorianCalendar();
        to2.set(2015, 05, 24, 12, 05);
        GregorianCalendar  to3 = new GregorianCalendar();
        to3.set(2015, 05, 25, 13, 05);

        OpSlot opSlot;
        Hospital akhWien = hospitalRepository.findByEmail("kh1@dse.at");

        if (akhWien == null) {
            akhWien = new Hospital();
            akhWien.setName("Krankenhaus 1 - AKH Wien");
            akhWien.setShortName("AKHW");
            akhWien.setPassword(authService.encodePassword("password"));
            akhWien.seteMail("kh1@dse.at");
            akhWien.setX(AKHWIEN.getX());
            akhWien.setY(AKHWIEN.getY());
            hospitalRepository.save(akhWien);

            opSlot = new OpSlot();
            opSlot.setHospital(akhWien);

            opSlot.setSlotStart(from1.getTime());
            opSlot.setSlotEnd(to1.getTime());

            opSlot.setType("eye");

            opSlotRepository.save(opSlot);

        }
        Hospital akhLinz = hospitalRepository.findByEmail("kh2@dse.at");
        if (akhLinz == null) {
            akhLinz = new Hospital();
            akhLinz.setName("Krankenhaus 2 - LKH Graz");
            akhLinz.setShortName("AKHL");
            akhLinz.setPassword(authService.encodePassword("password"));
            akhLinz.seteMail("kh2@dse.at");
            akhLinz.setX(AKHLINZ.getX());
            akhLinz.setY(AKHLINZ.getY());
            hospitalRepository.save(akhLinz);

            opSlot = new OpSlot();
            opSlot.setHospital(akhLinz);

            opSlot.setSlotStart(from2.getTime());
            opSlot.setSlotEnd(to2.getTime());

            opSlot.setType("neuro");

            opSlotRepository.save(opSlot);
        }

        Hospital lkhGraz = hospitalRepository.findByEmail("kh3@dse.at");
        if (lkhGraz == null) {
            lkhGraz = new Hospital();
            lkhGraz.setName("Krankenhaus 3 - LKH Graz");
            lkhGraz.setShortName("LKHG");
            lkhGraz.setPassword(authService.encodePassword("password"));
            lkhGraz.seteMail("kh3@dse.at");
            lkhGraz.setX(LKHGRAZ.getX());
            lkhGraz.setY(LKHGRAZ.getY());
            hospitalRepository.save(lkhGraz);

            opSlot = new OpSlot();
            opSlot.setHospital(lkhGraz);

            opSlot.setSlotStart(from3.getTime());
            opSlot.setSlotEnd(to3.getTime());

            opSlot.setType("eye");
        }

    }

    public static Hospital createHospital(HospitalRepository hospitalRepository, AuthService authService) {
        Hospital hospital = hospitalRepository.findByEmail("kh0@dse.at");
        if (hospital == null) {
            hospital = new Hospital();
            hospital.setName("Krankenhaus 0 - Demo KH");
            hospital.setShortName("KH 0");
            hospital.setPassword(authService.encodePassword("password"));
            hospital.seteMail("kh0@dse.at");
            hospital.setX(AKHWIEN.getX());
            hospital.setY(AKHWIEN.getY());
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
            from1.set(2015, 05, 23, 8, 55);
            GregorianCalendar from2 = new GregorianCalendar();
            from2.set(2015, 05, 24, 9, 55);
            GregorianCalendar  from3 = new GregorianCalendar();
            from3.set(2015, 05, 25, 10, 55);
            GregorianCalendar  to1 = new GregorianCalendar();
            to1.set(2015, 05, 23, 13, 05);
            GregorianCalendar  to2 = new GregorianCalendar();
            to2.set(2015, 05, 24, 14, 05);
            GregorianCalendar to3 = new GregorianCalendar();
            to3.set(2015, 05, 25, 15, 05);

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