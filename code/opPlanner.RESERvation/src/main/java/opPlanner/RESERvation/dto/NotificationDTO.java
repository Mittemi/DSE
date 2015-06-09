package opPlanner.RESERvation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.data.annotation.Id;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * Created by Thomas on 08.06.2015.
 */
public class NotificationDTO {

        private String eMail;

        private String message;

        private String subject;

        @JsonFormat(pattern = "yyyy-MM-dd")
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        private Date creationDate;

        public Date getCreationDate() {
            return creationDate;
        }

        public void setCreationDate(Date creationDate) {
            this.creationDate = creationDate;
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

        public NotificationDTO(String eMail, String message, String subject, Date creationDate) {
            this.eMail = eMail;
            this.message = message;
            this.subject = subject;
            this.creationDate = creationDate;
        }
}
