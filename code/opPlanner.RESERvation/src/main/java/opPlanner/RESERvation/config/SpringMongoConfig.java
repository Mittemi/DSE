package opPlanner.RESERvation.config;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;

/**
 * Created by Thomas on 10.05.2015.
 */
@Configuration
public class SpringMongoConfig extends AbstractMongoConfiguration {
    @Override
    public String getDatabaseName() {
        return "reservationDB";
    }

    //TODO thi: OPPlannerProperties

    //@Autowired
    //ReservationProperties properties;

    @Override
    @Bean
    public Mongo mongo() throws Exception {
        //return new MongoClient(properties.getMongoDBConf().getIpOrHostname());
        return new MongoClient("192.168.59.103");
    }
}
