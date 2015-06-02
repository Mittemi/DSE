package opPlanner.ApiGateway;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;

/**
 * Created by Lukas Kraenkl (1126052) on 13.05.2015.
 */
@Configuration
public class FilterConfigurationBean {

        @Bean
        public Filter shallowEtagHeaderFilter() {
            return new SimpleCORSFilter();
        }

}
