package opPlanner.KLINIsys.service;

import opPlanner.KLINIsys.dto.ExtendedOpSlotViewModel;
import opPlanner.Shared.dto.NotificationDTO;
import opPlanner.KLINIsys.dto.OpSlotViewModel;
import opPlanner.KLINIsys.dto.ReservationDto;
import opPlanner.KLINIsys.model.Doctor;
import opPlanner.KLINIsys.model.Hospital;
import opPlanner.KLINIsys.model.OpSlot;
import opPlanner.KLINIsys.model.Patient;
import opPlanner.KLINIsys.repository.OpSlotRepository;
import opPlanner.KLINIsys.repository.PatientRepository;
import opPlanner.Shared.OpPlannerProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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

        List<OpSlot> opSlots = opSlotRepository.findByHospitalAndTimeWindow(hospital, from, to);
        Map<Long, ReservationDto> reservationInfos = getReservationDetails(opSlots.stream().map(x -> x.getId()).collect(Collectors.toList()));

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

    private OpSlotViewModel enrichBasic(OpSlot opSlot, Map<Long, ReservationDto> reservationInfos) {

        OpSlotViewModel opSlotViewModel = new OpSlotViewModel(opSlot);
        opSlotViewModel.setFreeSlot(true);

        if(reservationInfos.containsKey(opSlotViewModel.getId())) {
            opSlotViewModel.setFreeSlot(false);
        }
        return opSlotViewModel;
    }

    private ExtendedOpSlotViewModel enrichExtended(OpSlot opSlot, Map<Long, ReservationDto> reservationInfos) {

        ExtendedOpSlotViewModel extendedOpSlotViewModel = new ExtendedOpSlotViewModel(opSlot);
        extendedOpSlotViewModel.setFreeSlot(true);

        if(reservationInfos.containsKey(extendedOpSlotViewModel.getId())) {
            extendedOpSlotViewModel.setPatientName(reservationInfos.get(extendedOpSlotViewModel.getId()).getPatientId());
            extendedOpSlotViewModel.setFreeSlot(false);
        }
        return extendedOpSlotViewModel;
    }

    public Map<Long, ReservationDto> getReservationDetails(List<Long> slotIds) {
        Map<String, Object[]> urlVariables = new HashMap<>();
        urlVariables.put("slotIds", slotIds.toArray());

        ReservationDto[] result = restTemplate.getForObject(config.getReservation().buildUrl("findReservationsByOPSlots?opSlotId={slotIds}"), ReservationDto[].class, urlVariables);

        Map<Long, ReservationDto> resultMap = new HashMap<>();

        for (ReservationDto reservationDTO : result) {
            resultMap.put(Long.parseLong(reservationDTO.getOpSlotId()), reservationDTO);
        }

        return resultMap;
    }

    public boolean deleteSlot(String email, Long id) {
        OpSlot slot = opSlotRepository.findOne(id);

        if(slot == null)    return false;

        if(!slot.getHospital().geteMail().equals(email))
            return false;

        Map<Long, ReservationDto> reservationDetails = getReservationDetails(Arrays.asList(new Long[]{id}));

        if(reservationDetails.size() == 0) {
            opSlotRepository.delete(slot);
            // notification ??
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


    /**
     * creates the slot if all fields are valid otherwise it does nothing
     * @param hospital
     * @param opSlot
     */
    public void createSlot(Hospital hospital, OpSlot opSlot) {

        opSlot.setHospital(hospital);

        // date required and start < end
        if(opSlot.getSlotStart() == null ||opSlot.getSlotEnd() == null ||opSlot.getSlotStart().after(opSlot.getSlotEnd()))
            return;

        //future slots only
        if(opSlot.getSlotStart().before(new Date()))
            return;

        // type required
        if(opSlot.getType() == null)
            return;

        // hospital required
        if(opSlot.getHospital() == null)
            return;

        opSlotRepository.save(opSlot);
    }
}
