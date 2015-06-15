package opPlanner.KLINIsys.dto;

import opPlanner.KLINIsys.model.Patient;

/**
 * Created by Michael on 15.06.2015.
 */
public class PatientDTO {

    private String name;

    private String eMail;

    private Long id;

    public PatientDTO(Patient patient) {
        this.name = patient.getName();
        this.eMail = patient.geteMail();
        this.id = patient.getId();
    }

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
