package opPlanner.KLINIsys.service;

import opPlanner.KLINIsys.dto.ExtendedOpSlotViewModel;
import opPlanner.KLINIsys.dto.OpSlotViewModel;
import opPlanner.KLINIsys.model.Doctor;
import opPlanner.KLINIsys.model.Hospital;
import opPlanner.KLINIsys.model.OpSlot;
import opPlanner.KLINIsys.model.Patient;
import opPlanner.KLINIsys.repository.OpSlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Michael on 09.05.2015.
 */
@Service
public class OpSlotService {

    @Autowired
    private OpSlotRepository opSlotRepository;

    private RestTemplate restTemplate;

    public OpSlotService() {
        restTemplate = new RestTemplate();
    }

    public List<?extends OpSlotViewModel> getFilteredOpSlots(Doctor doctor, Patient patient, Hospital hospital, Date from, Date to) {

        List<OpSlot> opSlots = opSlotRepository.findByHospitalDoctorAndTimeWindow(doctor, hospital, from, to);

        // todo: implement call to reservation, filter list
        if(from != null)
            System.out.println("From: " + from);
        if(to != null)
            System.out.println("To: " + to);

        //todo change this
        if(patient == null) {
            return opSlots.stream().map(x -> new OpSlotViewModel(x)).collect(Collectors.toList());
        }
        else  {
            return opSlots.stream().map(x -> new ExtendedOpSlotViewModel(x, patient)).collect(Collectors.toList());
        }
    }

    public void deleteSlot(Long id) {
        OpSlot slot = opSlotRepository.findOne(id);

        if(slot == null)    return;

        //TODO: check that there is no reservation

        opSlotRepository.delete(slot);
    }

    public List<OpSlot> allOpSlots(Doctor doctor, Patient patient, Hospital hospital) {

        return opSlotRepository.findByHospitalAndDoctor(doctor, hospital);
    }
    
    public List<OpSlot> allOpSlots(Hospital hospital) {

        return opSlotRepository.findByHospital_EMail(hospital.geteMail());
    }
}
