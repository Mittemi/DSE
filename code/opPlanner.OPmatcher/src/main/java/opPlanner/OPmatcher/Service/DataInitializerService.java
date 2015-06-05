package opPlanner.OPmatcher.Service;

import opPlanner.OPmatcher.OPPlannerProperties;
import opPlanner.OPmatcher.dto.Reservation;
import opPlanner.OPmatcher.model.OPSlot;
import opPlanner.OPmatcher.repository.OPSlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * scope = singleton (default scope of @Service is singleton anyway)
 * Created by Thomas on 05.06.2015.
 */
@Service
public class DataInitializerService {

    @Autowired
    private OPSlotRepository repo;

    @Autowired
    private OPPlannerProperties config;

    private RestTemplate restClient;

    //indicates whether data is already loaded to the op matcher
    private Boolean initialized;

    public DataInitializerService() {
        initialized = false;
        this.restClient = new RestTemplate();
    }


    @PostConstruct
    private void initOPSlots() {
        loadData();
    }

    /**
     * indicates whether the data has already been loaded from KLINISys and RESERvation
     * @return
     */
    public boolean isInitialized() {
        synchronized (initialized) {
            return initialized;
        }
    }

    /**
     * does a database clean up and loads the data from KLINISys and Reservation, afterwards the op slots are filtered
     * and inserted in the op matcher database
     */
    protected void loadData() {
        //synchronize to "initialized" in order to let other threads in the waiting state when the data is currently
        // loaded
        synchronized (initialized) {
            if (initialized) {
                return;
            }
            try {
                //remove all data entries
                repo.deleteAll();

                //pull op slot data
                String klinisysUrl = "http://" + config.getKlinisys().getIpOrHostname()
                        + ":" + config.getKlinisys().getPort()
                        + "/" + config.getKlinisys().getOpSlotListUrl();
                OPSlot[] opSlots = restClient.getForObject(klinisysUrl, OPSlot[].class);

                //pull reservation data
                String reservationUrl = "http://" + config.getReservation().getIpOrHostname()
                        + ":" + config.getReservation().getPort()
                        + "/" + config.getReservation().getFindAllReservationsUrl();
                Reservation[] reservations = restClient.getForObject(reservationUrl, Reservation[].class);

                //apply the filtering (all op slots - reserved op slots)
                List<OPSlot> filteredOPSlotList  = Arrays.asList(opSlots)
                        .stream()
                        .filter((s) -> isOPSlotNotIn(s, reservations))
                        .collect(Collectors.toList());

                //save the filtered list
                repo.save(filteredOPSlotList);
                this.initialized = true;
            } catch (ResourceAccessException e) {
                System.out.println("Data could not have been retrieved from KLINIS and/or RESERvation");
            }
        }
    }

    private boolean isOPSlotNotIn(OPSlot opSlot, Reservation[] reservation) {
        boolean isIn = Arrays.asList(reservation).stream().anyMatch((r) -> r.getOpSlotId().equals(opSlot.getId()));
        return !isIn;
    }
}
