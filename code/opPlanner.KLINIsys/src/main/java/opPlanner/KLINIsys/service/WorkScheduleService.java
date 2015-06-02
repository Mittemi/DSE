package opPlanner.KLINIsys.service;

import opPlanner.KLINIsys.model.TimeWindow;
import opPlanner.KLINIsys.repository.TimeWindowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by Thomas on 01.06.2015.
 */
@Service
public class WorkScheduleService {

    @Autowired
    TimeWindowRepository timeWindowRepository;

    public List<TimeWindow> getWorkSchedule(String doctorEMail) {
        return timeWindowRepository.findByDoctor_EMail(doctorEMail);
    }

    public List<TimeWindow> getWorkSchedule(String doctorEMail, Date from, Date to) {
        return timeWindowRepository.findByDoctorAndTimeWindow(doctorEMail, from, to);
    }
}
