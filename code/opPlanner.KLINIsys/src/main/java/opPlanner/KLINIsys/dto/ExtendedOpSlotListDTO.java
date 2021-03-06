package opPlanner.KLINIsys.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import opPlanner.KLINIsys.model.OpSlot;

/**
 * Created by Michael on 20.05.2015.
 */
public class ExtendedOpSlotListDTO extends OpSlotListDTO {
    public ExtendedOpSlotListDTO(OpSlot opSlot) {
        super(opSlot);
    }

    private String patientName;

    @JsonIgnore
    private String patientEmail;

    public String getPatientEmail() {
        return patientEmail;
    }

    public void setPatientEmail(String patientEmail) {
        this.patientEmail = patientEmail;
    }

    private Long patientId;

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }
}
