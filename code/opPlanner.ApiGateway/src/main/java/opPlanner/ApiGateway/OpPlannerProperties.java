package opPlanner.ApiGateway;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

/**
 * Created by Michael on 18.04.2015.
 */
@ConfigurationProperties(locations = "classpath:opPlanner.yml", ignoreUnknownFields = true, prefix = "application")
public class OpPlannerProperties {

    public static class Klinisys {
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
    private Klinisys klinisys;

    public Klinisys getKlinisys() {
        return klinisys;
    }

    public void setKlinisys(Klinisys klinisys) {
        this.klinisys = klinisys;
    }
}
