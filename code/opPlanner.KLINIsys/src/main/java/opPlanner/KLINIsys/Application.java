package opPlanner.KLINIsys;

import opPlanner.KLINIsys.model.Hospital;
import opPlanner.KLINIsys.service.HospitalService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import opPlanner.KLINIsys.repository.HospitalRepository;

@SpringBootApplication
@Configuration
@EntityScan(basePackageClasses = {Hospital.class})
@EnableJpaRepositories(basePackageClasses = {HospitalRepository.class})
@ComponentScan(basePackageClasses = {HospitalService.class})
@EnableAutoConfiguration()
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
