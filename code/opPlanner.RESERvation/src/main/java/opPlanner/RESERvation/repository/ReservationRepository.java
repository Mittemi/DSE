package opPlanner.RESERvation.repository;

import opPlanner.RESERvation.model.Reservation;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by Thomas on 10.05.2015.
 */
public interface ReservationRepository extends MongoRepository<Reservation, String> {
}
