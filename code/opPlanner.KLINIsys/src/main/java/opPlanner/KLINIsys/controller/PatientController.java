package opPlanner.KLINIsys.controller;

import opPlanner.KLINIsys.Constants;
import opPlanner.KLINIsys.dto.OpSlotViewModel;
import opPlanner.KLINIsys.model.Hospital;
import opPlanner.KLINIsys.model.Patient;
import opPlanner.KLINIsys.repository.HospitalRepository;
import opPlanner.KLINIsys.repository.PatientRepository;
import opPlanner.KLINIsys.service.OpSlotService;
import opPlanner.KLINIsys.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * Created by Michael on 28.04.2015.
 */
@RestController
@RequestMapping("/patient")
public class PatientController {

    @Autowired
    private PatientRepository patientRepository;
/*
    @RequestMapping(value = "/", method = RequestMethod.GET, produces = "application/json")
    public Iterable<Patient> index() {
        return patientService.allPatients();
    }*/

    @Autowired
    private OpSlotService opSlotService;

    @RequestMapping(value = "/{mail}/", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<?extends OpSlotViewModel> getOpSlots(@PathVariable("mail")String mail,
                                                     @RequestParam(value = "from", required = false)  @DateTimeFormat(pattern = Constants.DATE_FORMAT_STRING) Date dateFrom,
                                                     @RequestParam(value = "to", required = false)  @DateTimeFormat(pattern = Constants.DATE_FORMAT_STRING) Date dateTo,
                                                     HttpServletResponse response) {

        Patient patient = patientRepository.findByEmail(mail);

        if (patient == null) {
            response.setStatus(404);
            return null;
        }

        return opSlotService.getFilteredOpSlots(null, patient, null, dateFrom, dateTo);
    }
}
