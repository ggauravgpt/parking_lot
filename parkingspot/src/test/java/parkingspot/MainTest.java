package parkingspot;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import parkingspot.exception.ErrorCode;
import parkingspot.exception.ParkingException;
import parkingspot.services.ParkingService;
import parkingspot.services.SearchingService;
import parkingspot.services.impl.ParkingServiceImpl;
import parkingspot.services.impl.SeachingServiceImpl;
import parkingspot.entities.Car;

/**
 * Unit test for simple App.
 */
public class MainTest
{
	private int	parkingLevel;
	private final ByteArrayOutputStream	outContent	= new ByteArrayOutputStream();
	private ParkingService instance ;
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@Before
	public void init()
	{
		parkingLevel = 1;
		System.setOut(new PrintStream(outContent));
		instance = new ParkingServiceImpl();
	}
	
	@After
	public void cleanUp()
	{
		System.setOut(null);
		
		instance.doCleanup();
	}
	
	@Test
	public void createParkingLot() throws Exception
	{
//		 instance = new ParkingServiceImpl();
		instance.createParkingLot(parkingLevel, 60);
		assertTrue("createdparkinglotwith60slots".equalsIgnoreCase(outContent.toString().trim().replace(" ", "")));
		//instance.doCleanup();
	}
	
	@Test
	public void alreadyExistParkingLot() throws Exception
	{
//		ParkingService instance = new ParkingServiceImpl();
		instance.createParkingLot(parkingLevel, 60);
		assertTrue("createdparkinglotwith60slots".equalsIgnoreCase(outContent.toString().trim().replace(" ", "")));
		thrown.expect(ParkingException.class);
		thrown.expectMessage(is(ErrorCode.PARKING_ALREADY_EXIST.getMessage()));
		instance.createParkingLot(parkingLevel, 60);
		//instance.doCleanup();
	}
	
	@Test
	public void testParkingCapacity() throws Exception
	{
		ParkingService Capacityinstance = new ParkingServiceImpl();
//		thrown.expect(ParkingException.class);
//		thrown.expectMessage(is(ErrorCode.PARKING_NOT_EXIST_ERROR.getMessage()));
//		instance.park(parkingLevel, new Car("KA-01-HH-1234", "White"));
//		assertEquals("Sorry, Car Parking Does not Exist", outContent.toString().trim().replace(" ", ""));
		Capacityinstance.createParkingLot(parkingLevel, 3);
		Capacityinstance.park(parkingLevel, new Car("KA-01-HH-1234", "White"));
		Capacityinstance.park(parkingLevel, new Car("KA-01-HH-9999", "White"));
		Capacityinstance.park(parkingLevel, new Car("KA-01-BB-0001", "Black"));
		assertEquals(Optional.of(0),Capacityinstance.getAvailableSlotsCount(parkingLevel));
		//Capacityinstance.doCleanup();
	}
	
	@Test
	public void testEmptyParkingLot() throws Exception
	{
		ParkingService instance = new ParkingServiceImpl();
		thrown.expect(ParkingException.class);
		thrown.expectMessage(is(ErrorCode.PARKING_NOT_EXIST_ERROR.getMessage()));
		instance.getStatus(parkingLevel);
		assertFalse("Sorry,CarParkingDoesnotExist".equalsIgnoreCase(outContent.toString().trim().replace(" ", "")));
		instance.createParkingLot(parkingLevel, 6);
		instance.getStatus(parkingLevel);
		assertTrue(
				"Sorry,CarParkingDoesnotExist\ncreatedparkinglotwith6slots\nSlotNo.\tRegistrationNo.\tColor\nSorry,parkinglotisempty."
						.equalsIgnoreCase(outContent.toString().trim().replace(" ", "")));
		//instance.doCleanup();
	}
	
	@Test
	public void testParkingLotIsFull() throws Exception
	{
		instance.createParkingLot(parkingLevel, 3);
		instance.park(parkingLevel, new Car("KA-01-HH-1234", "White"));
		instance.park(parkingLevel, new Car("KA-01-HH-9999", "White"));
		instance.park(parkingLevel, new Car("KA-01-BB-0001", "Black"));
		instance.park(parkingLevel, new Car("KA-01-BB-0001", "Black"));
		assertEquals(Optional.of(-1),instance.park(parkingLevel, new Car("KA-01-BB-0001", "Black")));
	}
	
	@Test
	public void testNearestSlotAllotment() throws Exception
	{
//		ParkingService instance = new ParkingServiceImpl();
		SearchingService search_instance = new SeachingServiceImpl();
		thrown.expect(ParkingException.class);
		thrown.expectMessage(is(ErrorCode.PARKING_NOT_EXIST_ERROR.getMessage()));
		instance.park(parkingLevel, new Car("KA-01-HH-1234", "White"));
		assertEquals("Sorry,CarParkingDoesnotExist", outContent.toString().trim().replace(" ", ""));
		instance.createParkingLot(parkingLevel, 5);
		instance.park(parkingLevel, new Car("KA-01-HH-1234", "White"));
		instance.park(parkingLevel, new Car("KA-01-HH-9999", "White"));
		search_instance.getSlotNoFromRegistrationNo(parkingLevel, "KA-01-HH-1234");
		search_instance.getSlotNoFromRegistrationNo(parkingLevel, "KA-01-HH-9999");
		assertTrue("createdparkinglotwith5slots\nAllocatedslotnumber:1\nAllocatedslotnumber:2"
				.equalsIgnoreCase(outContent.toString().trim().replace(" ", "")));
//		instance.doCleanup();
	}
	
