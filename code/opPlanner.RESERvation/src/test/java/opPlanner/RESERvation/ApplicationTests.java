package opPlanner.RESERvation;

import opPlanner.RESERvation.controller.ReservationController;
import opPlanner.RESERvation.model.Reservation;
import opPlanner.RESERvation.repository.ReservationRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class ApplicationTests {

	@Autowired
	private ReservationController reservationController;

	@Autowired
	private ReservationRepository repo;

	private GregorianCalendar from1;
	private GregorianCalendar from2;
	private GregorianCalendar from3;
	private GregorianCalendar to1;
	private GregorianCalendar to2;
	private GregorianCalendar to3;

	private String doctorId1 = "d1@dse.at";
	private String doctorId2 = "d2@dse.at";
	private String patientId1 = "p1@dse.at";
	private String patientId2 = "p2@dse.at";

	@Before
	public void initTestData() {
		repo.deleteAll();

		//IMPORTANT: Assumes that the test data in KLINISys is in place (Demo Data) and in OPMatcher is NOT!
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

		Reservation r1 = new Reservation("1", from1.getTime(), to1.getTime(), patientId1, doctorId1);
		Reservation r2 = new Reservation("2", from2.getTime(), to2.getTime(), patientId1, doctorId1);

		Reservation r3 = new Reservation("3", from3.getTime(), to3.getTime(), patientId2, doctorId1);
		Reservation r4 = new Reservation("4", from1.getTime(), to1.getTime(), patientId2, doctorId2);

		repo.save(r1);
		repo.save(r2);
		repo.save(r3);
		repo.save(r4);
	}

	@After
	public void deleteTestData() {
		repo.deleteAll();
	}

	@Test
	public void findAllReservations() {
		List<String> opSlotIdList = new ArrayList<String>();
		opSlotIdList.add("1");
		opSlotIdList.add("2");
		opSlotIdList.add("3");
		opSlotIdList.add("4");
		List<Reservation> reservations = reservationController.findReservationsByOPSlots(opSlotIdList.toArray(new String[opSlotIdList.size()]));
		assertEquals("not all reservations found", 4, reservations.size());
	}

	@Test
	public void findReservationsByDoctorId() {
		List<Reservation> reservations = reservationController.findReservationsByDoctorId(doctorId1);
		assertEquals("not all reservations of doctor d1@dse.at found", 3, reservations.size());

		reservations = reservationController.findReservationsByDoctorId(doctorId2);
		assertEquals("not all reservations of doctor d2@dse.at found", 1, reservations.size());

		reservations = reservationController.findReservationsByDoctorId(doctorId2, from1.getTime(), to1.getTime());
		assertEquals("not all reservations of doctor d2@dse.at found", 1, reservations.size());
	}

	@Test
	public void findReservationsByPatientId() {
		List<Reservation> reservations = reservationController.findReservationsByPatientIdAndTW(patientId1, from1.getTime(), to1.getTime());
		assertEquals(1, reservations.size());

		reservations = reservationController.findReservationsByPatientIdAndTW(patientId1, from1.getTime(), to2.getTime());
		assertEquals(2, reservations.size());

		reservations = reservationController.findReservationsByPatientIdAndTW(patientId2, from2.getTime(), to2.getTime());
		assertEquals(0, reservations.size());

		reservations = reservationController.findReservationsByPatientIdAndTW(patientId2, from1.getTime(), to3.getTime());
		assertEquals(2, reservations.size());

		reservations = reservationController.findReservationsByPatientIdAndTW(patientId2, from1.getTime(), to1.getTime());
		assertEquals(1, reservations.size());
	}

	@Test
	public void cancelReservation() {
		List<Reservation> reservations = reservationController.findReservationsByPatientIdAndTW(patientId1, from1.getTime(), to1.getTime());
		assertEquals(1, reservations.size());

		repo.deleteReservationByOpSlotId(reservations.get(0).getOpSlotId());

		reservations = reservationController.findReservationsByPatientIdAndTW(patientId1, from1.getTime(), to1.getTime());
		assertEquals(0, reservations.size());
	}

	@Test
	public void cancelReservationAndReserveAgain() {
		reservationController.cancelReservation("2");
		reservationController.reserve(from2.getTime(), to2.getTime(), 500, "eye", doctorId1, patientId1);
		List<Reservation> reservations = reservationController.findReservationsByPatientIdAndTW(patientId1,
				from1.getTime(), to1.getTime());
		assertEquals(1, reservations.size());
	}


	@Test
	public void contextLoads() {
	}

}
