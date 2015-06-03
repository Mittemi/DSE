package opPlanner.KLINIsys.service;

import opPlanner.KLINIsys.dto.ExtendedOpSlotViewModel;
import opPlanner.KLINIsys.dto.OpSlotViewModel;
import opPlanner.KLINIsys.dto.ReservationDTO;
import opPlanner.KLINIsys.model.Doctor;
import opPlanner.KLINIsys.model.Hospital;
import opPlanner.KLINIsys.model.OpSlot;
import opPlanner.KLINIsys.model.Patient;
import opPlanner.KLINIsys.repository.DoctorRepository;
import opPlanner.KLINIsys.repository.OpSlotRepository;
import opPlanner.KLINIsys.repository.PatientRepository;
import opPlanner.Shared.OpPlannerProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Michael on 09.05.2015.
 */
@Service
public class OpSlotService {

    @Autowired
    private OpSlotRepository opSlotRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private OpPlannerProperties config;

    private RestTemplate restTemplate;

    public OpSlotService() {
        restTemplate = new RestTemplate();
    }

    public List<?extends OpSlotViewModel> getFilteredOpSlots(Class<?> type, Doctor doctor, Patient patient, Hospital hospital, Date from, Date to) {

        List<OpSlot> opSlots = opSlotRepository.findByHospitalDoctorAndTimeWindow(doctor, hospital, from, to);
        Map<Long, ReservationDTO> reservationInfos = getReservationDetails(opSlots.stream().map(x -> x.getId()).collect(Collectors.toList()));

        if (from != null)
            System.out.println("From: " + from);
        if (to != null)
            System.out.println("To: " + to);

        if (type == null) {      //public
            List<OpSlotViewModel> result = opSlots.stream().map(x -> enrichBasic(x, reservationInfos)).collect(Collectors.toList());
            return result;
        }

        // doctor, hospital or patient
        if (type != null) {
            List<ExtendedOpSlotViewModel> result = opSlots.stream().map(x -> enrichExtended(x, reservationInfos)).collect(Collectors.toList());

            fillPatientInfos(result);

            return result;
        }
        return null;    // should not happen anyway
    }

    private void fillPatientInfos(List<ExtendedOpSlotViewModel> result) {
        List<String> patientIds = result.stream().filter(x->x.getPatientName() != null).map(x->x.getPatientName()).collect(Collectors.toList());

        Map<String, Patient> patients = patientRepository.findByeMailIn(patientIds).stream().collect(Collectors.toMap(Patient::geteMail, x -> x));

        result.stream().forEach(x -> { if(patients.containsKey(x)) x.setPatientId(patients.get(x).getId()); });
    }

    private OpSlotViewModel enrichBasic(OpSlot opSlot, Map<Long, ReservationDTO> reservationInfos) {

        OpSlotViewModel opSlotViewModel = new OpSlotViewModel(opSlot);
        opSlotViewModel.setFreeSlot(true);

        if(reservationInfos.containsKey(opSlotViewModel.getId())) {
            opSlotViewModel.setFreeSlot(false);
        }
        return opSlotViewModel;
    }

    private ExtendedOpSlotViewModel enrichExtended(OpSlot opSlot, Map<Long, ReservationDTO> reservationInfos) {

        ExtendedOpSlotViewModel extendedOpSlotViewModel = new ExtendedOpSlotViewModel(opSlot);
        extendedOpSlotViewModel.setFreeSlot(true);

        if(reservationInfos.containsKey(extendedOpSlotViewModel.getId())) {
            extendedOpSlotViewModel.setPatientName(reservationInfos.get(extendedOpSlotViewModel.getId()).getPatientId());
            extendedOpSlotViewModel.setFreeSlot(false);
        }
        return extendedOpSlotViewModel;
    }

    public Map<Long, ReservationDTO> getReservationDetails(List<Long> slotIds) {
        Map<String, Object[]> urlVariables = new HashMap<>();
        urlVariables.put("slotIds", slotIds.toArray());

        ReservationDTO[] result = restTemplate.getForObject(config.getReservation().buildUrl("findReservationsByOPSlots?opSlotId={slotIds}"), ReservationDTO[].class, urlVariables);

        Map<Long, ReservationDTO> resultMap = new HashMap<>();

        for (ReservationDTO reservationDTO : result) {
            resultMap.put(Long.parseLong(reservationDTO.getOpSlotId()), reservationDTO);
        }

        return resultMap;
    }

    public boolean deleteSlot(Long id) {
        OpSlot slot = opSlotRepository.findOne(id);

        if(slot == null)    return false;

        Map<Long, ReservationDTO> reservationDetails = getReservationDetails(Arrays.asList(new Long[]{id}));

        if(reservationDetails.size() == 0) {
            opSlotRepository.delete(slot);
            return true;
        }
        return false;
    }
    
    public List<OpSlot> allOpSlots(Hospital hospital) {

        return opSlotRepository.findByHospital_EMail(hospital.geteMail());
    }

    public OpSlot getOPSlotById(Long id) {
        return opSlotRepository.findOne(id);
    }


}
