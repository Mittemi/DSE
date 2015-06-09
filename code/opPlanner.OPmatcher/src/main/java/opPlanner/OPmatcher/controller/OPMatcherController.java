package opPlanner.OPmatcher.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import opPlanner.OPmatcher.OPPlannerProperties;
import opPlanner.OPmatcher.Service.OPMatcherService;
import opPlanner.OPmatcher.Service.OPMatcherServicesNotAvailableException;
import opPlanner.OPmatcher.dto.Patient;
import opPlanner.OPmatcher.dto.Reservation;
import opPlanner.OPmatcher.dto.TimeWindow;
import opPlanner.OPmatcher.model.OPSlot;
import opPlanner.OPmatcher.repository.OPSlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.geo.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.GeospatialIndex;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
import org.springframework.data.mongodb.core.query.NearQuery;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.hal.Jackson2HalModule;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.xml.ws.Response;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Thomas on 28.04.2015.
 */
@RestController
public class OPMatcherController {

    @Autowired
    OPMatcherService opMatcherService;

    public OPMatcherController() {

    }

    /**
     * Tries to find an op slot in a given time range and with a specified geo location
     * @param preferredPerimeter
     * @param preferredTimeWindow
     * @param opSlotType
     * @param doctorId
     * @param patientId
     * @return matched op slot, if no one was found null will be returned. Null will also be returned if patient or
     * opSlotType is null or simply if the work schedule of the doctor could not be retrieved.
     */
    @RequestMapping(value = "/findFreeSlot", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<OPSlot> findFreeSlot(Integer preferredPerimeter,
                                               TimeWindow preferredTimeWindow, String opSlotType,
                                               String doctorId, String patientId) {
        OPSlot chosenSlot = null;
        try {
            chosenSlot = opMatcherService.findFreeSlot(preferredPerimeter, preferredTimeWindow, opSlotType, doctorId, patientId);
        } catch (OPMatcherServicesNotAvailableException e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<OPSlot>(chosenSlot, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<OPSlot>(chosenSlot, HttpStatus.OK);
    }

    /**
     * Adds a free op slot
     *
     * @param opSlot - op slot which should be added to the list of free slots
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST, produces = "application/json")
    public void addFreeOPSlot(OPSlot opSlot) {
        opMatcherService.addFreeOPSlot(opSlot);
    }

    /**
     *
     * @param opSlotId
     * @return message
     */
    @RequestMapping(value = "/addOPSlotById/{opSlotId}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<String> addFreeOPSlotById(@PathVariable("opSlotId")String opSlotId) {
       OPSlot opSlot = opMatcherService.addFreeOPSlotById(opSlotId);
        if (opSlot == null) {
            return new ResponseEntity<String>("no op slot with id " + opSlotId + " could have been found/added.",
                    HttpStatus.NOT_FOUND);
        }
       return new ResponseEntity<String>(opSlot.toString(), HttpStatus.OK);
    }

    /**
     * Deletes an free op slot (e.g. after reservation was made)
     *
     * @param opSlotId - id of op slot to delete
     */
    @RequestMapping(value = "/delete/{opSlotId}", method = RequestMethod.DELETE, produces = "application/json")
    public ResponseEntity<String> deleteFreeOPSlot(@PathVariable("opSlotId") String opSlotId) {
        boolean result = opMatcherService.deleteFreeOPSlot(opSlotId);

        if(!result)
            return new ResponseEntity<String>("delete not possible", HttpStatus.INTERNAL_SERVER_ERROR);

        return new ResponseEntity<String>("slot deleted", HttpStatus.OK);
    }
}
