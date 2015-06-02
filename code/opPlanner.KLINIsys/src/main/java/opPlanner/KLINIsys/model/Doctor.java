package opPlanner.KLINIsys.model;

import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michael on 28.04.2015.
 */
@Entity
@Component
public class Doctor extends LoginUser {

    private String name;
    
    @OneToMany(mappedBy = "doctor", targetEntity = TimeWindow.class)
    private List<TimeWindow> workSchedule;

    public List<TimeWindow> getWorkSchedule() {
        return workSchedule;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setWorkSchedule(List<TimeWindow> workSchedule) {
        this.workSchedule = workSchedule;
    }

    public Doctor() {
        workSchedule = new ArrayList<>();
    }
}
