package opPlanner.NOTifier;

import opPlanner.NOTifier.controller.NotificationController;
import opPlanner.NOTifier.model.Notification;
import opPlanner.NOTifier.repository.NotificationRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Michael on 05.06.2015.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class NotificationTests {

    @Autowired
    private NotificationRepository repository;

    @Test
    public void testAutowire() {
        assertNotNull(repository);
    }

    @Test
    public void testRepository() {
        List<Notification> notifications = repository.findByEMailOrderByCreationDateDesc("NotFound");

        assertNotNull(notifications);
        assertEquals(0, notifications.size());

        int countNotifications = repository.findByEMailOrderByCreationDateDesc("u1@dse.at").size();

        Notification notification = new Notification();
        notification.seteMail("u1@dse.at");
        notification.setMessage("This is a demo notification for the unit test");
        notification.setSubject("Unit-Test");
        notification.setCreationDate(new Date());

        repository.save(notification);

        notifications = repository.findByEMailOrderByCreationDateDesc("u1@dse.at");
        assertEquals(countNotifications + 1, notifications.size());

        notification = new Notification();
        notification.seteMail("u1@dse.at");
        notification.setMessage("This is a demo notification for the unit test");
        notification.setSubject("Unit-Test");
        notification.setCreationDate(new Date());
        notification.setSubject("Unit-Test 2");

        repository.save(notification);

        notifications = repository.findByEMailOrderByCreationDateDesc("u1@dse.at");
        assertEquals(countNotifications + 2, notifications.size());

        assertTrue(notifications.get(0).getCreationDate().after(notifications.get(1).getCreationDate()));
    }

    @Autowired
    private NotificationController controller;

    @Test
    public void testController() {
        int countWithoutNewNotification = repository.findByEMailOrderByCreationDateDesc("u2@dse.at").size();

        Notification notification = new Notification();
        notification.seteMail("u2@dse.at");
        notification.setMessage("This is a demo notification for the unit test");
        notification.setSubject("Unit-Test");
        notification.setCreationDate(new Date());

        controller.create(notification);

        int countWithNewNotification = repository.findByEMailOrderByCreationDateDesc("u2@dse.at").size();

        assertTrue(countWithoutNewNotification < countWithNewNotification);

        List<Notification> notifications = controller.index("u2@dse.at");

        assertEquals(countWithNewNotification, notifications.size());

        controller.delete(notifications.get(0).getId(),"User has no permission to do delete this one");

        assertEquals(notifications.size(), controller.index("u2@dse.at").size());

        controller.delete(notifications.get(0).getId(),"u2@dse.at");

        assertEquals(notifications.size() - 1, controller.index("u2@dse.at").size());
    }
}
