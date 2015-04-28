package opPlanner.OPmatcher;

import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.constraints.NotNull;

/**
 * Created by Thomas on 28.04.2015.
 */
@ConfigurationProperties(locations = "classpath:opPlanner.yml", ignoreUnknownFields = true, prefix = "application")
public class OPPlannerProperties {
    public static class MongoDb {
        private int port;
        private String ipOrHostname;

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public String getIpOrHostname() {
            return ipOrHostname;
        }

        public void setIpOrHostname(String ipOrHostname) {
            this.ipOrHostname = ipOrHostname;
        }
    }

    @NotNull
    private MongoDb mongoDBConf;

    public MongoDb getMongoDBConf() {
        return mongoDBConf;
    }

    public void setMongoDBConf(MongoDb mongoDBConf) {
        this.mongoDBConf = mongoDBConf;
    }
}
