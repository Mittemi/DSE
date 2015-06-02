package opPlanner.KLINIsys.service;

import opPlanner.KLINIsys.model.Doctor;
import opPlanner.KLINIsys.model.TimeWindow;
import opPlanner.KLINIsys.repository.DoctorRepository;
import opPlanner.KLINIsys.repository.TimeWindowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by Michael on 28.04.2015.
 */
@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private TimeWindowRepository timeWindowRepository;

    public Iterable<Doctor> allDoctors() {
        return doctorRepository.findAll();
    }

}
