package opPlanner.KLINIsys.controller;


import opPlanner.KLINIsys.dto.OpSlotListDTO;
import opPlanner.KLINIsys.model.Doctor;
import opPlanner.KLINIsys.repository.DoctorRepository;
import opPlanner.KLINIsys.service.OpSlotService;
import opPlanner.Shared.Constants;
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
@RequestMapping("/doctor")
public class DoctorController {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private OpSlotService opSlotService;

    @RequestMapping(value = "/{mail}/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<?extends OpSlotListDTO> getOpSlots(@PathVariable("mail")String mail,
                                                     @RequestParam(value = "from", required = false)  @DateTimeFormat(pattern = Constants.DATETIME_FORMAT_STRING_WITH_TZ) Date dateFrom,
                                                     @RequestParam(value = "to", required = false)  @DateTimeFormat(pattern = Constants.DATETIME_FORMAT_STRING_WITH_TZ) Date dateTo,
                                                     HttpServletResponse response) {

        Doctor doctor = doctorRepository.findByEmail(mail);

        if (doctor == null) {
            response.setStatus(404);
            return null;
        }

        return opSlotService.getFilteredOpSlots(Doctor.class, doctor, null, null, dateFrom, dateTo);
    }
}
