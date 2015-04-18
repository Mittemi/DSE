package opPlanner.KLINIsys.controller;

import opPlanner.KLINIsys.model.Hospital;
import opPlanner.KLINIsys.repository.HospitalRepository;
import opPlanner.KLINIsys.service.HospitalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by Michael on 18.04.2015.
 */
@RestController
public class HospitalController {

    @Autowired
    private HospitalService service;

    @RequestMapping(value = "/", method = RequestMethod.GET, produces = "application/json")
    public Iterable<Hospital> index() {
        return service.allHospitals();
    }


}
