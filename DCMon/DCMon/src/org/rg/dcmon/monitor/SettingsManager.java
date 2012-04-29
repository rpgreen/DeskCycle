/**
 * @(#) SettingsManager.java 
 *      Ryan Green 2006
 */

package org.rg.dcmon.monitor;

import org.rg.dcmon.device.DeviceCommType;
import org.rg.dcmon.device.state.DeviceInitKeyState;
import org.rg.dcmon.session.DistanceUnit;
import org.rg.dcmon.session.VelocityUnit;
import org.xml.sax.*;
import org.xml.sax.helpers.XMLReaderFactory;
import org.xml.sax.helpers.DefaultHandler;
import java.io.*;

public class SettingsManager extends DefaultHandler{
	
	//private static final String XML_SOURCE = "E:\\School\\Senior Project\\DCMon\\DCMon\\DCMonSettings.xml";//TOOD: adjust on release
	private static final String XML_SOURCE = "C:\\Ryan\\DCMon\\DCMonSettings.xml";//TOOD: adjust on release
	private static final String FILE_PROTOCOL = "file:\\";	
	
	//TODO: FIX ABSOULUTE PATH
	public String username;
	public String pass;
	public int updateInterval;
	public boolean uploadSession;
	public int sessionStartTicks;
	public long sessionTimeoutMillis;
	public long commTimeoutMillis;
	public String serverHostname;
	public String commPort;
	public int commBaudRate;
	public DeviceCommType deviceCommType;
	public DistanceUnit distanceUnit;  
	public VelocityUnit velocityUnit;
	
	private static SettingsManager instance;
	public StringBuffer accumulator;
	
	private SettingsManager(){
		//parse xml, set data
	
		load();
	}
	
	/**
	 * @return 
	 */
	public static SettingsManager getInstance(){
		if (instance == null){
			instance = new SettingsManager();
		}
		return instance;
	}
	
	public void load(){
		accumulator = new StringBuffer();
		
		try {
			XMLReader parser = XMLReaderFactory.createXMLReader();		        	        		        
			parser.setContentHandler(this);		      
			parser.parse(FILE_PROTOCOL + XML_SOURCE);	        
		}
		catch (Exception e) {
			System.err.println(e); 
		}
	}
		
	public void characters(char[] text, int start, int length)
	throws SAXException {
		accumulator.append(text, start, length); 
	}  
	
	public void startElement(String namespaceURI, String localName,
			String qualifiedName, Attributes atts) {		
		accumulator.setLength(0);	  				
	}
	
