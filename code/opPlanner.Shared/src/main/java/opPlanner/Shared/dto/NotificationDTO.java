package opPlanner.Shared.dto;

/**
 * Created by Michael on 05.06.2015.
 */
public class NotificationDTO {

    private String eMail;

    private String message;

    private String subject;

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
