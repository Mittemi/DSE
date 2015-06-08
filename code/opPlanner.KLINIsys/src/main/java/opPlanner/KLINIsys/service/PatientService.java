package opPlanner.KLINIsys.service;

import opPlanner.KLINIsys.model.Patient;
import opPlanner.KLINIsys.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Michael on 28.04.2015.
 */
@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    public Patient getPatientByEmail(String eMail) {
        return patientRepository.findByEmail(eMail);
    }
}
