package opPlanner.KLINIsys;

import opPlanner.KLINIsys.dto.ExtendedOpSlotListDTO;
import opPlanner.KLINIsys.dto.OpSlotListDTO;
import opPlanner.KLINIsys.model.Doctor;
import opPlanner.KLINIsys.model.Hospital;
import opPlanner.KLINIsys.model.OpSlot;
import opPlanner.KLINIsys.model.Patient;
import opPlanner.KLINIsys.repository.*;
import opPlanner.KLINIsys.service.AuthService;
import opPlanner.KLINIsys.service.OpSlotService;
import opPlanner.Shared.OpPlannerProperties;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Michael on 20.05.2015.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest({"server.port=0", "management.port=0"})
public class OpSlotsTests {

    /* IMPORTANT REQUIREMENT:
        These tests require the RESERVATION system to be accessible. They do not depend on the actual data retrieving from the RESERVATION system.
     */

    @Autowired
    OpPlannerProperties config;

    @Test
    public void testReservationAvailable() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getForObject(config.getReservation().buildUrl("/"), String.class,new Object());
    }


    @Autowired
    private AuthService authService;

    @Autowired
    private HospitalRepository hospitalRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private LoginUserRepository loginUserRepository;

    @Autowired
    private OpSlotRepository opSlotRepository;

    @Autowired
    private OpSlotService opSlotService;

    @Autowired
    private TimeWindowRepository timeWindowRepository;

    private Hospital hospital;

    private Doctor doctor;

    private Patient patient;

    private OpSlot opSlot;

    @Before
    public void setUp() {
        hospital = new Hospital();
        hospital.setName("Krankenhaus 2");
        hospital.seteMail("h2@dse.at");
        hospital.setShortName("KH 2");
        hospital.setAddress("Linz....");
        hospital.setPassword(authService.encodePassword("password"));
        hospitalRepository.save(hospital);


        doctor = new Doctor();
        doctor.setName("Doc 2");
        doctor.seteMail("d2@dse.at");
        doctor.setPassword(authService.encodePassword("password"));
        doctorRepository.save(doctor);

        hospital = DemoDataHelper.createHospital(hospitalRepository, authService);
        doctor = DemoDataHelper.createDoctor(doctorRepository, timeWindowRepository, authService);
        patient = DemoDataHelper.createPatient(patientRepository, authService);

        for (int i = 0; i < 10; i++) {
            opSlot = DemoDataHelper.createOpSlot(opSlotRepository, doctor, hospital);
        }
    }

    @Test
    public void testAutowire() {
        assertNotNull(opSlotRepository);
        assertNotNull(opSlotService);
    }

    @Test
    public void testServicePublic() {
        List<?extends OpSlotListDTO> opSlots = opSlotService.getFilteredOpSlots(null, null,null,null,new Date(0),null);

        assertNotNull(opSlots);
        assertEquals(opSlots.size(), 10);

        // we must not get an extended model when using public access!
        assertFalse(opSlots.stream().anyMatch(x->x instanceof ExtendedOpSlotListDTO));
    }

    @Test
    public void testServiceHospital() {

        List<?extends OpSlotListDTO> opSlots = opSlotService.getFilteredOpSlots(Hospital.class, null,null,hospital,new Date(0),null);

        assertNotNull(opSlots);
        assertEquals(opSlots.size(), 10);

        hospital = hospitalRepository.findByEmail("h2@dse.at");

        opSlots = opSlotService.getFilteredOpSlots(Hospital.class, null,null,hospital,new Date(0),null);
        assertNotNull(opSlots);
        assertEquals(opSlots.size(), 0);
    }

    @Test
    public void testServiceDoctor() {
        List<?extends OpSlotListDTO> opSlots = opSlotService.getFilteredOpSlots(Doctor.class, doctor,null,null,new Date(0),null);

        assertNotNull(opSlots);

        // only really relevant when data is available at the reservation
        for (OpSlotListDTO opSlot : opSlots) {
            assertEquals(ExtendedOpSlotListDTO.class, opSlot.getClass());

            ExtendedOpSlotListDTO extendedOpSlotListDTO = (ExtendedOpSlotListDTO) opSlot;
            assertNotNull(extendedOpSlotListDTO.getDoctorId());
            assertNotNull(extendedOpSlotListDTO.getDoctorName());
            assertNotNull(extendedOpSlotListDTO.getDoctorName());
            assertNotNull(extendedOpSlotListDTO.getPatientId());
            assertNotNull(extendedOpSlotListDTO.getPatientName());
        }
    }


    @Test
    public void testServiceBasicDateFiltering() {
        GregorianCalendar gc = new GregorianCalendar();
        gc.add(Calendar.YEAR, -1);
        List<?extends OpSlotListDTO> opSlots = opSlotService.getFilteredOpSlots(null, null,null,null,gc.getTime(), new Date());
        assertNotNull(opSlots);
        assertEquals(opSlots.size(), 10);   //later therefore we still get all slots

        opSlots = opSlotService.getFilteredOpSlots(null, null,null,null, new Date(0),new Date(Long.MAX_VALUE));
        assertNotNull(opSlots);
        assertEquals(opSlots.size(), 10);   //later therefore we still get all slots

        opSlots = opSlotService.getFilteredOpSlots(null, null,null,null, new Date(Long.MAX_VALUE),null);
        assertNotNull(opSlots);
        assertEquals(opSlots.size(), 0);   //from = MAX

        opSlots = opSlotService.getFilteredOpSlots(null, null,null,null, gc.getTime(),new Date(0));
        assertNotNull(opSlots);
        assertEquals(opSlots.size(), 0);   //to = 0
    }

}
