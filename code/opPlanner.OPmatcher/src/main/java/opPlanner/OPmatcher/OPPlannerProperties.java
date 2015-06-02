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

    public static class Reservation {
        private int port;
        private String ipOrHostname;
        private String reservationByDoctorUrl;
        private String findReservationsByDoctorIdAndTWURL;

        public String getFindReservationsByDoctorIdAndTWURL() {
            return findReservationsByDoctorIdAndTWURL;
        }

        public void setFindReservationsByDoctorIdAndTWURL(String findReservationsByDoctorIdAndTWURL) {
            this.findReservationsByDoctorIdAndTWURL = findReservationsByDoctorIdAndTWURL;
        }

        public String getReservationByDoctorUrl() {
            return reservationByDoctorUrl;
        }

        public void setReservationByDoctorUrl(String reservationByDoctorUrl) {
            this.reservationByDoctorUrl = reservationByDoctorUrl;
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
    private Reservation reservation;

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    @NotNull
    private Klinisys klinisys;

    public Klinisys getKlinisys() {
        return klinisys;
    }

    public void setKlinisys(Klinisys klinisys) {
        this.klinisys = klinisys;
    }

}
