package opPlanner.OPmatcher.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.validation.constraints.NotNull;

/**
 * Created by Thomas on 12.05.2015.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Patient {

    private String eMail;
    private String name;
    private double x;
    private double y;

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

   /* public Patient(Long id) {
        this.setId(id);
    }*/

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {

        this.eMail = eMail;
    }

    @Override
    public String toString() {
        return "Patient [eMail=" + this.eMail + ", name=" + this.name + ", x="+this.x+", y="+this.y+"]";
    }

    public Patient(String eMail, double y, String name, double x) {
        this.eMail = eMail;
        this.y = y;
        this.name = name;
        this.x = x;
    }

    public Patient() {

    }
}
