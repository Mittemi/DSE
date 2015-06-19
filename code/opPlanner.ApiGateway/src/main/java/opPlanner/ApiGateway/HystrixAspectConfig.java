package opPlanner.ApiGateway;

import com.netflix.hystrix.contrib.javanica.aop.aspectj.HystrixCommandAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Created by Thomas on 18.06.2015.
 */
@Configuration
@Profile(value = {"default", "unit-tests"})
public class HystrixAspectConfig {

    @Bean
    public HystrixCommandAspect hystrixAspect() {
        return new HystrixCommandAspect();
    }
}
