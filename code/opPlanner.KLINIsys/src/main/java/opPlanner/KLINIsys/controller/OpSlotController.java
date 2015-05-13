package opPlanner.KLINIsys.controller;

import opPlanner.KLINIsys.model.Hospital;
import opPlanner.KLINIsys.service.OpSlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Michael on 13.05.2015.
 */
@RestController
@RequestMapping("/opslot")
public class OpSlotController  {

    @Autowired
    private OpSlotService opSlotService;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json")
    public void deleteSlot(@PathVariable("id") Long id) {
        opSlotService.deleteSlot(id);
    }

}
