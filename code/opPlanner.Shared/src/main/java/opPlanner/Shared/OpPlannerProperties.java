package opPlanner.Shared;

//import com.sun.istack.internal.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by Michael on 18.04.2015.
 */
@ConfigurationProperties(locations = "classpath:opPlanner.yml", ignoreUnknownFields = true, prefix = "application")
public class OpPlannerProperties {

    public static class ServiceConfigBase {
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

        public String getBaseUrl() {
            return "http://" + getIpOrHostname() + ":" + getPort() + "/";
        }

        public String buildUrl(String relative) {
            return getBaseUrl() + relative;
        }
    }

    public static class OpMatcher extends ServiceConfigBase {

    }

    public static class Klinisys extends ServiceConfigBase {

    }

    public static class Reservation extends ServiceConfigBase {

    }

    public static class Notifier extends ServiceConfigBase {

        private String mongoDb;

        public String getMongoDb() {
            return mongoDb;
        }

        public void setMongoDb(String mongoDb) {
            this.mongoDb = mongoDb;
        }
    }

    //@NotNull
    private Klinisys klinisys;

    //@NotNull
    private Reservation reservation;

  //  @NotNull
    private Notifier notifier;

//    @NotNull
    private OpMatcher opMatcher;

    public OpMatcher getOpMatcher() {
        return opMatcher;
    }

    public void setOpMatcher(OpMatcher opMatcher) {
        this.opMatcher = opMatcher;
    }

    public Notifier getNotifier() {
        return notifier;
    }

    public void setNotifier(Notifier notifier) {
        this.notifier = notifier;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public Klinisys getKlinisys() {
        return klinisys;
    }

    public void setKlinisys(Klinisys klinisys) {
        this.klinisys = klinisys;
    }
}
