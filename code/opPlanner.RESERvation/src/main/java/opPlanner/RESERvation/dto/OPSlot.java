package opPlanner.RESERvation.dto;

import java.util.Date;

/**
 * Created by Thomas on 14.05.2015.
 */
public class OPSlot {
    private String id;

    private long hospitalId;

    //Geo location info
    private double[] position;

    private Date start;
    private Date end;

    private String type;

    public OPSlot() {

    }
}
