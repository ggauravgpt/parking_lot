package parkingspot.Main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

import parkingspot.exception.ErrorCode;
import parkingspot.exception.ParkingException;
import parkingspot.requestHandler.AbstractRequestHandler;
import parkingspot.requestHandler.RequestRequestHandler;
import parkingspot.services.impl.ServicesImpl;
import parkingspot.Main.PrintConsole;


public class Main
{
	public static void main(String[] args)
	{
		AbstractRequestHandler handler = new RequestRequestHandler();
		handler.setService(new ServicesImpl());
		BufferedReader bufferReader = null;
		String input = null;
		try
		{
			System.out.println("\n\n\n\n\n");
			System.out.println("=====================================================");
			System.out.println("=====================Welcome=========================");
			
			
			PrintConsole.printUsage();
			switch (args.length)
			{
				case 0: // Interactive: command-line input/output
				{
				//	System.out.println("Please Enter 'exit or 0' to end Execution");
					System.out.println("Input:");
					while (true)
					{
						try
						{
							bufferReader = new BufferedReader(new InputStreamReader(System.in));
							input = bufferReader.readLine().trim();
							if (input.equalsIgnoreCase("exit")|| input.equalsIgnoreCase("0"))
							{
								break;
							}
							else
							{
								if (handler.validate(input))
								{
									try
									{
										handler.execute(input.trim());
									}
									catch (Exception e)
									{
										System.out.println(e.getMessage());
									}
								}
								else
								{
									PrintConsole.printUsage();
								}
							}
						}
						catch (Exception e)
						{
							throw new ParkingException(ErrorCode.INVALID_REQUEST.getMessage(), e);
						}
					}
					break;
				}
				case 1:// File input/output
				{
					File inputFile = new File(args[0]);
					try
					{
						bufferReader = new BufferedReader(new FileReader(inputFile));
						int lineNo = 1;
						while ((input = bufferReader.readLine()) != null)
						{
							input = input.trim();
							if (handler.validate(input))
							{
								try
								{
									handler.execute(input);
								}
								catch (Exception e)
								{
									System.out.println(e.getMessage());
								}
							}
							else
								System.out.println("Incorrect Command Found at line: " + lineNo + " ,Input: " + input);
							lineNo++;
						}
					}
					catch (Exception e)
					{
						throw new ParkingException(ErrorCode.INVALID_FILE.getMessage(), e);
					}
					break;
				}
				default:
					System.out.println("Invalid input. Usage Style: java -jar <jar_file_path> <input_file_path>");
			}
		}
		catch (ParkingException e)
		{
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		finally
		{
			try
			{
				if (bufferReader != null)
					bufferReader.close();
			}
			catch (IOException e)
			{
			}
		}
	}
	
	
	
}
