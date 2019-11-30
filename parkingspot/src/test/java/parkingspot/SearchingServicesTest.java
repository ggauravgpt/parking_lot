package parkingspot;

import static org.hamcrest.CoreMatchers.is;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import parkingspot.entities.Car;
import parkingspot.exception.ErrorCode;
import parkingspot.exception.ParkingException;
import parkingspot.services.ParkingService;
import parkingspot.services.SearchingService;
import parkingspot.services.impl.ParkingServiceImpl;
import parkingspot.services.impl.SeachingServiceImpl;

public class SearchingServicesTest {
	
	private int	parkingLevel;
	private final ByteArrayOutputStream	outContent	= new ByteArrayOutputStream();
	private ParkingService park_instance;
	private SearchingService search_instance;
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
//	@BeforeClass
//	public void beforeClass()
//	{
//		
//	}
	
	
	@Before
	public void init()
	{
		parkingLevel = 1;
		System.setOut(new PrintStream(outContent));
		park_instance = new ParkingServiceImpl();
		search_instance = new SeachingServiceImpl();
		
	}
	
	@After
	public void cleanUp()
	{
		System.setOut(null);
	}

	@Test
	public void testGetSlotsByRegNo() throws Exception
	{
	
		thrown.expect(ParkingException.class);
		thrown.expectMessage(is(ErrorCode.PARKING_NOT_EXIST_ERROR.getMessage()));
		search_instance.getSlotNoFromRegistrationNo(parkingLevel, "KA-01-HH-1234");
		assertEquals("Sorry,CarParkingDoesnotExist", outContent.toString().trim().replace(" ", ""));
		park_instance.createParkingLot(parkingLevel, 10);
		park_instance.park(parkingLevel, new Car("KA-01-HH-1234", "White"));
		park_instance.park(parkingLevel, new Car("KA-01-HH-9999", "White"));
		search_instance.getSlotNoFromRegistrationNo(parkingLevel, "KA-01-HH-1234");
		assertEquals("Sorry,CarParkingDoesnotExist\n" + "Createdparkinglotwith6slots\n" + "\n"
				+ "Allocatedslotnumber:1\n" + "\n" + "Allocatedslotnumber:2\n1",
				outContent.toString().trim().replace(" ", ""));
		search_instance.getSlotNoFromRegistrationNo(parkingLevel, "KA-01-HH-1235");
		assertEquals("Sorry,CarParkingDoesnotExist\n" + "Createdparkinglotwith10slots\n" + "\n"
				+ "Allocatedslotnumber:1\n" + "\n" + "Allocatedslotnumber:2\n1\nNotFound",
				outContent.toString().trim().replace(" ", ""));
		park_instance.doCleanup();
	}
	
	//getRegNumberForColor
	@Test
	
	public void testGetSlotsByColor() throws Exception
	{
		
		thrown.expect(ParkingException.class);
		thrown.expectMessage(is(ErrorCode.PARKING_NOT_EXIST_ERROR.getMessage()));
		search_instance.getRegNumberForColor(parkingLevel, "white");
		assertEquals("Sorry,CarParkingDoesnotExist", outContent.toString().trim().replace(" ", ""));
		park_instance.createParkingLot(parkingLevel, 7);
		park_instance.park(parkingLevel, new Car("KA-01-HH-1234", "White"));
		park_instance.park(parkingLevel, new Car("KA-01-HH-9999", "White"));
		park_instance.getStatus(parkingLevel); 
		search_instance.getRegNumberForColor(parkingLevel, "White");
		assertEquals(
				"Sorry,CarParkingDoesnotExist\n" + "Createdparkinglotwith7slots\n" + "\n" + "Allocatedslotnumber:1\n"
						+ "\n" + "Allocatedslotnumber:2\nKA-01-HH-1234,KA-01-HH-9999",
				outContent.toString().trim().replace(" ", ""));
		search_instance.getRegNumberForColor(parkingLevel, "Red");
		assertEquals(
				"Sorry,CarParkingDoesnotExist\n" + "Createdparkinglotwith6slots\n" + "\n" + "Allocatedslotnumber:1\n"
						+ "\n" + "Allocatedslotnumber:2\n" + "KA-01-HH-1234,KA-01-HH-9999,Notfound",
				outContent.toString().trim().replace(" ", ""));
		park_instance.doCleanup();
		
	}


}




