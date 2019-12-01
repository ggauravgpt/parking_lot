/**
 * 
 */
package parkingspot.entities.strategy;


public interface ParkingStrategy
{
	public void add(int i);
	
	public int getSlot();
	
	public void removeSlot(int slot);
}
