package opPlanner.KLINIsys.service;

import opPlanner.KLINIsys.model.Doctor;
import opPlanner.KLINIsys.model.Hospital;
import opPlanner.KLINIsys.model.OpSlot;
import opPlanner.KLINIsys.model.Patient;
import opPlanner.KLINIsys.repository.OpSlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Michael on 09.05.2015.
 */
@Service
public class OpSlotService {

    @Autowired
    private OpSlotRepository opSlotRepository;

    public List<OpSlot> allOpSlots(Doctor doctor, Patient patient, Hospital hospital) {

        return opSlotRepository.findByHospitalAndDoctor(doctor, hospital);
    }

    public void deleteSlot(Long id) {
        OpSlot slot = opSlotRepository.findOne(id);

        if(slot == null)    return;

        //TODO: check that there is no reservation

        opSlotRepository.delete(slot);
    }
}
