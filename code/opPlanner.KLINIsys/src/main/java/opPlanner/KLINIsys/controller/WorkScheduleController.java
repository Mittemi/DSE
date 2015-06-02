package opPlanner.KLINIsys.controller;

import opPlanner.KLINIsys.dto.TimeWindowDTO;
import opPlanner.KLINIsys.model.TimeWindow;
import opPlanner.KLINIsys.service.WorkScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Time;
import java.util.Date;
import java.util.List;

/**
 * Created by Thomas on 01.06.2015.
 */
@RestController
@RequestMapping("/workSchedule")
public class WorkScheduleController {

    @Autowired
    private WorkScheduleService workScheduleService;

    @RequestMapping(value = "/findWorkScheduleByDoctor", method = RequestMethod.GET, produces = "application/json")
    public List<TimeWindowDTO> getWorkScheduleByDoctor(@RequestParam("email")String doctorEmail) {
        List<TimeWindow> workSchedule = workScheduleService.getWorkSchedule(doctorEmail);
        return TimeWindowDTO.convertFromModelList(workSchedule);
    }
    @RequestMapping(value = "/findWorkScheduleByDoctorAndTimeWindow", method = RequestMethod.GET, produces =
            "application/json")
    public List<TimeWindowDTO> getWorkScheduleByDoctorAndTimeWindow(@RequestParam("email")String eMail,
                                                                 @RequestParam("startTime")@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date dateFrom,
                                                                 @RequestParam("endTime")@DateTimeFormat(pattern =
                                                                         "yyyy-MM-dd " +
                                                                         "HH:mm") Date dateTo) {
        List<TimeWindow> workSchedule = workScheduleService.getWorkSchedule(eMail, dateFrom, dateTo);
        return TimeWindowDTO.convertFromModelList(workSchedule);
    }

}
