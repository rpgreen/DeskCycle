/**
 * @(#) SerialDevice.java 
 *      Ryan Green 2006
 */

package org.rg.dcmon.device;
import java.io.*;
import java.util.*;
import javax.comm.*;

import org.rg.dcmon.monitor.*;

public class SerialDevice implements IDevice, SerialPortEventListener{

	private Vector charBuffer;
	private SerialPort serialPort;
	private InputStream inputStream;
	private OutputStream outputStream;
	
	public SerialDevice(){
	
	}
	
	public boolean init() {
		charBuffer = new Vector();
		//try {serialPort.close(); } catch(Exception e){ System.out.println(e.getLocalizedMessage());}
		
		CommPortIdentifier portId = null;
		boolean portFound = false;
		String portName = SettingsManager.getInstance().commPort;
		 
		Enumeration ports = CommPortIdentifier.getPortIdentifiers();
		if (ports == null)
		{
			System.out.println("SerialDevice: No comm ports found");
		}
		while (ports.hasMoreElements() && portFound == false)
		{
			portId = (CommPortIdentifier) ports.nextElement();

			if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL)
			{
				if (portId.getName().equals(portName)){
					//  Is the port in use?	
					if (portId.isCurrentlyOwned())
					{
						System.out.println("SerialDevice: Port "
							   + portId.getName()
							   + " in use by "
							   + portId.getCurrentOwner());
						return false;
					}
					else 
					{
						portFound = true;
					}
				}
			}
		}
			
		if (!portFound) {
		    System.out.println("SerialDevice: Port " + portName + " not found.");
		    return false;
		} 
		else {
			try {
			    serialPort = (SerialPort) portId.open(DCMon.APP_TITLE, 2000);
			} 
			catch (PortInUseException e) {
				 System.out.println("SerialDevice: Port " + portName + " in use by "+ e.currentOwner);
				 return false;
			}

			try {
			    inputStream = serialPort.getInputStream();
			    outputStream = serialPort.getOutputStream();
			} 
			catch (IOException e) {
				System.out.println("SerialDevice: IO Exception.");
				return false;
			}

			try {
			    serialPort.addEventListener(this);
			} 
			catch (TooManyListenersException e) {
				System.out.println("SerialDevice: Too many listeners exception.");
				return false;
			}

			serialPort.notifyOnDataAvailable(true);

			try {
			    serialPort.setSerialPortParams(SettingsManager.getInstance().commBaudRate, SerialPort.DATABITS_8, 
							   SerialPort.STOPBITS_1, 
							   SerialPort.PARITY_NONE);
			} 
			catch (UnsupportedCommOperationException e) {
				System.out.println("SerialDevice: Unsupported comm operation.");
				return false;
			}
			
			return true;
		}
	}
	
	/**
	 * Relinquish ownership of the serial port.
	 * @return boolean
	 */
	public void close() {
		if (serialPort != null){
			serialPort.close();
		}
	}
	
	public int numCharsAvailable() {
		return charBuffer.size();
	}

	public char receive() {
		char c = (Character) charBuffer.elementAt(0);
		charBuffer.removeElementAt(0);
		//System.out.println("SerialDevice: " + c + " received.");	
		return c;
	}

	public void send(char c) {
		try {
            outputStream.write(c);
        } 
		catch (IOException e) {
        	System.out.println("SerialDevice: IO Exception");
        }
        //System.out.println("SerialDevice: " + c + " sent.");	
	}

	public void send(String str) {
		for (int i=0; i < str.length(); i++){
			send(str.charAt(i));
		}
	}

	public void serialEvent(SerialPortEvent event) {
		switch (event.getEventType()) {

		case SerialPortEvent.BI:

		case SerialPortEvent.OE:

		case SerialPortEvent.FE:

		case SerialPortEvent.PE:

		case SerialPortEvent.CD:

		case SerialPortEvent.CTS:

		case SerialPortEvent.DSR:

		case SerialPortEvent.RI:

		case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
		    break;

		case SerialPortEvent.DATA_AVAILABLE:
		    byte[] readBuffer = new byte[20];

		    try {
		    	while (inputStream.available() > 0) {
		    		charBuffer.add((char) inputStream.read());
		    	} 
		    } catch (IOException e) {}

		    break;
		}
	  }


	
}
