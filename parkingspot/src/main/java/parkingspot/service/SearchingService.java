package parkingspot.service;



import parkingspot.entities.Vehicle;
import parkingspot.exception.ParkingException;

public interface SearchingService {

public void getRegNumberForColor(int level, String color) throws ParkingException;
	
	public void getSlotNumbersFromColor(int level, String colour) throws ParkingException;
	
	public int getSlotNoFromRegistrationNo(int level, String registrationNo) throws ParkingException;

}
