package opPlanner.KLINIsys.dto;

import java.awt.image.Kernel;

/**
 * Created by Michael on 03.06.2015.
 */
public class ReservationDTO {

    private String opSlotId;
    private String patientId;
    private String doctorId;

    public String getOpSlotId() {
        return opSlotId;
    }

    public void setOpSlotId(String opSlotId) {
        this.opSlotId = opSlotId;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }
}
