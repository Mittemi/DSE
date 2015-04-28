package opPlanner.KLINIsys.repository;

import opPlanner.KLINIsys.model.Hospital;
import opPlanner.KLINIsys.model.LoginUser;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.stereotype.Repository;

/**
 * Created by Michael on 28.04.2015.
 */
@Repository
public interface LoginUserRepository<T extends LoginUser> extends CrudRepository<T, Long> {

    @Query("select u from #{#entityName} as u where u.eMail = ?1")
    LoginUser findByEmail(String eMail);
}
