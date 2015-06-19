package opPlanner.NOTifier;

import opPlanner.Shared.OpPlannerProperties;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest({"server.port=0", "management.port=0"})
public class ApplicationTests {

	@Autowired
	private OpPlannerProperties plannerProperties;

	@Test
	public void contextLoads() {
	}

	@Test
	public void configTest() {
		assertNotNull(plannerProperties);
		assertNotNull(plannerProperties.getNotifier());
		assertNotNull(plannerProperties.getNotifier());
		assertNotNull(plannerProperties.getNotifier().getMongoDb());
	}
}
