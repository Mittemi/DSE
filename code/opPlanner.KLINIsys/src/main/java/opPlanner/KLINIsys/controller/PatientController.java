package opPlanner.KLINIsys.controller;

import opPlanner.KLINIsys.dto.OpSlotListDTO;
import opPlanner.KLINIsys.dto.PatientDTO;
import opPlanner.KLINIsys.model.Patient;
import opPlanner.KLINIsys.repository.PatientRepository;
import opPlanner.KLINIsys.service.OpSlotService;
import opPlanner.KLINIsys.service.PatientService;
import opPlanner.Shared.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Michael on 28.04.2015.
 */
@RestController
@RequestMapping("/patient")
public class PatientController {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private OpSlotService opSlotService;

    @Autowired
    private PatientService patientService;

    @RequestMapping(value = "/findPatientByEmail", method = RequestMethod.GET, produces = "application/json")
    public Patient getPatientByEmail(@RequestParam("email")String eMail) {
        return patientService.getPatientByEmail(eMail);
    }

    @RequestMapping(value = "/autoComplete", method = RequestMethod.GET, produces = "application/json")
    public List<PatientDTO> autoComplete(@RequestParam String filter) {
        return patientRepository.findByNameStartsWith(filter).stream().map(x-> new PatientDTO(x)).collect(Collectors.toList());
    }
    
    @RequestMapping(value = "/{mail}/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<?extends OpSlotListDTO> getOpSlots(@PathVariable("mail")String mail,
                                                     @RequestParam(value = "from", required = false)  @DateTimeFormat(pattern = Constants.DATETIME_FORMAT_STRING_WITH_TZ) Date dateFrom,
                                                     @RequestParam(value = "to", required = false)  @DateTimeFormat(pattern = Constants.DATETIME_FORMAT_STRING_WITH_TZ) Date dateTo,
                                                     HttpServletResponse response) {

        Patient patient = patientRepository.findByEmail(mail);

        if (patient == null) {
            response.setStatus(404);
            return null;
        }

        return opSlotService.getFilteredOpSlots(Patient.class, null, patient, null, dateFrom, dateTo);
    }
}
