package parkingspot.main;

public class PrintConsole {
	
	public static void printUsage()
	{
		StringBuffer buffer = new StringBuffer();

		buffer = buffer.append("1) Command For creating parking lot of size n       --> create_parking_lot {size}")
				.append("\n");
		buffer = buffer
				.append("2) Command For parking a car                              ---> park <<car_number>> {color}")
				.append("\n");
		buffer = buffer.append("3) Command For Removeing (Unparking ) car from parking            ---> leave {slot_number}")
				.append("\n");
		buffer = buffer.append("4) Command For Printing status of parking slot                     ---> status").append("\n");
		buffer = buffer.append(
				"5) Command to Get cars registration no for the given car color ---> registration_numbers_for_cars_with_color {car_color}")
				.append("\n");
		buffer = buffer.append(
				"6) Command to Get slot numbers for the given car color         ---> slot_numbers_for_cars_with_color {car_color}")
				.append("\n");
		buffer = buffer.append(
				"7) Command to Get slot number for the given car number         ---> slot_number_for_registration_number {car_number}")
				.append("\n");
		buffer = buffer.append(
				"To end Execution Enter 'exit or 0' ").append("\n");

		System.out.println(buffer.toString());
		
	}

}
