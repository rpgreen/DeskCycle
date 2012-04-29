/**
 * @(#) DeviceIdleState.java 
 *      Ryan Green 2006
 */

package org.rg.dcmon.device.state;

import org.rg.dcmon.dcweb.IDCDB;
import org.rg.dcmon.device.DeviceController;
import org.rg.dcmon.device.DeviceState;
import org.rg.dcmon.device.IDevice;
import org.rg.dcmon.monitor.IMonitor;

public class DeviceIdleState extends DeviceState{
	private static DeviceIdleState instance;
	
	/**
	 * @return 
	 */
	public static DeviceState getInstance(){
		if (instance == null){
			instance = new DeviceIdleState();
		}
		return instance;
	}

	@Override
	public void process(DeviceController devCtrl, IDevice dev, IMonitor mon, IDCDB db) {
		//wait for init command
		while (dev.numCharsAvailable() == 0)
		{
			try {Thread.sleep(1000);} catch(Exception e){}
		}
		char charReceived = dev.receive();
		if (charReceived == DeviceController.CHAR_DEVICE_INIT)
		{
			dev.send(DeviceController.CHAR_MON_INIT);
			dev.send(DeviceController.CHAR_MON_ACK);
			devCtrl.changeState(DeviceInitKeyState.getInstance());
		}
		else {
			//dev.send(DeviceController.CHAR_MON_ERROR);
			//devCtrl.changeState(DeviceIdleState.getInstance());
		}
		
	}
	
	
}
