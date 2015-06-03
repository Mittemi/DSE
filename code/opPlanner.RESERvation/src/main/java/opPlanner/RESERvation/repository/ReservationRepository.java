package opPlanner.RESERvation.repository;

import opPlanner.RESERvation.model.Reservation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Created by Thomas on 10.05.2015.
 */
public interface ReservationRepository extends MongoRepository<Reservation, String> {

    List<Reservation> findByOpSlotIdIn(Collection<String> opSlotIds);

    List<Reservation> findByDoctorId(String doctorId);

    @Query("{ doctorId: ?0, start: { $gt: ?1 }, end: { $lt: ?2 }}")
    List<Reservation> findByDoctorAndTimeWindow(@Param("doctorId")String doctorId,
                                               @Param("start")@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date
                                                       dateFrom,
                                               @Param("end")@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date dateTo);

    List<Reservation> deleteReservationByOpSlotId(String opSlotId);
}
