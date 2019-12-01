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
import parkingspot.services.impl.ServicesImpl;


public class SearchingServicesTest {
	
	private int	parkingLevel;
	private final ByteArrayOutputStream	outContent	= new ByteArrayOutputStream();
	private ParkingService park_instance;
	
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	
	@Before
	public void init()
	{
		parkingLevel = 1;
		System.setOut(new PrintStream(outContent));
		park_instance = new ServicesImpl();
		
	}
	
	@After
	public void cleanUp()
	{
		System.setOut(null);
		park_instance.doCleanup();
	}

	@Test
	public void testGetRegNumbeByColorPositive() throws Exception
	{
		park_instance.createParkingLot(parkingLevel, 7);
		park_instance.park(parkingLevel, new Car("KA-01-HH-1234", "White"));
		park_instance.park(parkingLevel, new Car("KA-01-HH-9999", "White"));
		park_instance.getStatus(parkingLevel); 
		assertEquals("Createdparkinglotwith7slots\nAllocatedslotnumber:1\nAllocatedslotnumber:2\nSlotNo.\tRegistrationNo.\tColor\n1\t\tKA-01-HH-1234\t\tWhite\n2\t\tKA-01-HH-9999\t\tWhite",
				outContent.toString().trim().replace(" ", ""));
		park_instance.getRegNumberForColor(parkingLevel, "White");
		
		assertEquals("Createdparkinglotwith7slots\nAllocatedslotnumber:1\nAllocatedslotnumber:2\nSlotNo.\tRegistrationNo.\tColor\n1\t\tKA-01-HH-1234\t\tWhite\n2\t\tKA-01-HH-9999\t\tWhite\nKA-01-HH-1234,KA-01-HH-9999",
				outContent.toString().trim().replace(" ", ""));
			
	}
	@SuppressWarnings("deprecation")
	@Test
	public void testGetRegNumberByColorNegative() throws Exception
	{
		park_instance.createParkingLot(parkingLevel, 7);
		park_instance.park(parkingLevel, new Car("KA-01-HH-1234", "White"));
		park_instance.park(parkingLevel, new Car("KA-01-HH-9999", "White"));
		park_instance.getStatus(parkingLevel); 
		park_instance.getRegNumberForColor(parkingLevel, "red");
		 assertEquals(null, null);
		
	}
	
	@Test
	public void testGetSlotsByColorPositive() throws Exception
	{
		park_instance.createParkingLot(parkingLevel, 7);
		park_instance.park(parkingLevel, new Car("KA-01-HH-1234", "White"));
		park_instance.park(parkingLevel, new Car("KA-01-HH-9999", "White"));
		park_instance.getStatus(parkingLevel); 
		assertEquals("Createdparkinglotwith7slots\nAllocatedslotnumber:1\nAllocatedslotnumber:2\nSlotNo.\tRegistrationNo.\tColor\n1\t\tKA-01-HH-1234\t\tWhite\n2\t\tKA-01-HH-9999\t\tWhite",
				outContent.toString().trim().replace(" ", ""));
		park_instance.getSlotNumbersFromColor(parkingLevel, "White");
		
		assertEquals("Createdparkinglotwith7slots\nAllocatedslotnumber:1\nAllocatedslotnumber:2\nSlotNo.\tRegistrationNo.\tColor\n1\t\tKA-01-HH-1234\t\tWhite\n2\t\tKA-01-HH-9999\t\tWhite\n1,2",
				outContent.toString().trim().replace(" ", ""));
			
	}
	@Test
	public void testGetSlotsByColorNegative() throws Exception
	{
		park_instance.createParkingLot(parkingLevel, 7);
		park_instance.park(parkingLevel, new Car("KA-01-HH-1234", "White"));
		park_instance.park(parkingLevel, new Car("KA-01-HH-9999", "White"));
		park_instance.getStatus(parkingLevel); 
		park_instance.getSlotNumbersFromColor(parkingLevel, "red");
		 assertEquals(null, null);
		
	}	
		

	@Test
	public void testGetSlotsByRegNoPositive() throws Exception
	{
		park_instance.createParkingLot(parkingLevel, 10);
		park_instance.park(parkingLevel, new Car("KA-01-HH-1234", "White"));
		park_instance.park(parkingLevel, new Car("KA-01-HH-9999", "White"));
		park_instance.getStatus(parkingLevel); 
		park_instance.getSlotNoFromRegistrationNo(parkingLevel, "KA-01-HH-1234");
		assertEquals("Createdparkinglotwith10slots\nAllocatedslotnumber:1\nAllocatedslotnumber:2\nSlotNo.\tRegistrationNo.\tColor\n1\t\tKA-01-HH-1234\t\tWhite\n2\t\tKA-01-HH-9999\t\tWhite\n1",
				outContent.toString().trim().replace(" ", ""));	
	}
	@Test
	public void testGetSlotsByRegNoNegative() throws Exception
	{
		park_instance.createParkingLot(parkingLevel, 10);
		park_instance.park(parkingLevel, new Car("KA-01-HH-1234", "White"));
		park_instance.park(parkingLevel, new Car("KA-01-HH-9999", "White"));
		park_instance.getStatus(parkingLevel); 
		park_instance.getSlotNoFromRegistrationNo(parkingLevel, "KA-01-HH-5555");
		assertEquals("Createdparkinglotwith10slots\nAllocatedslotnumber:1\nAllocatedslotnumber:2\nSlotNo.\tRegistrationNo.\tColor\n1\t\tKA-01-HH-1234\t\tWhite\n2\t\tKA-01-HH-9999\t\tWhite\nNotFound",
				outContent.toString().trim().replace(" ", ""));
		
	}

	
	


}




