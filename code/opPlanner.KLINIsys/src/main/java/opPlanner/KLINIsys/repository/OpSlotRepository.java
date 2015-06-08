package opPlanner.KLINIsys.repository;

import opPlanner.KLINIsys.model.Doctor;
import opPlanner.KLINIsys.model.Hospital;
import opPlanner.KLINIsys.model.OpSlot;
import opPlanner.KLINIsys.model.Patient;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by Michael on 09.05.2015.
 */
@Repository
public interface OpSlotRepository extends CrudRepository<OpSlot, Long> {

    List<OpSlot> findByHospital_EMail(@Param("type") String eMail);

    @Query("select s from OpSlot s where (s.id in (:slotIds))")
    List<OpSlot> findByIdIn(@Param("slotIds") List<Long> slotIds);

    // used for op slots listing
    @Query("select s from OpSlot s where (:hospital is null or s.hospital = :hospital) and (:start is null or s.slotStart >= :start) and (:end is null or s.slotEnd <= :end)")
    List<OpSlot> findByHospitalAndTimeWindow(@Param("hospital") Hospital hospital, @Param("start") Date start, @Param("end") Date end);
}
