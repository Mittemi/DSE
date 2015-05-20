package opPlanner.KLINIsys.repository;

import opPlanner.KLINIsys.model.Doctor;
import opPlanner.KLINIsys.model.Hospital;
import opPlanner.KLINIsys.model.OpSlot;
import opPlanner.KLINIsys.model.Patient;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by Michael on 09.05.2015.
 */
@Repository
public interface OpSlotRepository extends CrudRepository<OpSlot, Long> {

    //TODO
    @Query("select s from OpSlot s where (:type is null or s.type = :type) and (:start is null or s.slotStart >= :start) and (:end is null or s.slotEnd <= :end)")
    List<OpSlot> findByHospital(@Param("type") String type, @Param("start") Date start, @Param("end") Date end);


    @Query("select s from OpSlot s where (s.doctor.eMail = :email)")
    List<OpSlot> findByDoctor(@Param("email")String eMail/*, @Param("from") Date dateFrom, @Param("to") Date dateTo*/);

    @Query("select s from OpSlot s where (s.doctor.eMail = :email and s.slotStart >= :from and s.slotEnd <= :to)")
    List<OpSlot> findByDoctorAndTimeWindow(@Param("email")String eMail, @Param("from") Date dateFrom, @Param("to") Date dateTo);

    // used for op slots listing
    @Query("select s from OpSlot s where (:doctor is null or s.doctor = :doctor) and (:hospital is null or s.hospital = :hospital) and (:start is null or s.slotStart >= :start) and (:end is null or s.slotEnd <= :end)")
    List<OpSlot> findByHospitalDoctorAndTimeWindow(@Param("doctor") Doctor doctor, @Param("hospital") Hospital hospital, @Param("start") Date start, @Param("end") Date end);
}
