package opPlanner.KLINIsys.dto;

import opPlanner.KLINIsys.model.OpSlot;
import opPlanner.KLINIsys.model.Patient;

/**
 * Created by Michael on 20.05.2015.
 */
public class ExtendedOpSlotViewModel extends OpSlotViewModel {
    public ExtendedOpSlotViewModel(OpSlot opSlot, Patient patient) {
        super(opSlot);
        if(patient != null) {
            this.patientName = patient.getName();
            this.patientId = patient.getId();
            setFreeSlot(false);
        }
    }

    private String patientName;

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
