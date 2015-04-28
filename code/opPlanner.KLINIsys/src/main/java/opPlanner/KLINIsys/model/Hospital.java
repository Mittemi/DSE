package opPlanner.KLINIsys.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.jpa.domain.AbstractPersistable;
import org.springframework.stereotype.Component;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Created by Michael on 08.04.2015.
 */
@Entity
@Component
public class Hospital extends LoginUser {

    @Column
    private String name;

    @Column
    private String shortName;

    @Column
    private String address;

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

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
