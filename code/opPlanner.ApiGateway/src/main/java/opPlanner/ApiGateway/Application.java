package opPlanner.ApiGateway;

import com.netflix.hystrix.contrib.javanica.aop.aspectj.HystrixCommandAspect;
import opPlanner.Shared.OpPlannerProperties;
import opPlanner.ApiGateway.controller.OpSlotsController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@EnableAutoConfiguration
@ComponentScan(basePackageClasses = { OpPlannerProperties.class,  OpSlotsController.class, SecurityConfig.class, FilterConfigurationBean.class, SimpleCORSFilter.class})
@SpringBootApplication
@EnableConfigurationProperties({  OpPlannerProperties.class})
public class Application {

    @Bean
    public HystrixCommandAspect hystrixAspect() {
        return new HystrixCommandAspect();
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
