/**
 * @(#) DeviceInitKeyState.java 
 *      Ryan Green 2006
 */

package org.rg.dcmon.device.state;

import org.rg.dcmon.dcweb.IDCDB;
import org.rg.dcmon.device.DeviceController;
import org.rg.dcmon.device.DeviceState;
import org.rg.dcmon.device.IDevice;
import org.rg.dcmon.monitor.IMonitor;

public class DeviceInitKeyState extends DeviceState{
	private static DeviceInitKeyState instance;
	
	/**
	 * @return 
	 */
	public static DeviceState getInstance(){
		if (instance == null){
			instance = new DeviceInitKeyState();
		}
		return instance;
	}

	@Override
	public void process(DeviceController devCtrl, IDevice dev, IMonitor mon, IDCDB db) {
		//wait for key (21 chars)
		while (dev.numCharsAvailable() < 21){
			//wait for input
			try {Thread.sleep(500);} catch(Exception e){}
			//TODO: timeout
		}
		char charReceived = dev.receive();
		String key = new String();
		if (charReceived == DeviceController.CHAR_DEVICE_KEY)
		{
			//get the key (20 chars)
			for (int i=0; i<20; i++)
			{
				key += dev.receive();
			}
			
			dev.send(DeviceController.CHAR_MON_KEY);
			
			if (db.validateDeviceKey(key))
			{	
				System.out.println("DeviceInitKeyState: Key OK. ");
				
				dev.send(DeviceController.CHAR_MON_ACK);
				devCtrl.setKey(key);
				devCtrl.changeState(DeviceInitScaleState.getInstance());
			}
			else {
				System.out.println("DeviceInitKeyState: Bad Key. ");
				
				dev.send(DeviceController.CHAR_MON_REJ);		
				devCtrl.changeState(DeviceIdleState.getInstance());
			}
		}
		else {
			//dev.send(DeviceController.CHAR_MON_ERROR);
			//devCtrl.changeState(DeviceIdleState.getInstance());
		}
	}


	
	
}
