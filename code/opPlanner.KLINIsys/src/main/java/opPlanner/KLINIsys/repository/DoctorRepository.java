package opPlanner.KLINIsys.repository;

import opPlanner.KLINIsys.model.Doctor;
import opPlanner.KLINIsys.model.LoginUser;
import org.springframework.stereotype.Repository;

/**
 * Created by Michael on 28.04.2015.
 */
@Repository
public interface DoctorRepository extends LoginUserRepository<Doctor> {
}
