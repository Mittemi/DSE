package opPlanner.KLINIsys.service;

import opPlanner.KLINIsys.dto.ExtendedOpSlotListDTO;
import opPlanner.KLINIsys.dto.OPSlotDTO;
import opPlanner.KLINIsys.dto.OpSlotListDTO;
import opPlanner.KLINIsys.dto.ReservationDto;
import opPlanner.KLINIsys.model.Doctor;
import opPlanner.KLINIsys.model.Hospital;
import opPlanner.KLINIsys.model.OpSlot;
import opPlanner.KLINIsys.model.Patient;
import opPlanner.KLINIsys.repository.DoctorRepository;
import opPlanner.KLINIsys.repository.OpSlotRepository;
import opPlanner.KLINIsys.repository.PatientRepository;
import opPlanner.Shared.OpPlannerProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
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
    private DoctorRepository doctorRepository;

    @Autowired
    private OpPlannerProperties config;

    private RestTemplate restTemplate;

    public OpSlotService() {
        restTemplate = new RestTemplate();
    }

    /**
     * returns the list of op slot dtos.
     * depending on the type class the amount of information part of the list varies.
     * @param type Type of slot list (null = public, Hospital, Doctor, Patient)
     * @param doctor null or the doctor
     * @param patient null or the patient
     * @param hospital null or the hospital
     * @param from null or start date required if to is used
     * @param to null or end date required if from is used
     * @return list of op slots depending on type
     */
    public List<?extends OpSlotListDTO> getFilteredOpSlots(Class<?> type, Doctor doctor, Patient patient, Hospital hospital, Date from, Date to) {

        if (from != null)
            System.out.println("From: " + from);
        if (to != null)
            System.out.println("To: " + to);

        if (type == null) {      //public
            List<OpSlot> opSlots = opSlotRepository.findByHospitalAndTimeWindow(null, from, to);        // hospital is optional therefore use null to get the unfiltered list

            // get all reservations for the supplied list of ids
            Map<Long, ReservationDto> reservationInfos = getReservationDetails(opSlots.stream().map(x -> x.getId()).collect(Collectors.toList()));

            // merge klinisys data with reservation data
            List<OpSlotListDTO> result = opSlots.stream().map(x -> toOpSlotListDTO(x, reservationInfos)).collect(Collectors.toList());

            // set doctor info (mail, id, name)
            fillDoctorInfos(result);
            return result;
        }

        // doctor, hospital or patient
        if (type != null) {

            List<ExtendedOpSlotListDTO> result;
            List<OpSlot> opSlots = null;
            Map<Long, ReservationDto> reservationInfos = null;

            if(type.equals(Hospital.class)) {
                // hospital is optional therefore use null to get the unfiltered list
                opSlots = opSlotRepository.findByHospitalAndTimeWindow(hospital, from, to);
                List<Long> slotIds = opSlots.stream().map(x -> x.getId()).collect(Collectors.toList());
                reservationInfos = getReservationDetails(slotIds);
            } else if(type.equals(Patient.class)) {

                reservationInfos = getReservationDetailsByKey("patientId", patient.geteMail(), from, to);
                opSlots = opSlotRepository.findByIdIn(new LinkedList<>(reservationInfos.keySet()));
            } else if(type.equals(Doctor.class)) {
                reservationInfos = getReservationDetailsByKey("doctorId", doctor.geteMail(), from, to);
                opSlots = opSlotRepository.findByIdIn(new LinkedList<>(reservationInfos.keySet()));
            }

            final Map<Long, ReservationDto> finalReservationInfos = reservationInfos;
            // merge the information form reservation and klinisys
            result = opSlots.stream().map(x -> toExtendedOpSlotListDTO(x, finalReservationInfos)).collect(Collectors.toList());

            // set additional patient info (mail, name, id)
            fillPatientInfos(result);
            // set additional doctor infos (mail, name, id)
            fillDoctorInfos(result);
            return result;

        }
        return null;    // should not happen anyway
    }

    /**
     * sets the patient id and name when an email is present
     * @param result
     */
    private void fillPatientInfos(List<ExtendedOpSlotListDTO> result) {
        List<String> patientMails = result.stream().filter(x->x.getPatientName() != null).map(x->x.getPatientName()).collect(Collectors.toList());

        final Map<String, Patient> patients = patientRepository.findByeMailIn(patientMails).stream().collect(Collectors.toMap(Patient::geteMail, x -> x));

        result.stream().forEach(x -> {
            if(patients.containsKey(x.getPatientEmail())) {
                Patient patient = patients.get(x.getPatientEmail());
                x.setPatientId(patient.getId());
                x.setPatientName(patient.getName());
            }
        });
    }

    /**
     * sets the doctor id and name when an email is present
     * @param result
     */
    private void fillDoctorInfos(List<?extends OpSlotListDTO> result) {
        List<String> doctorMails = result.stream().filter(x->x.getDoctorName() != null).map(x->x.getDoctorName()).collect(Collectors.toList());
        final Map<String, Doctor> doctors = doctorRepository.findByeMailIn(doctorMails).stream().collect(Collectors.toMap(Doctor::geteMail, x->x));

        result.stream().forEach(x -> {
            if(doctors.containsKey(x.getDoctorEmail())) {
                Doctor doctor = doctors.get(x.getDoctorEmail());
                x.setDoctorId(doctor.getId());
                x.setDoctorName(doctor.getName());
            }
        });
    }

    /**
     * Contains only public data
     *
     * @param opSlot the opSlot
     * @param reservationInfos reservation for lookup
     * @return a new instance containing information about a possible reservation
     */
    private OpSlotListDTO toOpSlotListDTO(OpSlot opSlot, Map<Long, ReservationDto> reservationInfos) {

        OpSlotListDTO opSlotListDTO = new OpSlotListDTO(opSlot);
        opSlotListDTO.setFreeSlot(true);

        if(reservationInfos.containsKey(opSlotListDTO.getId())) {
            ReservationDto reservationDto = reservationInfos.get(opSlotListDTO.getId());

            opSlotListDTO.setDoctorEmail(reservationDto.getDoctorId());
            opSlotListDTO.setDoctorName(reservationDto.getDoctorId());

            opSlotListDTO.setFreeSlot(false);
        }
        return opSlotListDTO;
    }

    /**
     * Contains private data (patient information)
     * @param opSlot the opSlot
     * @param reservationInfos reservation information for lookup
     * @return an extendedopslotviewmodel containing all basic information as well as the patients name if reserved
     */
    private ExtendedOpSlotListDTO toExtendedOpSlotListDTO(OpSlot opSlot, Map<Long, ReservationDto> reservationInfos) {

        ExtendedOpSlotListDTO extendedOpSlotViewModel = new ExtendedOpSlotListDTO(opSlot);
        extendedOpSlotViewModel.setFreeSlot(true);

        if(reservationInfos.containsKey(extendedOpSlotViewModel.getId())) {
            ReservationDto reservationDto = reservationInfos.get(extendedOpSlotViewModel.getId());
            extendedOpSlotViewModel.setPatientName(reservationDto.getPatientId());
            extendedOpSlotViewModel.setPatientEmail(reservationDto.getPatientId());

            extendedOpSlotViewModel.setDoctorEmail(reservationDto.getDoctorId());
            extendedOpSlotViewModel.setDoctorName(reservationDto.getDoctorId());

            extendedOpSlotViewModel.setFreeSlot(false);
        }
        return extendedOpSlotViewModel;
    }

    /**
     *
     * @param slotIds the list of slot ids to get reservation information from the server
     * @return
     */
    public Map<Long, ReservationDto> getReservationDetails(List<Long> slotIds) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<List<Long>> entity = new HttpEntity<>(slotIds ,headers);

        ReservationDto[] result = restTemplate.postForObject(config.getReservation().buildUrl("reservation/findReservationsByOPSlots"), entity,  ReservationDto[].class/* slotIds.toArray(new Long[slotIds.size()])*/);

        return toOpSlotLookupMap(result);
    }

    /**
     * returns the list of reservations as a map with key = opslotId and value = reservation
     * @param result
     * @return
     */
    private Map<Long, ReservationDto> toOpSlotLookupMap(ReservationDto[] result) {
        Map<Long, ReservationDto> resultMap = new HashMap<>();

        for (ReservationDto reservationDTO : result) {
            resultMap.put(Long.parseLong(reservationDTO.getOpSlotId()), reservationDTO);
        }

        return resultMap;
    }

    /**
     *
     * @param key the name of the key parameter (doctorId, patientId)
     * @param keyValue the email address of the doctor/patient, required
     * @param from the start date for filtering (optional, required if to is present)
     * @param to the end date for filtering (optional, required if from is present)
     * @return the map of reservations
     */
    public Map<Long, ReservationDto> getReservationDetailsByKey(String key, String keyValue, Date from, Date to) {

        Map<String, Object> urlVariables = new HashMap<>();
        urlVariables.put(key, keyValue);

        String url = "reservation/findReservationsBy" + key.toUpperCase().substring(0,1) + key.substring(1);

        if(from != null && to != null) {
            url = url + "AndTW";
        }

        url = url + "?" + key + "={" + key + "}";

        if(from != null && to != null) {
            urlVariables.put("start", from);
            urlVariables.put("end", to);
            url = url + "&start={start}&end={end}";
        }

        ReservationDto[] result = restTemplate.getForObject(config.getReservation().buildUrl(url), ReservationDto[].class, urlVariables);

        return toOpSlotLookupMap(result);
    }

    /**
     * removes the slot from klinisys
     *  - therefore we need to make sure there exists no reservation for the supplied slot
     *
     *  the deletion is done in a 2 step process:
     *   1) delete the slot from opmatcher, it can't get reserved anymore!
     *   2) remove the slot from klinisys
     * @param email
     * @param id
     * @return
     */
    public boolean deleteSlot(String email, Long id) {
        OpSlot slot = opSlotRepository.findOne(id);

        if(slot == null)    return false;

        // check permissions
        if(!slot.getHospital().geteMail().equals(email))
            return false;

        Map<Long, ReservationDto> reservationDetails = getReservationDetails(Arrays.asList(new Long[]{id}));

        //there is no existing reservation for the slot, not enough to check only here
        if(reservationDetails.size() == 0) {

            // delete the slot from the opmatcher if it is still managed by the opmatcher --> no reservation
            boolean resultOpMatcher = sendSlotDeletingNotification(slot);

            if(!resultOpMatcher)    return false;

            opSlotRepository.delete(slot);
            // notification ??
            return true;
        }
        return false;
    }

    public Iterable<OpSlot> allOpSlots() {
        return opSlotRepository.findAll();
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

        sendSlotCreatedNotification(opSlot);
    }

    /**
     * Send a notification to the opmatcher about the newly created op slot
     * @param opSlot
     */
    private void sendSlotCreatedNotification(OpSlot opSlot) {
        //OPSlotDTO opSlotDTO = new OPSlotDTO(opSlot.getId(),opSlot.getHospital().getId(), opSlot.getHospital().getX(), opSlot.getHospital().getY(), opSlot.getSlotStart(), opSlot.getSlotEnd(), opSlot.getType());

        String url = config.getOpMatcher().buildUrl("addOPSlotById/" + opSlot.getId());

        restTemplate.getForObject(url, String.class);
        System.out.println("Create slot notification sent");
    }

    /**
     * Notify the opmatcher about the upcomming deletion of the supplied slot
     * @return is it possible to delete this slot
     * @param opSlot
     */
    private boolean sendSlotDeletingNotification(OpSlot opSlot) {

        String url = config.getOpMatcher().buildUrl("delete/" + opSlot.getId());
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.DELETE, null, String.class);

        System.out.println("Delete slot notification sent");
        return responseEntity.getStatusCode() == HttpStatus.OK;
    }
}
