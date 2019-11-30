package parkingspot.services.impl;

import java.util.List;
import java.util.StringJoiner;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import parkingspot.exception.ErrorCode;
import parkingspot.exception.ParkingException;
import parkingspot.services.SearchingService;
import parkingspot.dao.ParkingDataManager;
import parkingspot.entities.Vehicle;;

public class SeachingServiceImpl implements SearchingService {
	
private ParkingDataManager<Vehicle> dataManager = null;
	
	private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
	
	private void validateParkingLot() throws ParkingException
	{
		if (dataManager == null)
		{
			throw new ParkingException(ErrorCode.PARKING_NOT_EXIST_ERROR.getMessage());
		}
	}

	@Override
	public void getRegNumberForColor(int level, String color) throws ParkingException
	{
		lock.readLock().lock();
		validateParkingLot();
		try
		{
			List<String> registrationList = dataManager.getRegNumberForColor(level, color);
			if (registrationList.size() == 0)
				System.out.println("Not Found");
			else
				System.out.println(String.join(",", registrationList));
		}
		catch (Exception e)
		{
			throw new ParkingException(ErrorCode.PROCESSING_ERROR.getMessage(), e);
		}
		finally
		{
			lock.readLock().unlock();
		}
	}
	
	@Override
	public void getSlotNumbersFromColor(int level, String color) throws ParkingException
	{
		lock.readLock().lock();
		validateParkingLot();
		try
		{
			List<Integer> slotList = dataManager.getSlotNumbersFromColor(level, color);
			if (slotList.size() == 0)
				System.out.println("Not Found");
			StringJoiner joiner = new StringJoiner(",");
			for (Integer slot : slotList)
			{
				joiner.add(slot + "");
			}
			System.out.println(joiner.toString());
		}
		catch (Exception e)
		{
			throw new ParkingException(ErrorCode.PROCESSING_ERROR.getMessage(), e);
		}
		finally
		{
			lock.readLock().unlock();
		}
	}
	
	@Override
	public int getSlotNoFromRegistrationNo(int level, String registrationNo) throws ParkingException
	{
		int value = -1;
		lock.readLock().lock();
		validateParkingLot();
		try
		{
			value = dataManager.getSlotNoFromRegistrationNo(level, registrationNo);
			System.out.println(value != -1 ? value : "Not Found");
		}
		catch (Exception e)
		{
			throw new ParkingException(ErrorCode.PROCESSING_ERROR.getMessage(), e);
		}
		finally
		{
			lock.readLock().unlock();
		}
		return value;
	}

}
