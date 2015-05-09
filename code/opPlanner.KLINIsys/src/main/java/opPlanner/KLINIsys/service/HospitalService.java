package opPlanner.KLINIsys.service;

import opPlanner.KLINIsys.model.Hospital;
import opPlanner.KLINIsys.repository.HospitalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Michael on 08.04.2015.
 */
@Service
public class HospitalService {

    @Autowired
    private HospitalRepository hospitalRepository;

    public Iterable<Hospital> allHospitals() {
        return hospitalRepository.findAll();
    }
}
