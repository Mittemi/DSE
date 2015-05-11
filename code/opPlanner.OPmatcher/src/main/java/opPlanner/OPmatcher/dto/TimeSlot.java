package opPlanner.OPmatcher.dto;

import java.util.Date;

/**
 * Created by Thomas on 10.05.2015.
 */
public class TimeSlot {
    private Date startTime;
    private Date endTime;

    public TimeSlot(Date startTime, Date endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}
