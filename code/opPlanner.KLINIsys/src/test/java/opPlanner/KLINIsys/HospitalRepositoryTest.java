package opPlanner.KLINIsys;

import opPlanner.KLINIsys.model.Hospital;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import opPlanner.KLINIsys.repository.HospitalRepository;

import static org.junit.Assert.*;

/**
 * Created by Michael on 08.04.2015.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@SpringApplicationConfiguration(classes = Application.class)
//@ActiveProfiles("unit-test")
public class HospitalRepositoryTest {

    @Autowired
    HospitalRepository repository;

    Hospital hospital;

    @Before
    public void setUp() {
        hospital = new Hospital();
        hospital.setName("Krankenhaus 1");

        repository.save(hospital);

        hospital = new Hospital();
        hospital.setName("Krankenhaus 2");

        repository.save(hospital);
    }

    @Test
    public void findByName() {

        assertTrue(repository.findByName("Krankenhaus 2").size() == 1);

        assertEquals(hospital, repository.findByName("Krankenhaus 2").get(0));

        assertTrue(repository.findByName("Das gits nicht!").size() == 0);
    }

    @Test
    public void findAfterDelete() {

        Hospital h = repository.findByName("Krankenhaus 1").get(0);

        repository.delete(h);

        assertTrue(repository.findByName("Krankenhaus 1").size() == 0);
    }
}
