/**
 * @(#) DeviceInitScaleState.java 
 *      Ryan Green 2006
 */

package org.rg.dcmon.device.state;

import org.rg.dcmon.dcweb.IDCDB;
import org.rg.dcmon.device.DeviceController;
import org.rg.dcmon.device.DeviceState;
import org.rg.dcmon.device.IDevice;
import org.rg.dcmon.monitor.*;

public class DeviceInitScaleState extends DeviceState{
	private static DeviceInitScaleState instance;
	
	/**
	 * @return 
	 */
	public static DeviceState getInstance(){
		if (instance == null){
			instance = new DeviceInitScaleState();
		}
		return instance;
	}

	@Override
	public void process(DeviceController devCtrl, IDevice dev, IMonitor mon, IDCDB db) {
		//wait for scale (5 chars)
		while (dev.numCharsAvailable() < 5){
			//wait for input
			try {Thread.sleep(500);} catch(Exception e){}
			//TODO: timeout
		}
		char charReceived = dev.receive();
		String scale = new String();
		
		if (charReceived == DeviceController.CHAR_DEVICE_SCALE)
		{
			//get the key (4 chars)
			for (int i=0; i<4; i++)
			{
				scale += dev.receive();
			}
						
			dev.send(DeviceController.CHAR_MON_SCALE);
			//TODO: check scale within range
			dev.send(DeviceController.CHAR_MON_ACK);
			
			devCtrl.setTickScale(Integer.parseInt(scale));
			
			/* Send # of ticks to init session, session timeout time, and comm timeout time*/
			dev.send(DeviceController.CHAR_MON_TICKS_INIT);
			int ticks = SettingsManager.getInstance().sessionStartTicks;
			String tickStr = convertAndPad(ticks, 2);
			dev.send(tickStr);
			
			dev.send(DeviceController.CHAR_MON_SESS_TIMEOUT);
			long st = SettingsManager.getInstance().sessionTimeoutMillis;
			String stStr = convertAndPad(st, 6);
			dev.send(stStr);
			
			dev.send(DeviceController.CHAR_MON_COMM_TIMEOUT);
			long ct = SettingsManager.getInstance().commTimeoutMillis;
			String ctStr = convertAndPad(ct, 6);
			dev.send(ctStr);
			
			devCtrl.changeState(DeviceSessionState.getInstance());
		}
		else {
			//dev.send(DeviceController.CHAR_MON_ERROR);
			//devCtrl.changeState(DeviceIdleState.getInstance());
		}
		
	}
	
	private String convertAndPad(long i, int len)
	{
		//convert to string, pad with 0's
		String tickStr = Long.toString(i);
		while (tickStr.length() < len)
		{
			tickStr = '0' + tickStr;
		}
		return tickStr;
	}

	
	
}
