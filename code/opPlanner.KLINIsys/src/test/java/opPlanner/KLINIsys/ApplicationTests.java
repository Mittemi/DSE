package opPlanner.KLINIsys;

import opPlanner.KLINIsys.service.HospitalService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
//@ActiveProfiles("unit-test")
public class ApplicationTests {

	@Test
	public void contextLoads() {
	}

	@Autowired
	private HospitalService service;

	@Autowired
	private Environment env;

	@Test
	public void testService() {
		assertNotNull(service);
	}

	@Test
	public void testProfile() {
		assertEquals(env.getActiveProfiles().length,1);
		assertTrue(env.getActiveProfiles()[0].equals("unit-test"));
	}
}
