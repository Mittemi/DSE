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

    public Doctor() {

        opSlots = new ArrayList<>();
    }
}
