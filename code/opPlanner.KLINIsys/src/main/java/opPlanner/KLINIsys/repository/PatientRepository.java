package opPlanner.KLINIsys.repository;

import opPlanner.KLINIsys.model.Hospital;
import opPlanner.KLINIsys.model.Patient;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Michael on 28.04.2015.
 */
@Repository
@RepositoryRestResource
public interface PatientRepository extends LoginUserRepository<Patient> {

    List<Patient> findByName(@Param("name")String name);

}
