package opPlanner.KLINIsys.dto;

import java.util.Date;

/**
 * Created by Thomas on 03.06.2015.
 */
public class OPSlotDTO {
    private String id;

    private long hospitalId;

    //Geo location info
    private double[] position;

    private Date start;
    private Date end;

    private String type;

    /**
     *
     * @param hospitalId
     * @param x - longitude (längengrad)


     * @param y - latitude (breitengrad)
     * @param start
     * @param end
     */
    public OPSlotDTO(Long id, Long hospitalId, double x, double y, Date start, Date end, String type) {
        this.id = id.toString();
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
    public OPSlotDTO(Long id, Long hospitalId, double[] position, Date start, Date end, String type) {
        this.id = id.toString();
        this.hospitalId = hospitalId;
        this.start = start;
        this.end = end;
        this.position = position;
        this.type = type;
    }

    public OPSlotDTO() {

    }


    public Long getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(Long hospitalId) {
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

    public String getId() {
        return id;
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
}
