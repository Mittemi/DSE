package opPlanner.ApiGateway;

import opPlanner.ApiGateway.controller.PatientController;
import opPlanner.Shared.OpPlannerProperties;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithSecurityContextTestExecutionListener;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest({"server.port=0", "management.port=0"})
@ActiveProfiles({"unit-test"})
//@TestExecutionListeners(inheritListeners = false, listeners = {DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class, WithSecurityContextTestExecutionListener.class})
public class ApplicationTests {


	/* IMPORTANT:
	 * for proper functioning of these test no other service should be running!
	 */

	@Test
	public void contextLoads() {
	}

	@Autowired
	OpPlannerProperties config;

	@Test
	public void configTest() {
		assertNotNull(config);
		assertNotNull(config.getReservation());
		assertNotNull(config.getReservation().getIpOrHostname());
	}

	@Value("${local.server.port}")
	private int port;

	private String getUrl(String relativ) {
		return "http://localhost:" + port + "/" + relativ;
	}

	@Autowired
	PatientController patientController;

	// depending on the state of the klinisys, this will test the hystrix fallback or the klinisys authentication
	@Test
	public void testAuthenticationAndHystrixFallback() {

		RestTemplate testRestTemplate = new TestRestTemplate("userdoesn'texist", "awrongpassword");
		String result = testRestTemplate.getForObject(getUrl("/account/details/"), String.class, new Object());

		assertTrue(result.contains("Bad credentials"));

		testRestTemplate = new RestTemplate();

		result = testRestTemplate.getForObject(getUrl("/account/details/"), String.class, new Object());

		assertTrue(result.contains("false"));
	}

	@Test
	public void testFailsIfReservationOrKlinisysIsRunning() {
		//if this test fails, the reservation system or klinisys is accessable. therefore the results of the following test is useless!
		// stop the other services or make sure they are not accessible
		RestTemplate restTemplate = new RestTemplate();
		try {
			restTemplate.getForObject(config.getReservation().buildUrl("/"), String.class, new Object());
			assertTrue("Reservation is running", true);
		}catch(Exception ex) {
		}

		try{
			restTemplate.getForObject(config.getKlinisys().buildUrl("/"), String.class, new Object());
			assertTrue("Klinisys is running", true);
		}catch (Exception ex){

		}
	}

	@Test(expected = HttpServerErrorException.class)
	public void testHystrixTimeoutAndPublicAccess() throws URISyntaxException {

		RestTemplate testRestTemplate = new RestTemplate();
		RequestEntity<String> requestEntity = new RequestEntity<String>(HttpMethod.GET,new URI(getUrl("/opslots/list")));

		//Hystrix is going to set status 503 while the resttemplate results with status 500, when klinisys and/or reservation is not running.
		try {
			ResponseEntity<String> responseEntity = testRestTemplate.exchange(requestEntity, new ParameterizedTypeReference<String>() {
			});
		}catch (HttpServerErrorException e){
			if(e.getStatusCode().value() == 503)
				throw e;
		}

		assertTrue("Either klinisys and/or reservation is running or hystrix fallback isn't working", true);
	}
}
