package opPlanner.RESERvation.repository;

import opPlanner.RESERvation.model.Reservation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Collection;
import java.util.List;

/**
 * Created by Thomas on 10.05.2015.
 */
public interface ReservationRepository extends MongoRepository<Reservation, String> {

    List<Reservation> findByOpSlotIdIn(Collection<String> opSlotIds);

    List<Reservation> findByDoctorId(String doctorId);
}
