import com.jayway.restassured.response.Response;
import com.jayway.restassured.response.ValidatableResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static com.jayway.restassured.RestAssured.get;
import static com.jayway.restassured.RestAssured.given;

/**
 * Created by Thomas on 18.06.2015.
 */
public class LoadTest {
    private static String BASE_URL = "http://localhost:8080/";
    private static int THREAD_COUNT_SIMPLE = 10; //number of threads for simple load testing
    private static int AWAIT_TIME_SIMPLE = 20; //seconds to wait for simple load test completion
    private AtomicInteger responseNumber; //concurrently used counter for increasing the number of valid responses
    private ExecutorService es = Executors.newCachedThreadPool();

    private static String buildUrl(String relative) {
        return BASE_URL + relative;
    }

    @Before
    public void prepareLoadTest() {
        responseNumber = new AtomicInteger(0);
    }

    @Test
    public void startSimpleLoadTest () throws InterruptedException {
        for (int i = 0; i < THREAD_COUNT_SIMPLE; i++) {
            es.execute(new Runnable() {
                /**
                 * performs a simple test by just performing a rest call to a method,
                 * which no authorization is needed for
                 */
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
