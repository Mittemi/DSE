package opPlanner.KLINIsys.repository;

import opPlanner.KLINIsys.model.Hospital;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Michael on 08.04.2015.
 */
@Repository
public interface HospitalRepository /*extends CrudRepository<Hospital, Long>*/ extends LoginUserRepository<Hospital> {

    List<Hospital> findByName(String name);

}
