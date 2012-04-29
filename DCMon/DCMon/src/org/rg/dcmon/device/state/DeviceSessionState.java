/**
 * @(#) DeviceSessionState.java 
 *      Ryan Green 2006
 */

package org.rg.dcmon.device.state;

import org.rg.dcmon.dcweb.IDCDB;
import org.rg.dcmon.device.DeviceController;
import org.rg.dcmon.device.DeviceState;
import org.rg.dcmon.device.IDevice;
import org.rg.dcmon.monitor.IMonitor;

public class DeviceSessionState extends DeviceState{
	private static DeviceSessionState instance;
	
	/**
	 * @return 
	 */
	public static DeviceState getInstance(){
		if (instance == null){
			instance = new DeviceSessionState();
		}
		return instance;
	}

	@Override
	public void process(DeviceController devCtrl, IDevice dev, IMonitor mon, IDCDB db) {
		//check for ticks or quit session messages
		while (dev.numCharsAvailable() > 0)
		{
			switch (dev.receive()){
			case DeviceController.CHAR_DEVICE_TICK:
				mon.addTick();
				dev.send(DeviceController.CHAR_MON_TICK);
				break;
			case DeviceController.CHAR_DEVICE_QUIT:
				dev.send(DeviceController.CHAR_MON_QUIT);
				mon.endSession();
				return;
			default:
				//dev.send(DeviceController.CHAR_MON_ERROR);
				//devCtrl.changeState(DeviceIdleState.getInstance());
				break;
			}
		}
	}
	
}
