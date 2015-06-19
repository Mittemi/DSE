package opPlanner.OPmatcher;

import opPlanner.OPmatcher.Service.DataInitializerService;
import opPlanner.OPmatcher.Service.OPMatcherService;
import opPlanner.OPmatcher.controller.OPMatcherController;
import opPlanner.OPmatcher.dto.Reservation;
import opPlanner.OPmatcher.dto.TimeWindow;
import opPlanner.OPmatcher.model.OPSlot;
import opPlanner.OPmatcher.repository.OPSlotRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebIntegrationTest({"server.port=0", "management.port=0"})
public class OPMatcherTest {

	@Autowired
	OPSlotRepository repo;

	@Autowired
	OPMatcherService opMatcherService;

	@Autowired
	OPMatcherController opMatcher;

	@Autowired
	DataInitializerService dataInitializerService;

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
		repo.deleteAll();
		//ignore further data retrieving from KLINISys and Reservation during the data initialization process
		dataInitializerService.setInitialized(true);
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

		OPSlot slotAKHVienna = new OPSlot("kh1@dse.at", AKHWIEN.getX(), AKHWIEN.getY(), from1.getTime(), to1.getTime(),
				"eye")
				;	//akh wien
		repo.save(slotAKHVienna);
		OPSlot slotAKHLinz = new OPSlot("kh2@dse.at", AKHLINZ.getX(), AKHLINZ.getY(), from2.getTime(), to2.getTime(), "neuro");	//akh linz
		repo.save(slotAKHLinz);
		OPSlot slotLKHGraz = new OPSlot("kh3@dse.at", LKHGRAZ.getX(), LKHGRAZ.getY() , from3.getTime(), to3.getTime()
				, "eye");		//lkh graz
		repo.save(slotLKHGraz);
	}

	@After
	public void cleanTestData() {
		repo.deleteAll();
	}

	@Test
	public void testFindAll() {
		GeoResults<OPSlot> slots = opMatcherService.findFreeSlotList(TUWIEN.getX(),TUWIEN.getY(),500, null, null,
				null, null) ;
		assertEquals(3, slots.getContent().size());
	}

	@Test
	 public void testFindAllEyeSlots() {
		GeoResults<OPSlot> slots = opMatcherService.findFreeSlotList(TUWIEN.getX(),TUWIEN.getY(),500, null, "eye",
				null, null) ;
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
		GeoResults<OPSlot> slots = opMatcherService.findFreeSlotList(TUWIEN.getX(), TUWIEN.getY(), 20, null, null,
				null, null);
		List<OPSlot> slots2 = repo.findByPositionNear(TUWIEN, new Distance(20, Metrics.KILOMETERS));

		assertTrue(slots.getContent().size() == slots2.size());

		assertTrue(AKHWIEN.equals(new Point(slots.getContent().get(0).getContent().getX(),slots.getContent().get(0).getContent().getY())));
		assertTrue(AKHWIEN.equals(new Point(slots2.get(0).getX(),slots2.get(0).getY())));

		slots = opMatcherService.findFreeSlotList(SCHLADMING.getX(), SCHLADMING.getY(), 300,null, null, null, null);
		slots2 = repo.findByPositionNear(SCHLADMING, new Distance(300, Metrics.KILOMETERS));

		assertTrue(slots.getContent().size() == slots2.size());
	}

	@Test
	public void testFindAnyNearOPSlot() {
		GeoResults<OPSlot> slots = opMatcherService.findFreeSlotList(TUWIEN.getX(), TUWIEN.getY(), 20, null,  null, null, null);
		assertEquals(1, slots.getContent().size());
	}

	@Test
	public void testFindNearestNeuroOPSlot() {
		GeoResults<OPSlot> slots = opMatcherService.findFreeSlotList(TUWIEN.getX(), TUWIEN.getY(), 20, null, "neuro", null, null);
		assertEquals(0, slots.getContent().size());

		slots = opMatcherService.findFreeSlotList(TUWIEN.getX(), TUWIEN.getY(), 300, null, "neuro", null, null);
		assertEquals(1, slots.getContent().size());
	}

	@Test
	public void testFindDoctorSuitableOpSlots() {
		List<TimeWindow> slots = new LinkedList<>();

		GregorianCalendar from4 = new GregorianCalendar();
		from4.set(2015, 05, 25, 10, 00);
		GregorianCalendar to4 = new GregorianCalendar();
		to4.set(2015, 05, 25, 14, 00);

		TimeWindow slot1 = new TimeWindow(from1.getTime(), to1.getTime());
		TimeWindow slot2 = new TimeWindow(from4.getTime(), to4.getTime());

		slots.add(slot1);
		slots.add(slot2);

		GeoResults<OPSlot> resultSlots = opMatcherService.findFreeSlotList(TUWIEN.getX(), TUWIEN.getY(), 500, null, "eye", slots, null);
		assertEquals(2, resultSlots.getContent().size());

		//assume that the doctor has already a reservation on one possible op slot
		Reservation reservation = new Reservation("1234", from1.getTime(), to1.getTime(), "anyPatient@dse.at", "d1@dse.at");
		List<Reservation> reservations = new LinkedList<>();
		reservations.add(reservation);
		resultSlots = opMatcherService.findFreeSlotList(TUWIEN.getX(), TUWIEN.getY(), 500, null, "eye", slots, reservations);
		assertEquals(1, resultSlots.getContent().size());

		//add a time window --> s.t. op slot at 25.5 at LKHGraz cancels out.
		TimeWindow preferredTimeWindow = new TimeWindow(from1.getTime(), to2.getTime());
		resultSlots = opMatcherService.findFreeSlotList(TUWIEN.getX(), TUWIEN.getY(), 500, preferredTimeWindow, "eye", slots, null);
		assertEquals(1, resultSlots.getContent().size());

		resultSlots = opMatcherService.findFreeSlotList(TUWIEN.getX(), TUWIEN.getY(), 20, null, "eye", slots, null);
		assertEquals(1, resultSlots.getContent().size());

		resultSlots = opMatcherService.findFreeSlotList(TUWIEN.getX(), TUWIEN.getY(), 500, null, "neuro", slots, null);
		assertEquals(0, resultSlots.getContent().size());

		slots.remove(slot1);
		resultSlots = opMatcherService.findFreeSlotList(TUWIEN.getX(), TUWIEN.getY(), 500, null, "eye", slots, null);
		assertEquals(1, resultSlots.getContent().size());
		assertTrue(resultSlots.getContent().get(0).getContent().getX() == LKHGRAZ.getX()
				&& resultSlots.getContent().get(0).getContent().getY() == LKHGRAZ.getY());
	}

	@Test
	public void testFindOPSlot() {
		//TODO
	}

}
