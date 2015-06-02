package opPlanner.OPmatcher;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * Created by Michael on 02.06.2015.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class ConfigTest {

    @Autowired
    private OPPlannerProperties plannerProperties;

    @Test
    public void testConfig() {

        assertNotNull(plannerProperties);
        assertNotNull(plannerProperties.getMongoDb());
        assertNotNull(plannerProperties.getMongoDb().getIpOrHostname());
    }
}
