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

    private static final Point SMZOST = new Point(47.220589, 16.346794);
    private static final Point LKHKREMS = new Point(47.802743, 14.305328);
    private static final Point LKHBADEN = new Point(48.081800, 13.466207);
    private static final Point RUDOLFINERHAUS = new Point(47.081800, 10.466207);

    public static void createRequirementsTestData(HospitalRepository hospitalRepository, OpSlotRepository
            opSlotRepository, PatientRepository patientRepository, DoctorRepository doctorRepository,
                                                  TimeWindowRepository timeWindowRepository, AuthService authService) {

        opSlotRepository.deleteAll();
        timeWindowRepository.deleteAll();
        doctorRepository.deleteAll();
        hospitalRepository.deleteAll();
        patientRepository.deleteAll();

        //create patients

        Patient patient1 = new Patient();
        patient1.setName("Fr. Adelheid Abesser");
        patient1.seteMail("abesser@dse.at");
        patient1.setX(48.208697);
        patient1.setY(15.372265);
        patient1.setPassword(authService.encodePassword("password"));
        patientRepository.save(patient1);

        Patient patient2 = new Patient();
        patient2.setName("Hr. Peter Berger");
        patient2.seteMail("berger@dse.at");
        patient2.setX(47.208697);
        patient2.setY(14.872265);
        patient2.setPassword(authService.encodePassword("password"));
        patientRepository.save(patient2);

        Patient patient3 = new Patient();
        patient3.setName("Fr. Beatrix Bauer");
        patient3.seteMail("bauer@dse.at");
        patient3.setX(47.908697);
        patient3.setY(14.272215);
        patient3.setPassword(authService.encodePassword("password"));
        patientRepository.save(patient3);

        Patient patient4 = new Patient();
        patient4.setName("Hr. Franz Fiedler");
        patient4.seteMail("fiedler@dse.at");
        patient4.setX(48.108697);
        patient4.setY(14.202215);
        patient4.setPassword(authService.encodePassword("password"));
        patientRepository.save(patient4);

        Patient patient5 = new Patient();
        patient5.setName("Fr. Gloria Geraus");
        patient5.seteMail("geraus@dse.at");
        patient5.setX(48.308697);
        patient5.setY(16.272215);
        patient5.setPassword(authService.encodePassword("password"));
        patientRepository.save(patient5);

        Patient patient6 = new Patient();
        patient6.setName("Hr. Manfred Takacs");
        patient6.seteMail("takacs@dse.at");
        patient6.setX(48.55);
        patient6.setY(14.63);
        patient6.setPassword(authService.encodePassword("password"));
        patientRepository.save(patient6);

        //create work schedules
        GregorianCalendar twfrom1 = new GregorianCalendar();
        twfrom1.set(2015, 07, 23, 8, 00);
        GregorianCalendar twto1 = new GregorianCalendar();
        twto1.set(2015, 07, 27, 20, 00);

        GregorianCalendar twfrom2 = new GregorianCalendar();
        twfrom2.set(2015, 07, 23, 8, 00);
        GregorianCalendar twto2 = new GregorianCalendar();
        twto2.set(2015, 07, 24, 20, 00);

        TimeWindow fullTimeWorkSchedule1 = new TimeWindow(twfrom1.getTime(), twto1.getTime());
        TimeWindow partTimeWorkSchedule1 = new TimeWindow(twfrom2.getTime(), twto2.getTime());

        TimeWindow fullTimeWorkSchedule2 = new TimeWindow(twfrom1.getTime(), twto1.getTime());
        TimeWindow partTimeWorkSchedule2 = new TimeWindow(twfrom2.getTime(), twto2.getTime());




        //create doctors
        Doctor doctor1 = new Doctor();
        doctor1.setName("Dr. Albert Aufschneider");
        doctor1.seteMail("aufschneider@dse.at");
        doctor1.setPassword(authService.encodePassword("password"));
        doctor1.getWorkSchedule().add(fullTimeWorkSchedule1);
        fullTimeWorkSchedule1.setDoctor(doctor1);
        doctorRepository.save(doctor1);
        timeWindowRepository.save(fullTimeWorkSchedule1);


        Doctor doctor2 = new Doctor();
        doctor2.setName("Dr. Emily Ehmoser");
        doctor2.seteMail("ehmoser@dse.at");
        doctor2.setPassword(authService.encodePassword("password"));
        doctor2.getWorkSchedule().add(fullTimeWorkSchedule2);
        fullTimeWorkSchedule2.setDoctor(doctor2);
        doctorRepository.save(doctor2);
        timeWindowRepository.save(fullTimeWorkSchedule2);

        Doctor doctor3 = new Doctor();
        doctor3.setName("Dr. Adam Augfehler");
        doctor3.seteMail("augfehler@dse.at");
        doctor3.setPassword(authService.encodePassword("password"));
        doctor3.getWorkSchedule().add(partTimeWorkSchedule1);
        partTimeWorkSchedule1.setDoctor(doctor3);
        doctorRepository.save(doctor3);
        timeWindowRepository.save(partTimeWorkSchedule1);

        Doctor doctor4 = new Doctor();
        doctor4.setName("Dr. Maria Morks");
        doctor4.seteMail("morks@dse.at");
        doctor4.setPassword(authService.encodePassword("password"));
        doctor4.getWorkSchedule().add(partTimeWorkSchedule2);
        partTimeWorkSchedule2.setDoctor(doctor4);
        doctorRepository.save(doctor4);
        timeWindowRepository.save(partTimeWorkSchedule2);

        //create hospitals

        Hospital hospital1 = new Hospital();
        hospital1.setName("SMZ Ost");
        hospital1.setShortName("SMZO");
        hospital1.setPassword(authService.encodePassword("password"));
        hospital1.seteMail("smzo@dse.at");
        hospital1.setX(SMZOST.getX());
        hospital1.setY(SMZOST.getY());
        hospitalRepository.save(hospital1);

        Hospital hospital2 = new Hospital();
        hospital2.setName("LKH Krems");
        hospital2.setShortName("LKHK");
        hospital2.setPassword(authService.encodePassword("password"));
        hospital2.seteMail("lkhk@dse.at");
        hospital2.setX(LKHKREMS.getX());
        hospital2.setY(LKHKREMS.getY());
        hospitalRepository.save(hospital2);

        Hospital hospital3 = new Hospital();
        hospital3.setName("Rudolfinerhaus");
        hospital3.setShortName("RUFIHA");
        hospital3.setPassword(authService.encodePassword("password"));
        hospital3.seteMail("rufiha@dse.at");
        hospital3.setX(RUDOLFINERHAUS.getX());
        hospital3.setY(RUDOLFINERHAUS.getY());
        hospitalRepository.save(hospital3);

        Hospital hospital4 = new Hospital();
        hospital4.setName("LKH Baden");
        hospital4.setShortName("LKHB");
        hospital4.setPassword(authService.encodePassword("password"));
        hospital4.seteMail("lkhb@dse.at");
        hospital4.setX(LKHBADEN.getX());
        hospital4.setY(LKHBADEN.getY());
        hospitalRepository.save(hospital4);

        //define op slot durations
        GregorianCalendar from1 = new GregorianCalendar();
        from1.set(2015, 07, 23, 9, 00);
        GregorianCalendar   from2 = new GregorianCalendar();
        from2.set(2015, 07, 24, 10, 00);
        GregorianCalendar  from3 = new GregorianCalendar();
        from3.set(2015, 07, 24, 15, 00);
        GregorianCalendar from4 = new GregorianCalendar();
        from4.set(2015, 07, 26, 9, 00);
        GregorianCalendar  to1 = new GregorianCalendar();
        to1.set(2015, 07, 23, 10, 00);
        GregorianCalendar  to2 = new GregorianCalendar();
        to2.set(2015, 07, 24, 12, 00);
        GregorianCalendar  to3 = new GregorianCalendar();
        to3.set(2015, 07, 24, 18, 00);
        GregorianCalendar  to4 = new GregorianCalendar();
        to4.set(2015, 07, 26, 13, 00);


        //create opslots
        OpSlot opSlot1 = new OpSlot();
        opSlot1.setHospital(hospital1);             //SMZ Ost
        opSlot1.setSlotStart(from1.getTime());      //60min slot
        opSlot1.setSlotEnd(to1.getTime());
        opSlot1.setType("Augenheilkunde");
        opSlotRepository.save(opSlot1);

        OpSlot opSlot2 = new OpSlot();
        opSlot2.setHospital(hospital1);             //SMZ Ost
        opSlot2.setSlotStart(from2.getTime());      //120min slot
        opSlot2.setSlotEnd(to2.getTime());
        opSlot2.setType("Orthopaedie");
        opSlotRepository.save(opSlot2);

        OpSlot opSlot3 = new OpSlot();
        opSlot3.setHospital(hospital2);             //LKH Krems
        opSlot3.setSlotStart(from1.getTime());      //60min slot
        opSlot3.setSlotEnd(to1.getTime());
        opSlot3.setType("HNO");
        opSlotRepository.save(opSlot3);

        OpSlot opSlot4 = new OpSlot();
        opSlot4.setHospital(hospital2);             //LKH Krems
        opSlot4.setSlotStart(from3.getTime());      //180min slot
        opSlot4.setSlotEnd(to3.getTime());
        opSlot4.setType("Neurochirurgie");
        opSlotRepository.save(opSlot4);

        OpSlot opSlot5 = new OpSlot();
        opSlot5.setHospital(hospital3);             //LKH Baden
        opSlot5.setSlotStart(from4.getTime());      //240min slot
        opSlot5.setSlotEnd(to4.getTime());
        opSlot5.setType("Kardiologie");
        opSlotRepository.save(opSlot5);

        OpSlot opSlot6 = new OpSlot();
        opSlot6.setHospital(hospital3);             //LKH Baden
        opSlot6.setSlotStart(from3.getTime());      //180min slot
        opSlot6.setSlotEnd(to3.getTime());
        opSlot6.setType("Augenheilkunde");
        opSlotRepository.save(opSlot6);

        OpSlot opSlot7 = new OpSlot();
        opSlot7.setHospital(hospital4);             //Rudolfinerhaus
        opSlot7.setSlotStart(from1.getTime());      //60min slot
        opSlot7.setSlotEnd(to1.getTime());
        opSlot7.setType("Orthopaedie");
        opSlotRepository.save(opSlot7);

        OpSlot opSlot8 = new OpSlot();
        opSlot8.setHospital(hospital4);             //Rudolfinerhaus
        opSlot8.setSlotStart(from4.getTime());      //240min slot
        opSlot8.setSlotEnd(to4.getTime());
        opSlot8.setType("HNO");
        opSlotRepository.save(opSlot8);

        OpSlot opSlot9 = new OpSlot();
        opSlot9.setHospital(hospital4);             //Rudolfinerhaus
        opSlot9.setSlotStart(from2.getTime());      //120 min slot
        opSlot9.setSlotEnd(to2.getTime());
        opSlot9.setType("Neurochirurgie");
        opSlotRepository.save(opSlot9);

        OpSlot opSlot10 = new OpSlot();
        opSlot10.setHospital(hospital1);             //SMZ Ost
        opSlot10.setSlotStart(from4.getTime());      //240 min slot
        opSlot10.setSlotEnd(to4.getTime());
        opSlot10.setType("Neurochirurgie");
        opSlotRepository.save(opSlot10);


        OpSlot opSlot11 = new OpSlot();
        opSlot11.setHospital(hospital3);             //LKH Baden
        opSlot11.setSlotStart(from3.getTime());      //180 min slot
        opSlot11.setSlotEnd(to3.getTime());
        opSlot11.setType("Kardiologie");
        opSlotRepository.save(opSlot11);


        OpSlot opSlot12 = new OpSlot();
        opSlot12.setHospital(hospital2);             //LKH Krems
        opSlot12.setSlotStart(from4.getTime());      //240 min slot
        opSlot12.setSlotEnd(to4.getTime());
        opSlot12.setType("Augenheilkunde");
        opSlotRepository.save(opSlot12);
    }

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
            opSlotRepository.save(opSlot);

            opSlot = new OpSlot();
            opSlot.setHospital(lkhGraz);
            opSlot.setSlotStart(from1.getTime());
            opSlot.setSlotEnd(to1.getTime());
            opSlot.setType("eye");
            opSlotRepository.save(opSlot);
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
            doctor.setName("The one and only doc");
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
     //   opSlot.setDoctor(doctor);
        opSlot.setHospital(hospital);

        opSlot.setSlotStart(new Date());
        opSlot.setSlotEnd(new Date());

        opSlot.setType("Demo");

        opSlotRepository.save(opSlot);
        return opSlot;
    }


}