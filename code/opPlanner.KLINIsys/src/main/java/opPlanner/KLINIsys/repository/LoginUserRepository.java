package opPlanner.KLINIsys.repository;

import opPlanner.KLINIsys.model.Hospital;
import opPlanner.KLINIsys.model.LoginUser;
import opPlanner.KLINIsys.model.Patient;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * Created by Michael on 28.04.2015.
 */
@Repository
public interface LoginUserRepository<T extends LoginUser> extends CrudRepository<T, Long> {

    @Query("select u from #{#entityName} as u where u.eMail = :email")
    T findByEmail(@Param("email")String eMail);

    @Query("select u from #{#entityName} as u where u.eMail in (:emails)")
    List<T> findByeMailIn(@Param("emails")List<String> emails);
}
