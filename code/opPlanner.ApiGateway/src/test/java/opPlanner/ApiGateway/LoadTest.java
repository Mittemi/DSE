package opPlanner.ApiGateway;

import com.jayway.restassured.response.Response;
import com.jayway.restassured.response.ValidatableResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static com.jayway.restassured.RestAssured.get;
import static com.jayway.restassured.RestAssured.given;

/**
 * Created by Thomas on 18.06.2015.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest({"server.port=0", "management.port=0"})
@ActiveProfiles({"load-test"})
public class LoadTest {

    @Value("${local.server.port}")
    private int port;

    private static String BASE_URL = "http://localhost:";
    private static int THREAD_COUNT_SIMPLE = 100; //number of threads for simple load testing
    private static int AWAIT_TIME_SIMPLE = 30; //seconds to wait for simple load test completion
    private AtomicInteger responseNumber; //concurrently used counter for increasing the number of valid responses
    private ExecutorService es = Executors.newCachedThreadPool();

    private String buildUrl(String relative) {
        return BASE_URL + port +"/" + relative;
    }

    @Before
    public void prepareLoadTest() {
        responseNumber = new AtomicInteger(0);
    }

    /**
     * performs a simple test by just performing a rest call to a method,
     * which no authorization is needed for
     */
    @Test
    public void startSimpleLoadTest () throws InterruptedException {
        for (int i = 0; i < THREAD_COUNT_SIMPLE; i++) {
            es.execute(new Runnable() {
                @Override
                public void run() {
                    get(buildUrl("account/details")).then().statusCode(200);
                    responseNumber.incrementAndGet();
                }
            });
        }
        es.awaitTermination(AWAIT_TIME_SIMPLE, TimeUnit.SECONDS);
        Assert.assertEquals(THREAD_COUNT_SIMPLE, responseNumber.get());
    }

    /**
     * Performs a load test by retrieving opslots as "public"
     * @throws InterruptedException
     */
    @Test
     public void startPublicAccessLoadTest() throws InterruptedException {
        for (int i = 0; i < THREAD_COUNT_SIMPLE; i++) {
            es.execute(new Runnable() {
                @Override
                public void run() {
                    given()
                            .when()
                            .get(buildUrl("opslots/list"))
                            .then()
                            .statusCode(200);
                    responseNumber.incrementAndGet();
                }
            });
        }
        es.awaitTermination(AWAIT_TIME_SIMPLE, TimeUnit.SECONDS);
        Assert.assertEquals(THREAD_COUNT_SIMPLE, responseNumber.get());
    }

    /**
     * performs a load test by retrieving op slots from a doctor
     * @throws InterruptedException
     */
    @Test
    public void startAuthorizedAccessLoadTest() throws InterruptedException {
        for (int i = 0; i < THREAD_COUNT_SIMPLE; i++) {
            es.execute(new Runnable() {
                @Override
                public void run() {
                    given()
                            .auth()
                            .basic("morks@dse.at", "password")
                            .when()
                            .get(buildUrl("opslots/list"))
                            .then()
                            .statusCode(200);
                    responseNumber.incrementAndGet();
                }
            });
        }
        es.awaitTermination(AWAIT_TIME_SIMPLE, TimeUnit.SECONDS);
        Assert.assertEquals(THREAD_COUNT_SIMPLE, responseNumber.get());
    }

}
