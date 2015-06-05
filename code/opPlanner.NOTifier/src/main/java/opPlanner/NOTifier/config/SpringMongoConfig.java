package opPlanner.NOTifier.config;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import opPlanner.Shared.OpPlannerProperties;
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
        return "notification";
    }

    @Autowired
    private OpPlannerProperties properties;

    @Override
    @Bean
    public Mongo mongo() throws Exception {
        return new MongoClient(properties.getNotifier().getMongoDb());
    }
}
