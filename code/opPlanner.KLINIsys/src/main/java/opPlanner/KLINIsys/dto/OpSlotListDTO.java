package opPlanner.KLINIsys.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import opPlanner.KLINIsys.model.OpSlot;
import opPlanner.Shared.Constants;

import java.util.Date;

/**
 * Created by Michael on 20.05.2015.
 */
public class OpSlotListDTO {

    private Long id;

    private String type;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATETIME_FORMAT_STRING, timezone = Constants.TIME_ZONE)
    private Date slotStart;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Constants.DATETIME_FORMAT_STRING, timezone = Constants.TIME_ZONE)
    private Date slotEnd;

    private String doctorName;

    private Long doctorId;

    @JsonIgnore
    private String doctorEmail;


    private String hospitalName;

    private Long hospitalId;

    private boolean freeSlot;

    public OpSlotListDTO(OpSlot opSlot) {
        this.id = opSlot.getId();
        this.type = opSlot.getType();
        this.slotStart = opSlot.getSlotStart();
        this.slotEnd = opSlot.getSlotEnd();
        this.hospitalId = opSlot.getHospital().getId();
        this.hospitalName = opSlot.getHospital().getShortName();
        freeSlot = true;
    }

    public String getDoctorEmail() {
        return doctorEmail;
    }

    public void setDoctorEmail(String doctorEmail) {
        this.doctorEmail = doctorEmail;
    }

    public Long getHospitalId() {
        return hospitalId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getSlotStart() {
        return slotStart;
    }

    public void setSlotStart(Date slotStart) {
        this.slotStart = slotStart;
    }

    public Date getSlotEnd() {
        return slotEnd;
    }

    public void setSlotEnd(Date slotEnd) {
        this.slotEnd = slotEnd;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public void setHospitalId(Long hospitalId) {
        this.hospitalId = hospitalId;
    }

    public boolean isFreeSlot() {
        return freeSlot;
    }

    public void setFreeSlot(boolean freeSlot) {
        this.freeSlot = freeSlot;
    }
}
