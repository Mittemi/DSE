package opPlanner.OPmatcher;

import opPlanner.OPmatcher.controller.OPMatcherController;
import opPlanner.OPmatcher.model.OPSlot;
import opPlanner.OPmatcher.repository.OPSlotRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.index.GeospatialIndex;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.GregorianCalendar;
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

	@Before
	public void initTestData() {
		GregorianCalendar from1 = new GregorianCalendar();
		from1.set(2015, 05, 23, 11, 00);
		GregorianCalendar from2 = new GregorianCalendar();
		from2.set(2015, 05, 24, 11, 00);
		GregorianCalendar from3 = new GregorianCalendar();
		from3.set(2015, 05, 25, 11, 00);
		GregorianCalendar to1 = new GregorianCalendar();
		to1.set(2015, 05, 23, 12, 00);
		GregorianCalendar to2 = new GregorianCalendar();
		to2.set(2015, 05, 24, 12, 00);
		GregorianCalendar to3 = new GregorianCalendar();
		to3.set(2015, 05, 25, 12, 00);

		OPSlot slotAKHVienna = new OPSlot(1, AKHWIEN.getX(), AKHWIEN.getY(), from1.getTime(), to1.getTime(), "eye");	//akh wien
		repo.save(slotAKHVienna);
		OPSlot slotAKHLinz = new OPSlot(2, AKHLINZ.getX(), AKHLINZ.getY(), from2.getTime(), to2.getTime(), "eye");	//akh linz
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
		List<OPSlot> slots = opMatcher.findFreeSlotList(TUWIEN.getX(),TUWIEN.getY(),500, "eye", null) ;
		assertEquals(3, slots.size());
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
		assertTrue(true);
	}

}
