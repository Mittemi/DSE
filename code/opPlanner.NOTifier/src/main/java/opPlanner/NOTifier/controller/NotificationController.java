package opPlanner.NOTifier.controller;

import opPlanner.NOTifier.model.Notification;
import opPlanner.NOTifier.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * Created by Michael on 05.06.2015.
 */
@RestController
public class NotificationController {

    @Autowired
    private NotificationRepository notificationRepository;

    /**
     *
     * @param email email of the user
     * @return a list of all notifications available for this email
     */
    @RequestMapping(value = "/list/{email}/", method = RequestMethod.GET, produces = "application/json")
    public List<Notification> index(@PathVariable String email) {
        return notificationRepository.findByEMailOrderByCreationDateDesc(email);
    }

    /**
     * deletes the notification if user is allowed to do so
     *
     * @param id id of the notification
     * @param email email of the user trying to delete it
     */
    @RequestMapping(value = "/delete/{email}/{id}", method = RequestMethod.DELETE, produces = "application/json")
    public void delete(@PathVariable String id, @PathVariable String email) {
        Notification notification = notificationRepository.findOne(id);
        if(notification != null && notification.geteMail().equals(email))
            notificationRepository.delete(notification);
    }

    /**
     *
     * @param notification the new notification which should be created, the date is replaced with the current one
     */
    @RequestMapping(value = "/create", method = RequestMethod.PUT, produces = "application/json")
    public void create(@RequestBody Notification notification) {
        notification.setCreationDate(new Date());
        notification.setId(null);

        notificationRepository.save(notification);
    }
}
