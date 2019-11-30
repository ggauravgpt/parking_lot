/**
 * 
 */
package parkingspot.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import parkingspot.constants.Constants;
import parkingspot.dao.ParkingDataManager;
import parkingspot.dao.impl.MemoryParkingManager;
import parkingspot.exception.ErrorCode;
import parkingspot.exception.ParkingException;
import parkingspot.entities.Vehicle;
import parkingspot.model.strategy.NearestFirstParkingStrategy;
import parkingspot.model.strategy.ParkingStrategy;
import parkingspot.service.ParkingService;


public class ParkingServiceImpl implements ParkingService
{
	private ParkingDataManager<Vehicle> dataManager = null;
	
	private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
	
	@Override
	public void createParkingLot(int level, int capacity) throws ParkingException
	{
		if (dataManager != null)
			throw new ParkingException(ErrorCode.PARKING_ALREADY_EXIST.getMessage());
		List<Integer> parkingLevels = new ArrayList<>();
		List<Integer> capacityList = new ArrayList<>();
		List<ParkingStrategy> parkingStrategies = new ArrayList<>();
		parkingLevels.add(level);
		capacityList.add(capacity);
		parkingStrategies.add(new NearestFirstParkingStrategy());
		this.dataManager = MemoryParkingManager.getInstance(parkingLevels, capacityList, parkingStrategies);
		System.out.println("Created parking lot with " + capacity + " slots");
	}
	
	@Override
	public Optional<Integer> park(int level, Vehicle vehicle) throws ParkingException
	{
		Optional<Integer> value = Optional.empty();
		lock.writeLock().lock();
		validateParkingLot();
		try
		{
			value = Optional.of(dataManager.parkCar(level, vehicle));
			if (value.get() == Constants.NOT_AVAILABLE)
				System.out.println("Sorry, parking lot is full");
			else if (value.get() == Constants.VEHICLE_ALREADY_EXIST)
				System.out.println("Sorry, vehicle is already parked.");
			else
				System.out.println("Allocated slot number: " + value.get());
		}
		catch (Exception e)
		{
			throw new ParkingException(ErrorCode.PROCESSING_ERROR.getMessage(), e);
		}
		finally
		{
			lock.writeLock().unlock();
		}
		return value;
	}
	
	/**
	 * @throws ParkingException
	 */
	private void validateParkingLot() throws ParkingException
	{
		if (dataManager == null)
		{
			throw new ParkingException(ErrorCode.PARKING_NOT_EXIST_ERROR.getMessage());
		}
	}
	
	@Override
	public void unPark(int level, int slotNumber) throws ParkingException
	{
		lock.writeLock().lock();
		validateParkingLot();
		try
		{
			
			if (dataManager.leaveCar(level, slotNumber))
				System.out.println("Slot number " + slotNumber + " is free");
			else
				System.out.println("Slot number is Empty Already.");
		}
		catch (Exception e)
		{
			throw new ParkingException(ErrorCode.INVALID_VALUE.getMessage().replace("{variable}", "slot_number"), e);
		}
		finally
		{
			lock.writeLock().unlock();
		}
	}
	
	@Override
	public void getStatus(int level) throws ParkingException
	{
		lock.readLock().lock();
		validateParkingLot();
		try
		{
			System.out.println("Slot No.\tRegistration No.\tColor");
			List<String> statusList = dataManager.getStatus(level);
			if (statusList.size() == 0)
				System.out.println("Sorry, parking lot is empty.");
			else
			{
				for (String statusSting : statusList)
				{
					System.out.println(statusSting);
				}
			}
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
	
	public Optional<Integer> getAvailableSlotsCount(int level) throws ParkingException
	{
		lock.readLock().lock();
		Optional<Integer> value = Optional.empty();
		validateParkingLot();
		try
		{
			value = Optional.of(dataManager.getAvailableSlotsCount(level));
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
	
	
	@Override
	public void doCleanup()
	{
		if (dataManager != null)
			dataManager.doCleanup();
	}
}