	@Test
	public void leave() throws Exception
	{
//		ParkingService instance = new ParkingServiceImpl();
		thrown.expect(ParkingException.class);
		thrown.expectMessage(is(ErrorCode.PARKING_NOT_EXIST_ERROR.getMessage()));
		instance.unPark(parkingLevel, 2);
		assertEquals("Sorry,CarParkingDoesnotExist", outContent.toString().trim().replace(" ", ""));
		instance.createParkingLot(parkingLevel, 6);
		instance.park(parkingLevel, new Car("KA-01-HH-1234", "White"));
		instance.park(parkingLevel, new Car("KA-01-HH-9999", "White"));
		instance.park(parkingLevel, new Car("KA-01-BB-0001", "Black"));
		instance.unPark(parkingLevel, 4);
		assertTrue(
				"Sorry,CarParkingDoesnotExist\ncreatedparkinglotwith6slots\nAllocatedslotnumber:1\nAllocatedslotnumber:2\nAllocatedslotnumber:3\nSlotnumber4isfree"
						.equalsIgnoreCase(outContent.toString().trim().replace(" ", "")));
	//	instance.doCleanup();
	}
	
	@Test
	public void testWhenVehicleAlreadyPresent() throws Exception
	{
//		ParkingService instance = new ParkingServiceImpl();
		thrown.expect(ParkingException.class);
		thrown.expectMessage(is(ErrorCode.PARKING_NOT_EXIST_ERROR.getMessage()));
		instance.park(parkingLevel, new Car("KA-01-HH-1234", "White"));
		assertEquals("Sorry,CarParkingDoesnotExist", outContent.toString().trim().replace(" ", ""));
		instance.createParkingLot(parkingLevel, 3);
		instance.park(parkingLevel, new Car("KA-01-HH-1234", "White"));
		instance.park(parkingLevel, new Car("KA-01-HH-1234", "White"));
		assertTrue(
				"Sorry,CarParkingDoesnotExist\ncreatedparkinglotwith3slots\nAllocatedslotnumber:1\nSorry,vehicleisalreadyparked."
						.equalsIgnoreCase(outContent.toString().trim().replace(" ", "")));
//		instance.doCleanup();
	}
	
	@Test
	public void testWhenVehicleAlreadyPicked() throws Exception
	{
//		ParkingService instance = new ParkingServiceImpl();
		thrown.expect(ParkingException.class);
		thrown.expectMessage(is(ErrorCode.PARKING_NOT_EXIST_ERROR.getMessage()));
		instance.park(parkingLevel, new Car("KA-01-HH-1234", "White"));
		assertEquals("Sorry,CarParkingDoesnotExist", outContent.toString().trim().replace(" ", ""));
		instance.createParkingLot(parkingLevel, 99);
		instance.park(parkingLevel, new Car("KA-01-HH-1234", "White"));
		instance.park(parkingLevel, new Car("KA-01-HH-9999", "White"));
		instance.unPark(parkingLevel, 1);
		instance.unPark(parkingLevel, 1);
		assertTrue(
				"Sorry,CarParkingDoesnotExist\ncreatedparkinglotwith99slots\nAllocatedslotnumber:1\nAllocatedslotnumber:2\nSlotnumberisEmptyAlready."
						.equalsIgnoreCase(outContent.toString().trim().replace(" ", "")));
//		instance.doCleanup();
	}
	
	@Test
	public void testStatus() throws Exception
	{
//		ParkingService instance = new ParkingServiceImpl();
		thrown.expect(ParkingException.class);
		thrown.expectMessage(is(ErrorCode.PARKING_NOT_EXIST_ERROR.getMessage()));
		instance.getStatus(parkingLevel);
		assertEquals("Sorry,CarParkingDoesnotExist", outContent.toString().trim().replace(" ", ""));
		instance.createParkingLot(parkingLevel, 8);
		instance.park(parkingLevel, new Car("KA-01-HH-1234", "White"));
		instance.park(parkingLevel, new Car("KA-01-HH-9999", "White"));
		instance.getStatus(parkingLevel);
		assertTrue(
				"Sorry,CarParkingDoesnotExist\ncreatedparkinglotwith8slots\nAllocatedslotnumber:1\nAllocatedslotnumber:2\nSlotNo.\tRegistrationNo.\tColor\n1\tKA-01-HH-1234\tWhite\n2\tKA-01-HH-9999\tWhite"
						.equalsIgnoreCase(outContent.toString().trim().replace(" ", "")));
//		instance.doCleanup();
		
	}
	

	

}
