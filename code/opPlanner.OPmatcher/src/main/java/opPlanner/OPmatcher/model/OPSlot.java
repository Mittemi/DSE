package opPlanner.OPmatcher.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * Created by Thomas on 18.04.2015.
 */
@Document(collection = "freeOPSlots")
public class OPSlot {

    @Id
    private long id;

    private long hospitalId;
    private Date start;
    private Date end;

    public OPSlot(long hospitalId, Date start, Date end) {
        this.hospitalId = hospitalId;
        this.start = start;
        this.end = end;
    }

    public long getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(long hospitalId) {
        this.hospitalId = hospitalId;
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

    public long getId() {
        return id;
    }

}
