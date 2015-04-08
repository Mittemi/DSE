package opPlanner.KLINIsys.model;

import org.springframework.data.jpa.domain.AbstractPersistable;
import org.springframework.stereotype.Component;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Created by Michael on 08.04.2015.
 */
@Entity
@Component
public class Hospital extends AbstractPersistable<Long> {

    @Column
    private String name;

    public Hospital(Long id) {
        this.setId(id);
    }

    public Hospital() {
        this(null);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
