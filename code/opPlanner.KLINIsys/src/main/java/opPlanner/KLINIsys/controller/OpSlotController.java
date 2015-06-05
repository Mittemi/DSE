package opPlanner.KLINIsys.controller;

import opPlanner.KLINIsys.dto.OpSlotViewModel;
import opPlanner.KLINIsys.model.Hospital;
import opPlanner.KLINIsys.model.OpSlot;
import opPlanner.KLINIsys.service.HospitalService;
import opPlanner.KLINIsys.service.OpSlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * Created by Michael on 13.05.2015.
 */
@RestController
@RequestMapping("/opslot")
public class OpSlotController  {

    @Autowired
    private OpSlotService opSlotService;

    @Autowired
    private HospitalService hospitalService;

    @RequestMapping(value = "/{email}/{id}", method = RequestMethod.DELETE, produces = "application/json")
    public void deleteSlot(@PathVariable("email") String email, @PathVariable("id") Long id, HttpServletResponse response) {
        boolean result = opSlotService.deleteSlot(email, id);

        if (!result) {
            response.setStatus(500);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json")
    public OpSlotViewModel getSlotById(@PathVariable("id") Long id) {
        OpSlotViewModel opSlotViewModel = new OpSlotViewModel(opSlotService.getOPSlotById(id));
        return opSlotViewModel;
    }

    @RequestMapping(value="create/{email}/", method = RequestMethod.PUT, produces = "application/json")
    public void createSlot(@PathVariable(value = "email") String hospitalMail, @RequestBody OpSlot opSlot) {
        System.out.println("Create OPSlot: " + hospitalMail);

        Hospital hospital = hospitalService.findByEmail(hospitalMail);
        opSlotService.createSlot(hospital, opSlot);

    }

}
