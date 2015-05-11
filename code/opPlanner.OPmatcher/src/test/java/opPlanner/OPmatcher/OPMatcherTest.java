package opPlanner.OPmatcher;

import opPlanner.OPmatcher.controller.OPMatcherController;
import opPlanner.OPmatcher.dto.TimeSlot;
import opPlanner.OPmatcher.model.OPSlot;
import opPlanner.OPmatcher.repository.OPSlotRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.index.GeospatialIndex;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class OPMatcherTest {

	@Autowired
	OPSlotRepository repo;

	@Autowired
	OPMatcherController opMatcher;

	private static final Point AKHWIEN = new Point(48.220589, 16.346794);
	private static final Point AKHLINZ = new Point(48.302743, 14.305328);
	private static final Point LKHGRAZ = new Point(47.081800, 15.466207);
	private static final Point TUWIEN = new Point(48.198787, 16.367873);
	private static final Point SCHLADMING = new Point(47.397252, 13.673470);

	GregorianCalendar from1;
	GregorianCalendar from2;
	GregorianCalendar from3;
	GregorianCalendar to1;
	GregorianCalendar to2;
	GregorianCalendar to3;

	@Before
	public void initTestData() {
		from1 = new GregorianCalendar();
		from1.set(2015, 05, 23, 11, 00);
		from2 = new GregorianCalendar();
		from2.set(2015, 05, 24, 11, 00);
		from3 = new GregorianCalendar();
		from3.set(2015, 05, 25, 11, 00);
		to1 = new GregorianCalendar();
		to1.set(2015, 05, 23, 12, 00);
		to2 = new GregorianCalendar();
		to2.set(2015, 05, 24, 12, 00);
		to3 = new GregorianCalendar();
		to3.set(2015, 05, 25, 12, 00);

		OPSlot slotAKHVienna = new OPSlot(1, AKHWIEN.getX(), AKHWIEN.getY(), from1.getTime(), to1.getTime(), "eye");	//akh wien
		repo.save(slotAKHVienna);
		OPSlot slotAKHLinz = new OPSlot(2, AKHLINZ.getX(), AKHLINZ.getY(), from2.getTime(), to2.getTime(), "neuro");	//akh linz
		repo.save(slotAKHLinz);
		OPSlot slotLKHGraz = new OPSlot(3, LKHGRAZ.getX(), LKHGRAZ.getY() , from3.getTime(), to3.getTime(), "eye");		//lkh graz
		repo.save(slotLKHGraz);
	}

	@After
	public void cleanTestData() {
		repo.deleteAll();
	}

	@Test
	public void testFindAll() {
		GeoResults<OPSlot> slots = opMatcher.findFreeSlotList(TUWIEN.getX(),TUWIEN.getY(),500, null, null) ;
		assertEquals(3, slots.getContent().size());
	}

	@Test
	 public void testFindAllEyeSlots() {
		GeoResults<OPSlot> slots = opMatcher.findFreeSlotList(TUWIEN.getX(),TUWIEN.getY(),500, "eye", null) ;
		assertEquals(2, slots.getContent().size());
	}

	@Test
	public void testFindNearest() {
		List<OPSlot> slots = repo.findByPositionNear(TUWIEN, new Distance(20, Metrics.KILOMETERS));

		assertTrue(slots.size() == 1);

		assertTrue(AKHWIEN.equals(new Point(slots.get(0).getX(),slots.get(0).getY())));

		slots = repo.findByPositionNear(SCHLADMING, new Distance(300, Metrics.KILOMETERS));

		assertTrue(slots.size() == 2);
	}



	@Test
	public void testFindNearestWithController() {
		GeoResults<OPSlot> slots = opMatcher.findFreeSlotList(TUWIEN.getX(), TUWIEN.getY(), 20, null, null);
		List<OPSlot> slots2 = repo.findByPositionNear(TUWIEN, new Distance(20, Metrics.KILOMETERS));

		assertTrue(slots.getContent().size() == slots2.size());

		assertTrue(AKHWIEN.equals(new Point(slots.getContent().get(0).getContent().getX(),slots.getContent().get(0).getContent().getY())));
		assertTrue(AKHWIEN.equals(new Point(slots2.get(0).getX(),slots2.get(0).getY())));

		slots = opMatcher.findFreeSlotList(SCHLADMING.getX(), SCHLADMING.getY(), 300, null, null);
		slots2 = repo.findByPositionNear(SCHLADMING, new Distance(300, Metrics.KILOMETERS));

		assertTrue(slots.getContent().size() == slots2.size());
	}

	@Test
	public void testFindAnyNearOPSlot() {
		GeoResults<OPSlot> slots = opMatcher.findFreeSlotList(TUWIEN.getX(), TUWIEN.getY(), 20, null, null);
		assertEquals(1, slots.getContent().size());
	}

	@Test
	public void testFindNearestNeuroOPSlot() {
		GeoResults<OPSlot> slots = opMatcher.findFreeSlotList(TUWIEN.getX(), TUWIEN.getY(), 20, "neuro", null);
		assertEquals(0, slots.getContent().size());

		slots = opMatcher.findFreeSlotList(TUWIEN.getX(), TUWIEN.getY(), 300, "neuro", null);
		assertEquals(1, slots.getContent().size());
	}

	@Test
	public void testFindDoctorSuitableSlots() {
		List<TimeSlot> slots = new LinkedList<>();

		GregorianCalendar from4 = new GregorianCalendar();
		from4.set(2015, 05, 25, 10, 00);
		GregorianCalendar to4 = new GregorianCalendar();
		to4.set(2015, 05, 25, 14, 00);

		TimeSlot slot1 = new TimeSlot(from1.getTime(), to1.getTime());
		TimeSlot slot2 = new TimeSlot(from4.getTime(), to4.getTime());

		slots.add(slot1);
		slots.add(slot2);

		GeoResults<OPSlot> resultSlots = opMatcher.findFreeSlotList(TUWIEN.getX(), TUWIEN.getY(), 500, "eye", slots);
		assertEquals(2, resultSlots.getContent().size());

		resultSlots = opMatcher.findFreeSlotList(TUWIEN.getX(), TUWIEN.getY(), 20, "eye", slots);
		assertEquals(1, resultSlots.getContent().size());

		resultSlots = opMatcher.findFreeSlotList(TUWIEN.getX(), TUWIEN.getY(), 500, "neuro", slots);
		assertEquals(0, resultSlots.getContent().size());

		slots.remove(slot1);
		resultSlots = opMatcher.findFreeSlotList(TUWIEN.getX(), TUWIEN.getY(), 500, "eye", slots);
		assertEquals(1, resultSlots.getContent().size());
		assertTrue(resultSlots.getContent().get(0).getContent().getX() == LKHGRAZ.getX()
				&& resultSlots.getContent().get(0).getContent().getY() == LKHGRAZ.getY());
	}

}
