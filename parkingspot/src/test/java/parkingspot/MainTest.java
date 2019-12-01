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
import parkingspot.services.impl.ServicesImpl;
import parkingspot.entities.Car;

/**
 * Unit test for simple App.
 */
public class MainTest
{
	private int	parkingLevel;
	private final ByteArrayOutputStream	outContent	= new ByteArrayOutputStream();
	private ParkingService service ;
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@Before
	public void init()
	{
		parkingLevel = 1;
		System.setOut(new PrintStream(outContent));
		service = new ServicesImpl();
	}
	
	@After
	public void cleanUp()
	{
		System.setOut(null);
		
		service.doCleanup();
	}
	
	@Test
	public void leave() throws Exception
	{
		service.createParkingLot(parkingLevel, 90);
		service.park(parkingLevel, new Car("KA-01-HH-1234", "White"));
		service.park(parkingLevel, new Car("KA-01-HH-9999", "White"));
		service.park(parkingLevel, new Car("KA-01-BB-0001", "Black"));
		service.unPark(parkingLevel, 1);
		
		assertEquals("Createdparkinglotwith90slots\nAllocatedslotnumber:1\nAllocatedslotnumber:2\nAllocatedslotnumber:3\nSlotnumber1isfree",
				outContent.toString().trim().replace(" ", ""));
				

	}
	
	@Test
	public void createParkingLot() throws Exception
	{
		service.createParkingLot(parkingLevel, 60);
		assertTrue("createdparkinglotwith60slots".equalsIgnoreCase(outContent.toString().trim().replace(" ", "")));

	}
	
	@Test
	public void alreadyExistParkingLot() throws Exception
	{
		service.createParkingLot(parkingLevel, 6);
		assertTrue("createdparkinglotwith6slots".equalsIgnoreCase(outContent.toString().trim().replace(" ", "")));
		thrown.expect(ParkingException.class);
		thrown.expectMessage(is(ErrorCode.PARKING_ALREADY_EXIST.getMessage()));
		service.createParkingLot(parkingLevel, 6);
		
	}
	
	@Test
	public void testParkingCapacity() throws Exception
	{
		service.createParkingLot(parkingLevel, 3);
		service.park(parkingLevel, new Car("KA-01-HH-1234", "White"));
		service.park(parkingLevel, new Car("KA-01-HH-9998", "White"));
		assertEquals(Optional.of(1),service.getAvailableSlotsCount(parkingLevel));
 
	}
	
	@Test
	public void testEmptyParkingLot() throws Exception
	{
		service.createParkingLot(parkingLevel, 6);
		service.getStatus(parkingLevel);
		assertTrue(
				"createdparkinglotwith6slots\nSlotNo.\tRegistrationNo.\tColor\nSorry,parkinglotisempty."
						.equalsIgnoreCase(outContent.toString().trim().replace(" ", "")));

	}
	
	@Test
	public void testParkingLotIsFull() throws Exception
	{
		service.createParkingLot(parkingLevel, 3);
		service.park(parkingLevel, new Car("KA-01-HH-1234", "White"));
		service.park(parkingLevel, new Car("KA-01-HH-9999", "White"));
		service.park(parkingLevel, new Car("KA-01-BB-0001", "Black"));
		service.park(parkingLevel, new Car("KA-01-BB-0001", "Black"));
		assertEquals(Optional.of(-1),service.park(parkingLevel, new Car("KA-01-BB-0001", "Black")));
	}
	
	@Test
	public void testNearestSlotAllotment() throws Exception
	{
		service.createParkingLot(parkingLevel, 50);
		service.park(parkingLevel, new Car("KA-01-HH-1234", "White"));
		service.park(parkingLevel, new Car("KA-01-HH-9999", "White"));
		service.getSlotNoFromRegistrationNo(parkingLevel, "KA-01-HH-1234");
		service.getSlotNoFromRegistrationNo(parkingLevel, "KA-01-HH-9999");
		assertTrue("createdparkinglotwith50slots\nAllocatedslotnumber:1\nAllocatedslotnumber:2\n1\n2"
				.equalsIgnoreCase(outContent.toString().trim().replace(" ", "")));

	}
	

	
	@Test
	public void testWhenVehicleAlreadyPresent() throws Exception
	{

		service.createParkingLot(parkingLevel, 3);
		service.park(parkingLevel, new Car("KA-01-HH-1234", "White"));
		service.park(parkingLevel, new Car("KA-01-HH-1234", "White"));
		assertTrue(
				"createdparkinglotwith3slots\nAllocatedslotnumber:1\nSorry,vehicleisalreadyparked."
						.equalsIgnoreCase(outContent.toString().trim().replace(" ", "")));

	}
	
	@Test
	public void testWhenVehicleAlreadyPicked() throws Exception
	{
		service.createParkingLot(parkingLevel, 99);
		service.park(parkingLevel, new Car("KA-01-HH-1234", "White"));
		service.park(parkingLevel, new Car("KA-01-HH-9999", "White"));
		service.unPark(parkingLevel, 1);
		service.unPark(parkingLevel, 1);
		assertTrue(
				"createdparkinglotwith99slots\nAllocatedslotnumber:1\nAllocatedslotnumber:2\nSlotnumber1isfree\nSlotnumberisEmptyAlready."
						.equalsIgnoreCase(outContent.toString().trim().replace(" ", "")));

	}
	
	@Test
	public void testStatus() throws Exception
	{

		service.createParkingLot(parkingLevel, 10);
		service.park(parkingLevel, new Car("KA-01-HH-1234", "White"));
		service.park(parkingLevel, new Car("KA-01-HH-9999", "White"));
		service.getStatus(parkingLevel);
		assertTrue(
				"createdparkinglotwith10slots\nAllocatedslotnumber:1\nAllocatedslotnumber:2\nSlotNo.\tRegistrationNo.\tColor\n1\t\tKA-01-HH-1234\t\tWhite\n2\t\tKA-01-HH-9999\t\tWhite"
						.equalsIgnoreCase(outContent.toString().trim().replace(" ", "")));

		
	}
	

	

}
