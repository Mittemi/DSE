package opPlanner.OPmatcher.repository;

import opPlanner.OPmatcher.model.OPSlot;
import org.springframework.data.geo.Box;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by Thomas on 28.04.2015.
 */
public interface OPSlotRepository extends MongoRepository<OPSlot, String> {

        List<OPSlot> findByPositionWithin(Circle c);

        List<OPSlot> findByPositionWithin(Box b);

        List<OPSlot> findByPositionNear(Point p, Distance d);
}