	public void endElement(String namespaceURI, String localName, String qualifiedName) {
		if (qualifiedName.equals("UserName")){
			username = accumulator.toString().trim();
		}  		
		else if (qualifiedName.equals("Pass")){
			pass = accumulator.toString().trim();
		}  		
		else if (qualifiedName.equals("UpdateInterval")){
			updateInterval = Integer.parseInt(accumulator.toString().trim());
		}  
		else if (qualifiedName.equals("UploadSession")){
			String u = accumulator.toString().trim();
			if (u.equals("true"))
			{
				uploadSession = true;
			}
			else {
				uploadSession = false;
			}
		}  
		else if (qualifiedName.equals("SessionStartTicks")){
			sessionStartTicks = Integer.parseInt(accumulator.toString().trim());
		}  
		else if (qualifiedName.equals("SessionTimeout")){
			sessionTimeoutMillis = Integer.parseInt(accumulator.toString().trim());
		}  
		else if (qualifiedName.equals("CommTimeout")){
			commTimeoutMillis = Integer.parseInt(accumulator.toString().trim());
		}  
		else if (qualifiedName.equals("ServerHost")){
			serverHostname = accumulator.toString().trim();
		}  
		else if (qualifiedName.equals("DeviceCommType")){
			String u = accumulator.toString().trim();
			if (u.equals("DEVICE_SERIAL"))
			{
				deviceCommType = DeviceCommType.DEVICE_SERIAL;
			}
			else if (u.equals("DEVICE_NET")){
				deviceCommType = DeviceCommType.DEVICE_NET;
			}
			
		}  
		else if (qualifiedName.equals("DistanceUnit")){
			String u = accumulator.toString().trim();
			//M, CM, FT, KM, IN, MI
			if (u.equals("M"))
			{
				distanceUnit = DistanceUnit.M;
			}
			else if (u.equals("CM")){
				distanceUnit = DistanceUnit.CM;
			}
			else if (u.equals("FT")){
				distanceUnit = DistanceUnit.FT;
			}
			else if (u.equals("KM")){
				distanceUnit = DistanceUnit.KM;
			}
			else if (u.equals("IN")){
				distanceUnit = DistanceUnit.IN;
			}
			else if (u.equals("MI")){
				distanceUnit = DistanceUnit.MI;
			}
		} 
		else if (qualifiedName.equals("VelocityUnit")){
			String u = accumulator.toString().trim();
			if (u.equals("M_S"))
			{
				velocityUnit = VelocityUnit.M_S;
			}
			else if (u.equals("CM_S")){
				velocityUnit = VelocityUnit.CM_S;
			}
			else if (u.equals("IN_S")){
				velocityUnit = VelocityUnit.IN_S;
			}
			else if (u.equals("FT_S")){
				velocityUnit = VelocityUnit.FT_S;
			}
			else if (u.equals("KM_H")){
				velocityUnit = VelocityUnit.KM_H;
			}
			else if (u.equals("MI_H")){
				velocityUnit = VelocityUnit.MI_H;
			}
		} 
		else if (qualifiedName.equals("CommPort")){
			commPort = accumulator.toString().trim();
		}  
		else if (qualifiedName.equals("CommBaud")){
			commBaudRate = Integer.parseInt(accumulator.toString().trim());
		}  
	}
	
	public void save(){
		String src = "<?xml version=\"1.0\" ?>\r\n\r\n"; 
		src += "<DCMonSettings>\r\n\r\n";
		
		src += "<UserName>\r\n   ";
		src += username;
		src += "\r\n</UserName>\r\n\r\n";
		
		src += "<Pass>\r\n   ";
		src += pass;
		src += "\r\n</Pass>\r\n\r\n";
		
		src += "<UpdateInterval>\r\n   ";
		src += updateInterval;
		src += "\r\n</UpdateInterval>\r\n\r\n";
		
		src += "<UploadSession>\r\n   ";
		src += uploadSession;
		src += "\r\n</UploadSession>\r\n\r\n";
		
		src += "<SessionStartTicks>\r\n   ";
		src += sessionStartTicks;
		src += "\r\n</SessionStartTicks>\r\n\r\n";
		
		src += "<SessionTimeout>\r\n   ";
		src += sessionTimeoutMillis;
		src += "\r\n</SessionTimeout>\r\n\r\n";
		
		src += "<CommTimeout>\r\n   ";
		src += commTimeoutMillis;
		src += "\r\n</CommTimeout>\r\n\r\n";

		src += "<ServerHost>\r\n   ";
		src += serverHostname;
		src += "\r\n</ServerHost>\r\n\r\n";

		src += "<DeviceCommType>\r\n   ";
		src += deviceCommType;
		src += "\r\n</DeviceCommType>\r\n\r\n";
		
		src += "<CommPort>\r\n   ";
		src += commPort;
		src += "\r\n</CommPort>\r\n\r\n";
		
		src += "<CommBaud>\r\n   ";
		src += commBaudRate;
		src += "\r\n</CommBaud>\r\n\r\n";
		
		src += "<VelocityUnit>\r\n   ";
		src += velocityUnit;
		src += "\r\n</VelocityUnit>\r\n\r\n";
		
		src += "<DistanceUnit>\r\n   ";
		src += distanceUnit;
		src += "\r\n</DistanceUnit>\r\n\r\n";
		
		src += "</DCMonSettings>";
		
		try {
	        BufferedWriter out = new BufferedWriter(new FileWriter(XML_SOURCE));
	        out.write(src);
	        out.close();
	    } catch (IOException e) {
	    	System.out.println("SettingsManager: Error saving settings to file " + XML_SOURCE + e.getLocalizedMessage());
	    }

	}
	
	
}
