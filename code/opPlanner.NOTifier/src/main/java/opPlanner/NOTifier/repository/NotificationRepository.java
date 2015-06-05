package opPlanner.NOTifier.repository;

import opPlanner.NOTifier.model.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by Michael on 05.06.2015.
 */
public interface NotificationRepository extends MongoRepository<Notification, String> {

    List<Notification> findByEMailOrderByCreationDateDesc(String email);
}
