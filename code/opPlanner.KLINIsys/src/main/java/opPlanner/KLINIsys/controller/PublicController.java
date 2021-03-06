package opPlanner.KLINIsys.controller;

import opPlanner.KLINIsys.dto.OpSlotListDTO;
import opPlanner.KLINIsys.service.OpSlotService;
import opPlanner.Shared.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * Created by Michael on 09.05.2015.
 */
@RestController
@RequestMapping("/public")
public class PublicController {

    @Autowired
    private OpSlotService opSlotService;

    @RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<? extends OpSlotListDTO> getOpSlots(@RequestParam(value = "from", required = false)  @DateTimeFormat(pattern = Constants.DATETIME_FORMAT_STRING_WITH_TZ) Date dateFrom,
                                                      @RequestParam(value = "to", required = false)  @DateTimeFormat(pattern = Constants.DATETIME_FORMAT_STRING_WITH_TZ)Date dateTo) {
        return opSlotService.getFilteredOpSlots(null, null, null, null, dateFrom, dateTo);
    }
}
