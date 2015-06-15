package opPlanner.KLINIsys.controller;

import opPlanner.KLINIsys.dto.OpSlotListDTO;
import opPlanner.KLINIsys.model.Hospital;
import opPlanner.KLINIsys.repository.HospitalRepository;
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
 * Created by Michael on 18.04.2015.
 */
@RestController
@RequestMapping("/hospital")
public class HospitalController {

    @Autowired
    private OpSlotService opSlotService;

    @Autowired
    private HospitalRepository hospitalRepository;

    @RequestMapping(value = "/{mail}/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<?extends OpSlotListDTO> getOpSlots(@PathVariable("mail")String mail,
                                                     @RequestParam(value = "from", required = false)  @DateTimeFormat(pattern = Constants.DATE_FORMAT_STRING) Date dateFrom,
                                                     @RequestParam(value = "to", required = false)  @DateTimeFormat(pattern = Constants.DATE_FORMAT_STRING) Date dateTo,
                                                     HttpServletResponse response) {

        Hospital hospital = hospitalRepository.findByEmail(mail);

        if (hospital == null) {
            response.setStatus(404);
            return null;
        }

        return opSlotService.getFilteredOpSlots(Hospital.class, null, null, hospital, dateFrom, dateTo);
    }
}
