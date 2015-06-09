package opPlanner.OPmatcher.controller;

import opPlanner.OPmatcher.config.SpringMongoConfig;
import opPlanner.OPmatcher.model.OPSlot;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by Thomas on 18.04.2015.
 */
public class OPSlotTest {

    //Test Spring Data Mongo DB
    public static void main(String[] args) {

        // For XML
        //ApplicationContext ctx = new GenericXmlApplicationContext("SpringConfig.xml");

        // For Annotation
        ApplicationContext ctx =
                new AnnotationConfigApplicationContext(SpringMongoConfig.class);
        MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");

        OPSlot slot = new OPSlot("kh0@dse.at", -0.5, 2.5, new Date(),new Date(), "eye");
        // save
        mongoOperation.save(slot);

        // now slot object got the created id.
        System.out.println("1. slot : " + slot.getHospitalId());

        // query to search slot
        Query searchSlotQuery = new Query(Criteria.where("hospitalId").is(1));

        // find the saved slot again.
        OPSlot savedSlot = mongoOperation.findOne(searchSlotQuery, OPSlot.class);
        System.out.println("2. find - savedSlot : " + savedSlot.getHospitalId());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // update end date
        mongoOperation.updateFirst(searchSlotQuery,
                Update.update("start", new Date()),OPSlot.class);

        // find the updated user object
        OPSlot updatedSlot = mongoOperation.findOne(searchSlotQuery, OPSlot.class);

        System.out.println("3. updatedOPSlot : " + updatedSlot.getHospitalId());

        // delete
        mongoOperation.remove(searchSlotQuery, OPSlot.class);

        // List, it should be empty now.
        List<OPSlot> listSlots = mongoOperation.findAll(OPSlot.class);
        System.out.println("4. Number of slot = " + listSlots.size());

    }
}
