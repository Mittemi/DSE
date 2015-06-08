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

    //@Query("select s from OpSlot s where (:type is null or s.type = :type) and (:start is null or s.slotStart >= :start) and (:end is null or s.slotEnd <= :end)")
    //List<OpSlot> findByHospital(@Param("type") String type, @Param("start")@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date start, @Param("end")@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date end);

    List<OpSlot> findByHospital_EMail(@Param("type") String eMail);

    //@Query("select s from OpSlot s where (s.doctor.eMail = :email)")
    //List<OpSlot> findByDoctor(@Param("email")String eMail/*, @Param("from") Date dateFrom, @Param("to") Date dateTo*/);

    //@Query("select s from OpSlot s where (s.doctor.eMail = :email and s.slotStart >= :from and s.slotEnd <= :to)")
    //List<OpSlot> findByDoctorAndTimeWindow(@Param("email")String eMail, @Param("from") Date dateFrom, @Param("to") Date dateTo);

    @Query("select s from OpSlot s where (s.id in (:slotIds))")
    List<OpSlot> findByIdIn(@Param("slotIds") List<Long> slotIds);

    // used for op slots listing
    @Query("select s from OpSlot s where (:hospital is null or s.hospital = :hospital) and (:start is null or s.slotStart >= :start) and (:end is null or s.slotEnd <= :end)")
    List<OpSlot> findByHospitalAndTimeWindow(@Param("hospital") Hospital hospital, @Param("start") Date start, @Param("end") Date end);
}
