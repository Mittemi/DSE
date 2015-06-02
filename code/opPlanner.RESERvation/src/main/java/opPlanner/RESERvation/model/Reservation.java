package opPlanner.RESERvation.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * Created by Thomas on 10.05.2015.
 */
@Document(collection = "reservations")
public class Reservation {

    @Id
    private String reservationId;
    private String opSlotId;    //reserved op slot
    private Date start;     //opSlot start date and time
    private Date end;       //opSlot end date and time
    private String patientId; //patient mail address
    private String doctorId; //doctor mail address

    public String getOpSlotId() {
        return opSlotId;
    }

    public void setOpSlotId(String opSlotId) {
        this.opSlotId = opSlotId;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
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

    public Reservation(String opSlotId, Date start, Date end, String patientId, String doctorId) {
        this.opSlotId = opSlotId;
        this.start = start;
        this.end = end;
        this.patientId = patientId;
        this.doctorId = doctorId;
    }

    public Reservation() {

    }
}
