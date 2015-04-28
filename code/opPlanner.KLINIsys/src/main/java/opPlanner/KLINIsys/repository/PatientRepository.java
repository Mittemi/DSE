package opPlanner.KLINIsys.repository;

import opPlanner.KLINIsys.model.Hospital;
import opPlanner.KLINIsys.model.Patient;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Michael on 28.04.2015.
 */
@Repository
public interface PatientRepository extends CrudRepository<Patient, Long> {

    List<Patient> findByName(String name);
}
