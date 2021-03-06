package opPlanner.KLINIsys.repository;

import opPlanner.KLINIsys.model.OpSlot;
import opPlanner.KLINIsys.model.TimeWindow;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * Created by Thomas on 13.05.2015.
 */
public interface TimeWindowRepository extends CrudRepository<TimeWindow, Long> {

    @Query("select w from TimeWindow w where (w.doctor.eMail = :email and w.slotStart >= :dateFrom and w.slotEnd <= :dateTo)")
    List<TimeWindow> findByDoctorAndTimeWindow(@Param("email")String email,
                                           @Param("dateFrom")@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date dateFrom,
                                           @Param("dateTo")@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date dateTo);

    List<TimeWindow> findByDoctor_EMail(@Param("email")String email);

}
