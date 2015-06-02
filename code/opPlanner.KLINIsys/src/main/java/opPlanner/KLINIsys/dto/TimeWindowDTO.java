package opPlanner.KLINIsys.dto;

import opPlanner.KLINIsys.model.TimeWindow;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Thomas on 01.06.2015.
 */
public class TimeWindowDTO {

    private Date startTime;

    private Date endTime;

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

    public TimeWindowDTO(Date startTime, Date endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public TimeWindowDTO() {

    }

    /**
     * convert a model of timeWindow to a timeWindowDTO
     * @param timeWindow
     * @return
     */
    public static TimeWindowDTO convertFromModel(TimeWindow timeWindow) {
        return new TimeWindowDTO(timeWindow.getSlotStart(),timeWindow.getSlotEnd());
    }

    /**
     * converts a list of timeWindows (model) to a list of timeWindowDTO
     * @param timeWindows
     * @return
     */
    public static List<TimeWindowDTO> convertFromModelList(List<TimeWindow> timeWindows) {
        List<TimeWindowDTO> dtoList = new LinkedList<>();
        for (TimeWindow tw : timeWindows) {
            dtoList.add(TimeWindowDTO.convertFromModel(tw));
        }
        return dtoList;
    }
}
