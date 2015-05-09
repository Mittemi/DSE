package opPlanner.ApiGateway;

import opPlanner.ApiGateway.controller.KlinisysController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@EnableAutoConfiguration
@ComponentScan(basePackageClasses = { KlinisysController.class, SecurityConfig.class})
@SpringBootApplication
@EnableConfigurationProperties({OpPlannerProperties.class})
@EnableCircuitBreaker
//@EnableHystrixDashboard     //TODO: remove for production ???
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
