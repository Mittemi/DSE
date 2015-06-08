package opPlanner.KLINIsys.controller;

import opPlanner.KLINIsys.dto.OPSlotDTO;
import opPlanner.KLINIsys.model.OpSlot;
import opPlanner.KLINIsys.model.Hospital;
import opPlanner.KLINIsys.service.HospitalService;
import opPlanner.KLINIsys.service.OpSlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.LinkedList;
import java.util.List;

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
    public OPSlotDTO getSlotById(@PathVariable("id") Long id) {
        OpSlot opSlot = opSlotService.getOPSlotById((id));
        if (opSlot == null) {
            return null;
        }
        OPSlotDTO opSlotDTO = new OPSlotDTO(opSlot.getId(),opSlot.getHospital().getId(), opSlot.getHospital().getX(), opSlot.getHospital().getY(), opSlot.getSlotStart(), opSlot.getSlotEnd(), opSlot.getType());
        return opSlotDTO;
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET, produces = "application/json")
    public List<OPSlotDTO> getAllSlots() {
        Iterable<OpSlot> opSlotList = opSlotService.allOpSlots();
        List<OPSlotDTO> opSlotDTOList = new LinkedList<OPSlotDTO>();
        OPSlotDTO opSlotDTO;
        for (OpSlot opSlot : opSlotList) {
            opSlotDTO = new OPSlotDTO(opSlot.getId(),opSlot.getHospital().getId(), opSlot.getHospital().getX(), opSlot.getHospital().getY(), opSlot.getSlotStart(), opSlot.getSlotEnd(), opSlot.getType());
            opSlotDTOList.add(opSlotDTO);
        }
       return opSlotDTOList;
    }

    @RequestMapping(value="create/{email}/", method = RequestMethod.PUT, produces = "application/json")
    public void createSlot(@PathVariable(value = "email") String hospitalMail, @RequestBody OpSlot opSlot) {
        System.out.println("Create OPSlot: " + hospitalMail);

        Hospital hospital = hospitalService.findByEmail(hospitalMail);
        opSlotService.createSlot(hospital, opSlot);

    }

}
