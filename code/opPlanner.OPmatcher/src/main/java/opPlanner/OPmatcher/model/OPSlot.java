package opPlanner.OPmatcher.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Thomas on 18.04.2015.
 */
@Document(collection = "freeOPSlots")
public class OPSlot {

    @Id
    private String id;

    //hospital email
    private String hospitalId;

    //Geo location info
    private double[] position;

    private Date start;
    private Date end;

    private String type;

    /**
     *
     * @param id
     * @param hospitalId
     * @param x
     * @param y
     * @param start
     * @param end
     * @param type
     */
    public OPSlot(String id, String hospitalId, double x, double y, Date start, Date end, String type) {
        this.id = id;
        this.hospitalId = hospitalId;
        this.start = start;
        this.end = end;
        this.position = new double[] {x,y};
        this.type = type;
    }

    /**
     *
     * @param hospitalId
     * @param x - longitude (laengengrad)
     * @param y - latitude (breitengrad)
     * @param start
     * @param end
     */
    public OPSlot(String hospitalId, double x, double y, Date start, Date end, String type) {
        this.hospitalId = hospitalId;
        this.start = start;
        this.end = end;
        this.position = new double[] {x,y};
        this.type = type;
    }

    /**
     *
     * @param hospitalId
     * @param position - double array with 2 values for x and y.
     * @param start
     * @param end
     * @param type
     */
    public OPSlot(String hospitalId, double[] position, Date start, Date end, String type) {
        this.hospitalId = hospitalId;
        this.start = start;
        this.end = end;
        this.position = position;
        this.type = type;
    }

    public OPSlot() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    /**
     * [0] = x - longitude
     * [1] = y - latitude
     * @return geo location of hospital
     */
    public double[] getPosition() {
        return position;
    }

    public void setPosition(double[] position) {
        this.position = position;
    }

    public String getType() {
        return type;
    }

    public double getX() {
        return position[0];
    }

    public double getY() {
        return position[1];
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return "OPSlot [id=" + id +", start=" + formatter.format(start) + ", end=" + formatter.format(end) + ", x=" +
                getX() + " y=" + getY()+"]";
    }

}
