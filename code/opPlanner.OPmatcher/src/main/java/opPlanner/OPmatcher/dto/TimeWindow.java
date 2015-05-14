package opPlanner.OPmatcher.dto;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * Created by Thomas on 10.05.2015.
 */
public class TimeWindow {

    private Date startTime;
    private Date endTime;

    public TimeWindow() {

    }

    public TimeWindow(@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date startTime, @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}
