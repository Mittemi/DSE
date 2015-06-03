package opPlanner.KLINIsys.dto;

import opPlanner.KLINIsys.model.OpSlot;
import opPlanner.KLINIsys.model.Patient;

/**
 * Created by Michael on 20.05.2015.
 */
public class ExtendedOpSlotViewModel extends OpSlotViewModel {
    public ExtendedOpSlotViewModel(OpSlot opSlot) {
        super(opSlot);
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
