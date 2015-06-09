package opPlanner.RESERvation;

import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.constraints.NotNull;

/**
 * Created by Thomas on 13.05.2015.
 */
@ConfigurationProperties(locations = "classpath:reservation.yml", ignoreUnknownFields = true, prefix = "application")
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

    public static class Klinisys {
        private int port;
        private String ipOrHostname;
        private String patientUrl;
        private String timeWindowUrl;
        private String timeWindowAltUrl;

        public String getTimeWindowAltUrl() {
            return timeWindowAltUrl;
        }

        public void setTimeWindowAltUrl(String timeWindowAltUrl) {
            this.timeWindowAltUrl = timeWindowAltUrl;
        }

        public String getTimeWindowUrl() {
            return timeWindowUrl;
        }

        public void setTimeWindowUrl(String timeWindowUrl) {
            this.timeWindowUrl = timeWindowUrl;
        }

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

    public static class OPMatcher {
        private int port;
        private String ipOrHostname;
        private String findOPSlotUrl;
        private String deleteFreeOPSlotUrl;
        private String addOPSlotByIdUrl;

        public String getAddOPSlotByIdUrl() {
            return addOPSlotByIdUrl;
        }

        public void setAddOPSlotByIdUrl(String addOPSlotByIdUrl) {
            this.addOPSlotByIdUrl = addOPSlotByIdUrl;
        }

        public String getDeleteFreeOPSlotUrl() {
            return deleteFreeOPSlotUrl;
        }

        public void setDeleteFreeOPSlotUrl(String deleteFreeOPSlotUrl) {
            this.deleteFreeOPSlotUrl = deleteFreeOPSlotUrl;
        }

        public String getFindOPSlotUrl() {
            return findOPSlotUrl;
        }

        public void setFindOPSlotUrl(String findOPSlotUrl) {
            this.findOPSlotUrl = findOPSlotUrl;
        }

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

    }

    public static class Notifier {
        private int port;
        private String ipOrHostname;
        private String createUrl;

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

        public String getCreateUrl() {
            return createUrl;
        }

        public void setCreateUrl(String createUrl) {
            this.createUrl = createUrl;
        }
    }



    @NotNull
    private MongoDb mongoDb;

    public MongoDb getMongoDb() {
        return mongoDb;
    }

    public void setMongoDb(MongoDb mongoDb) {
        this.mongoDb = mongoDb;
    }

    @NotNull
    private OPMatcher opMatcher;

    @NotNull
    private Klinisys klinisys;

    public OPMatcher getOpMatcher() {
        return opMatcher;
    }

    public void setOpMatcher(OPMatcher opMatcher) {
        this.opMatcher = opMatcher;
    }

    public Klinisys getKlinisys() {
        return klinisys;
    }

    public void setKlinisys(Klinisys klinisys) {
        this.klinisys = klinisys;
    }

    @NotNull
    private Notifier notifier;

    public Notifier getNotifier() {
        return notifier;
    }

    public void setNotifier(Notifier notifier) {
        this.notifier = notifier;
    }
}
