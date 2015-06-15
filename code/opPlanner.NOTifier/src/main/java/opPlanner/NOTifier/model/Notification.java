package opPlanner.NOTifier.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import opPlanner.Shared.Constants;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * Created by Michael on 05.06.2015.
 */
@Document(collection = "freeOPSlots")
public class Notification {

    @Id
    private String id;

    private String eMail;

    private String message;

    private String subject;

    @JsonFormat(pattern = Constants.DATETIME_FORMAT_STRING, timezone = Constants.TIME_ZONE)
    @DateTimeFormat(pattern = Constants.DATETIME_FORMAT_STRING)
    private Date creationDate;

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
