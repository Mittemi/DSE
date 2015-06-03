package opPlanner.KLINIsys.controller;

import opPlanner.KLINIsys.dto.OPSlotDTO;
import opPlanner.KLINIsys.dto.OpSlotViewModel;
import opPlanner.KLINIsys.model.OpSlot;
import opPlanner.KLINIsys.service.OpSlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by Michael on 13.05.2015.
 */
@RestController
@RequestMapping("/opslot")
public class OpSlotController  {

    @Autowired
    private OpSlotService opSlotService;

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "application/json")
    public void deleteSlot(@PathVariable("id") Long id, HttpServletResponse response) {
        boolean result = opSlotService.deleteSlot(id);

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

}
