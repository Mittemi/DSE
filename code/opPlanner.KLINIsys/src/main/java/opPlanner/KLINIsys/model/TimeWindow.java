package opPlanner.KLINIsys.model;

import org.springframework.data.jpa.domain.AbstractPersistable;
import org.springframework.stereotype.Component;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Date;

/**
 * Created by Thomas on 13.05.2015.
 */
@Entity
@Component
public class TimeWindow extends AbstractPersistable<Long> {
    @Column(nullable = false)
    private Date slotStart;

    @Column(nullable = false)
    private Date slotEnd;

    @ManyToOne(targetEntity = Doctor.class)
    @JoinColumn(name = "doctor_id", referencedColumnName = "id")
    private Doctor doctor;

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

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public TimeWindow(Date slotStart, Date slotEnd) {
        this.slotStart = slotStart;
        this.slotEnd = slotEnd;
    }

    public TimeWindow() {

    }
}
