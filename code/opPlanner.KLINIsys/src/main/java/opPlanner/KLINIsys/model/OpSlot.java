package opPlanner.KLINIsys.model;

import org.hibernate.annotations.CollectionId;
import org.springframework.data.jpa.domain.AbstractPersistable;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Michael on 09.05.2015.
 */
@Entity
@Component
public class OpSlot extends AbstractPersistable<Long> {

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private Date slotStart;

    @Column(nullable = false)
    private Date slotEnd;

    @ManyToOne(optional = false, targetEntity = Hospital.class)
    @JoinColumn(name = "hospital_id", referencedColumnName = "id")
    private Hospital hospital;
/*
    @ManyToOne(targetEntity = Doctor.class)
    @JoinColumn(name = "doctor_id", referencedColumnName = "id")
    private Doctor doctor;
*/
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

    public Hospital getHospital() {
        return hospital;
    }

    public void setHospital(Hospital hospital) {
        this.hospital = hospital;
    }
/*
    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }
*/
}
