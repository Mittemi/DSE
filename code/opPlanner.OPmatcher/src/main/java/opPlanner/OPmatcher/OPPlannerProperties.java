package opPlanner.OPmatcher;

import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.constraints.NotNull;

/**
 * Created by Thomas on 28.04.2015.
 */
@ConfigurationProperties(locations = "classpath:opPlanner.yml", ignoreUnknownFields = true, prefix = "application")
public class OPPlannerProperties {
   /* public static class MongoDb {
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
*/
    public static class Klinisys {
        private int port;
        private String ipOrHostname;
        private String patientUrl;

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

        public String getBaseUrl() {
            return "http://" + getIpOrHostname() + ":" + getPort() + "/";
        }

        public String getPatientUrl() {
            return patientUrl;
        }

        public void setPatientUrl(String patientUrl) {
            this.patientUrl = patientUrl;
        }
    }

/*
    @NotNull
    private MongoDb mongoDb;

    public MongoDb getMongoDBConf() {
        return mongoDb;
    }

    public void setMongoDBConf(MongoDb mongoDBConf) {
        this.mongoDb = mongoDBConf;
    }
*/
    //TODO thi: mongoDB config


    @NotNull
    private Klinisys klinisys;

    public Klinisys getKlinisys() {
        return klinisys;
    }

    public void setKlinisys(Klinisys klinisys) {
        this.klinisys = klinisys;
    }

}
