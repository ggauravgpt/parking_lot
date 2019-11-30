/**
 * 
 */
package parkingspot.requestHandler;

import parkingspot.constants.Constants;
import parkingspot.exception.ErrorCode;
import parkingspot.exception.ParkingException;
import parkingspot.services.ParkingService;
import parkingspot.services.SearchingService;
import parkingspot.entities.Car;


public class RequestRequestHandler implements AbstractRequestHandler
{
	private ParkingService parkingService;
	private SearchingService searchingService;
	
	
	public void setParkingService(ParkingService parkingService) throws ParkingException
	{
		this.parkingService = parkingService;
	}
	
	@Override
	public void execute(String input) throws ParkingException
	{
		int level = 1;
		String[] inputs = input.split(" ");
		String key = inputs[0];
		switch (key)
		{
			case Constants.CREATE_PARKING_LOT:
				try
				{
					int capacity = Integer.parseInt(inputs[1]);
					parkingService.createParkingLot(level, capacity);
				}
				catch (NumberFormatException e)
				{
					throw new ParkingException(ErrorCode.INVALID_VALUE.getMessage().replace("{variable}", "capacity"));
				}
				break;
			case Constants.PARK:
				parkingService.park(level, new Car(inputs[1], inputs[2]));
				break;
			case Constants.LEAVE:
				try
				{
					int slotNumber = Integer.parseInt(inputs[1]);
					parkingService.unPark(level, slotNumber);
				}
				catch (NumberFormatException e)
				{
					throw new ParkingException(
							ErrorCode.INVALID_VALUE.getMessage().replace("{variable}", "slot_number"));
				}
				break;
			case Constants.STATUS:
				parkingService.getStatus(level);
				break;
			case Constants.REG_NUMBER_FOR_CARS_WITH_COLOR:
				searchingService.getRegNumberForColor(level, inputs[1]);
				break;
			case Constants.SLOTS_NUMBER_FOR_CARS_WITH_COLOR:
				searchingService.getSlotNumbersFromColor(level, inputs[1]);
				break;
			case Constants.SLOTS_NUMBER_FOR_REG_NUMBER:
				searchingService.getSlotNoFromRegistrationNo(level, inputs[1]);
				break;
			default:
				break;
		}
	}
	
	@Override
	public void setService(ParkingService service)
	{
		this.parkingService = (ParkingService) service;
	}
}
