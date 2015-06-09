package opPlanner.ApiGateway;

import opPlanner.Shared.OpPlannerProperties;
import opPlanner.ApiGateway.controller.OpSlotsController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.context.annotation.ComponentScan;

@EnableAutoConfiguration
@ComponentScan(basePackageClasses = { OpPlannerProperties.class,  OpSlotsController.class, SecurityConfig.class, FilterConfigurationBean.class, SimpleCORSFilter.class})
@SpringBootApplication
@EnableConfigurationProperties({  OpPlannerProperties.class})
@EnableCircuitBreaker
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
