package opPlanner.KLINIsys.model;

import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michael on 28.04.2015.
 */
@Entity
@Component
public class Doctor extends LoginUser {

    @OneToMany(mappedBy = "doctor", targetEntity = OpSlot.class)
    private List<OpSlot> opSlots;

    private String name;

    public List<OpSlot> getOpSlots() {
        return opSlots;
    }

    public void setOpSlots(List<OpSlot> opSlots) {
        this.opSlots = opSlots;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Doctor() {

        opSlots = new ArrayList<>();
    }
}
