package opPlanner.KLINIsys;

import opPlanner.KLINIsys.model.AuthResult;
import opPlanner.KLINIsys.model.Doctor;
import opPlanner.KLINIsys.model.Hospital;
import opPlanner.KLINIsys.model.Patient;
import opPlanner.KLINIsys.repository.*;
import opPlanner.KLINIsys.service.AuthService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Michael on 08.04.2015.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@SpringApplicationConfiguration(classes = Application.class)
//@ActiveProfiles("unit-test")
public class ServiceTest {


    /* IMPORTANT REQUIREMENT:
        These tests require the RESERVATION system to be accessible. They do not depend on the actual data retrieving from the RESERVATION system.
     */

    @Autowired
    private AuthService authService;

    @Autowired
    private HospitalRepository hospitalRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private TimeWindowRepository timeWindowRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private LoginUserRepository loginUserRepository;

    private Hospital hospital;

    private Doctor doctor;

    private Patient patient;


    @Before
    public void setUp() {
        hospital = new Hospital();
        hospital.setName("Krankenhaus 2");
        hospital.seteMail("h2@dse.at");
        hospital.setShortName("KH 2");
        hospital.setAddress("Linz....");
        hospital.setPassword(authService.encodePassword("password"));
        hospitalRepository.save(hospital);


        hospital = DemoDataHelper.createHospital(hospitalRepository, authService);
        doctor = DemoDataHelper.createDoctor(doctorRepository, timeWindowRepository, authService);
        patient = DemoDataHelper.createPatient(patientRepository, authService);
    }

    @Test
    public void findByName() {

        assertTrue(hospitalRepository.findByName("Krankenhaus 2").size() == 1);

        assertEquals(hospital, hospitalRepository.findByName(hospital.getName()).get(0));

        assertTrue(hospitalRepository.findByName("Das gits nicht!").size() == 0);
    }

    @Test
    public void findByeMail() {
        assertEquals(hospital, hospitalRepository.findByEmail(hospital.geteMail()));
        assertEquals(doctor, doctorRepository.findByEmail(doctor.geteMail()));
        assertEquals(patient, patientRepository.findByEmail(patient.geteMail()));
        assertEquals(hospital, loginUserRepository.findByEmail(hospital.geteMail()));
        assertEquals(doctor, loginUserRepository.findByEmail(doctor.geteMail()));
        assertEquals(patient, loginUserRepository.findByEmail(patient.geteMail()));
    }

    @Test
    public void findByEmailIn() {

        List<Hospital> hospitals = hospitalRepository.findByeMailIn(Arrays.asList(new String[]{"kh1@dse.at", "h2@dse.at"}));
        assertEquals(2, hospitals.size());
        hospitals = hospitalRepository.findByeMailIn(Arrays.asList(new String[]{}));
        assertEquals(0, hospitals.size());
        hospitals = hospitalRepository.findByeMailIn(Arrays.asList(new String[]{"kh1@dse.at"}));
        assertEquals(1, hospitals.size());
        hospitals = hospitalRepository.findByeMailIn(Arrays.asList(new String[]{"h2@dse.at"}));
        assertEquals(1, hospitals.size());
        hospitals = hospitalRepository.findByeMailIn(Arrays.asList(new String[]{"NOT FOUND"}));
        assertEquals(0, hospitals.size());
    }

    @Test
    public void findAfterDelete() {

        Hospital h = hospitalRepository.findByName("Krankenhaus 1").get(0);

        hospitalRepository.delete(h);

        assertTrue(hospitalRepository.findByName("Krankenhaus 1").size() == 0);
    }

    @Test
    public void testAuthentication() throws Exception {
        assertNotNull(authService);

        //no username
        AuthResult authResult = authService.authenticate(null, "wrongPassword");

        assertNotNull(authResult);
        assertFalse(authResult.isAuthenticated());

        //wrong user
        authResult = authService.authenticate("this user does not exist", "wrongPassword");

        assertNotNull(authResult);
        assertFalse(authResult.isAuthenticated());

        //correct user wrong password
        authResult = authService.authenticate(hospital.geteMail(), "wrongPassword");

        assertNotNull(authResult);
        assertFalse(authResult.isAuthenticated());

    }

    @Test
    public void testAuthHospital() {
        testAuth(hospital.geteMail(), "password", "Hospital");
    }

    @Test
    public void testAuthDoctor() {
        testAuth(doctor.geteMail(), "password", "Doctor");
    }

    @Test
    public void testAuthPatient() {
        testAuth(patient.geteMail(), "password", "Patient");
    }

    private void testAuth(String email,String password, String role) {
        AuthResult authResult;
        authResult = authService.authenticate(email, password);

        assertNotNull(authResult);
        assertTrue(authResult.isAuthenticated());
        assertEquals(authResult.getRoles().size(), 1);

        assertEquals(authResult.getRoles().get(0), role);
    }
}
