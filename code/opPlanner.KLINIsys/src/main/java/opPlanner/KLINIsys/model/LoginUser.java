package opPlanner.KLINIsys.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

/**
 * Created by Michael on 28.04.2015.
 */
@Inheritance(strategy = InheritanceType.JOINED)
@JsonIgnoreProperties({"new"})
public class LoginUser extends AbstractPersistable<Long> {

}
