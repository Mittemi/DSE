package opPlanner.KLINIsys;

import static org.junit.Assert.*;

import opPlanner.KLINIsys.service.HospitalService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
//@ActiveProfiles("unit-test")
public class ApplicationTests {

	@Test
	public void contextLoads() {
	}

	@Autowired
	HospitalService service;

	@Test
	public void testService() {
		assertNotNull(service);
	}

}
