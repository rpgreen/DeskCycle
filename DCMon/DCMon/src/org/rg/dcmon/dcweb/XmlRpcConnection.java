/**
 * @(#) XMLRPCConnection.java 
 *      Ryan Green 2006
 */

package org.rg.dcmon.dcweb;

import org.rg.dcmon.session.Session;

import java.util.Date;
import java.util.Vector;
import java.util.Hashtable;
import org.apache.xmlrpc.client.*;
import org.rg.dcmon.monitor.*;
import java.net.URL;

public class XmlRpcConnection implements IDCDB{
	
	private XmlRpcClient serv;
	private String authUser = "demo";
	private String authPass = "demo";
	
	public XmlRpcConnection() {
		try {
			String host = SettingsManager.getInstance().serverHostname;
			
			XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
			config.setBasicUserName(authUser);
			config.setBasicPassword(authPass);
			config.setServerURL(new URL(host));
			serv = new XmlRpcClient();
			serv.setTransportFactory(new XmlRpcCommonsTransportFactory(serv));
			serv.setConfig(config);
			config.setEnabledForExtensions(false);
			
			//getUserId("poo");
		}
		catch (Exception e){
			System.out.println("XMLRPCConnection: Error initiating connection. ");
		}
	}
	
	
	public int getUserId(String username) {
		Object[] params = new Object[]{username};
		Integer result = new Integer(0);
		
		try {
			result = (Integer) serv.execute("DCWeb.getUserId", params);
		}
		catch (Exception e)
		{
			System.out.println("XMLRPCConnection: getUserId() error. ");
		}
		return result.intValue();
	}
	
	public boolean uploadSession(Session session) 
	{
		//loop through DEFAULT_DATAPOINT_INTERVAL ms blocks
		//find how many ticks since that iteration and calc velocity for that time quant
		//keep things in native format. cm & ms
		
		int limit = 1000;
		if (session.calculateTotalDistance() < limit)	//dont upload if less than 0.01km 
		{
			return true;	
		}
		
		Vector vPoints = new Vector();
		
		for (long timePointer = session.getStartTime().getTime();	//hot loop
			timePointer <= (session.getEndTime().getTime() - Session.DEFAULT_DATAPOINT_INTERVAL);
			timePointer += Session.DEFAULT_DATAPOINT_INTERVAL)
		{
			int ticks = session.calculateTicksInTimeRange(timePointer, 
					timePointer + Session.DEFAULT_DATAPOINT_INTERVAL);
			//calc velocity in cm/ms
			double vInst = ((double) (ticks * session.getTickScale())) / Session.DEFAULT_DATAPOINT_INTERVAL;
			
			vPoints.add(vInst);
		}
		
		//remove 0 velocities from the end of the session
		/*for (int i=vPoints.size(); i>=0; i--)
		{
			double pt = (Double) vPoints.elementAt(i);
			if (pt == 0)
			{
				
			}
		}*/
		
		//if (vPoints.elementAt(vPoints.size()-1))
		//TODO: maybe dont add the points from the last SettingsManager.getInstance().sessionTimeout  ms
		
		Object[] params = new Object[]{
				session.getUserId(),
				session.getDeviceKey(),
				session.getStartTime(),
				session.getEndTime(),
				Session.DEFAULT_DATAPOINT_INTERVAL,
				session.calculateTotalDistance(),	// cm
				(double)session.calculateAverageVelocity(),	// cm/ms
				vPoints,
		};
		
		Boolean result = new Boolean(false);  
		
		try {
			result = (Boolean) serv.execute("DCWeb.uploadSession", params);
		}
		catch (Exception e)
		{
			System.out.println("XMLRPCConnection: uploadSession() error. " + e.getLocalizedMessage());
		}
		
		return result.booleanValue();
	}
	
	public boolean validateDeviceKey(String key) 
	{
		Object[] params = new Object[]{key};
		Boolean result = new Boolean(false);
		
		try {
			result = (Boolean) serv.execute("DCWeb.validateDeviceKey", params);
		}
		catch (Exception e)
		{
			System.out.println("XMLRPCConnection: validateDeviceKey() error. " + e.getLocalizedMessage());
		}
		return result.booleanValue();
	}
	
	public boolean validateUser(String username, String pass) {
		Object[] params = new Object[]{username, pass};
		Boolean result = new Boolean(false);
		
		try {
			result = (Boolean) serv.execute("DCWeb.validateUser", params);
		}
		catch (Exception e)
		{
			System.out.println("XMLRPCConnection: validateUser() error. " + e.getLocalizedMessage());
		}
		return result.booleanValue();
	}
	
}