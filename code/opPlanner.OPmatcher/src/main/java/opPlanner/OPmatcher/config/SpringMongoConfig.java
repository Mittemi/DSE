package opPlanner.OPmatcher.config;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import opPlanner.OPmatcher.OPPlannerProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;

/**
 * Created by Thomas on 18.04.2015.
 */
@Configuration
public class SpringMongoConfig extends AbstractMongoConfiguration {
    @Override
    public String getDatabaseName() {
        return "opMatchDB";
    }

    @Autowired
    private OPPlannerProperties properties;

    @Override
    @Bean
    public Mongo mongo() throws Exception {
        return new MongoClient(properties.getMongoDb().getIpOrHostname());
    }
}
