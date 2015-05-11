package opPlanner.RESERvation.dto;

/**
 * Created by Thomas on 11.05.2015.
 */
public class Patient {
    private String patientId;
    private double x;
    private double y;

    public Patient(String patientId, double x, double y) {
        this.patientId = patientId;
        this.x = x;
        this.y = y;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

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
}
