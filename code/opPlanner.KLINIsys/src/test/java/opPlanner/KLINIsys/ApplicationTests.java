package opPlanner.KLINIsys;

import opPlanner.KLINIsys.service.HospitalService;
import opPlanner.KLINIsys.service.OpSlotService;
import opPlanner.Shared.OpPlannerProperties;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest({"server.port=0", "management.port=0"})
public class ApplicationTests {

	@Test
	public void contextLoads() {
	}

	@Autowired
	private HospitalService service;

	@Autowired
	private Environment env;

	@Autowired
	private OpPlannerProperties plannerProperties;

	@Test
	public void testService() {
		assertNotNull(service);
	}

	@Test
	public void testProfile() {
		assertEquals(env.getActiveProfiles().length,1);
		assertTrue(env.getActiveProfiles()[0].equals("unit-test"));
	}

	@Test
	public void testConfig() {
		assertNotNull(plannerProperties);
		assertNotNull(plannerProperties.getReservation());
		assertNotNull(plannerProperties.getReservation().getBaseUrl());
	}
}
