package opPlanner.KLINIsys;

import opPlanner.KLINIsys.controller.AuthController;
import opPlanner.KLINIsys.controller.HospitalController;
import opPlanner.KLINIsys.model.Hospital;
import opPlanner.KLINIsys.model.LoginUser;
import opPlanner.KLINIsys.repository.LoginUserRepository;
import opPlanner.KLINIsys.repository.PatientRepository;
import opPlanner.KLINIsys.service.AuthService;
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
@EntityScan(basePackages = { "opPlanner.KLINIsys.model" })
@EnableJpaRepositories(basePackages = {"opPlanner.KLINIsys.repository"})
@ComponentScan(basePackages = {"opPlanner.KLINIsys.service", "opPlanner.KLINIsys.controller"})
@EnableAutoConfiguration()
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
