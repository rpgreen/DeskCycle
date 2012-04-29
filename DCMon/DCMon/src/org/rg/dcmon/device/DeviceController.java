/**
 * @(#) DeviceController.java 
 *      Ryan Green 2006
 */

package org.rg.dcmon.device;

import org.rg.dcmon.monitor.*;
import org.rg.dcmon.dcweb.*;
import org.rg.dcmon.device.state.*;

public class DeviceController{
	public static final char CHAR_MON_ACK = '1';
	public static final char CHAR_MON_REJ = '0';
	public static final char CHAR_DEVICE_INIT = 'I';
	public static final char CHAR_MON_INIT = 'i';
	public static final char CHAR_DEVICE_KEY= 'K';
	public static final char CHAR_MON_KEY = 'k';
	public static final char CHAR_DEVICE_SCALE = 'S';
	public static final char CHAR_MON_SCALE = 's';
	public static final char CHAR_MON_TICKS_INIT = 'r';
	public static final char CHAR_MON_SESS_TIMEOUT = 'u';
	public static final char CHAR_MON_COMM_TIMEOUT = 'm';
	public static final char CHAR_DEVICE_TICK = 'T';
	public static final char CHAR_MON_TICK = 't';
	public static final char CHAR_DEVICE_QUIT = 'Q';
	public static final char CHAR_MON_QUIT = 'q';
	public static final char CHAR_MON_ERROR = 'e';
	
	private IDevice device;
	private DeviceState state;
	private IMonitor monitorController;
	private IDCDB dcdb;
	private String key;
	private int tickScale;
	
	public DeviceController(IMonitor mc, IDCDB dcdbParam){
		monitorController = mc;
		dcdb = dcdbParam;
		
		//TODO: having this in constructor is going to require restart after comm settings change
		DeviceCommType d = SettingsManager.getInstance().deviceCommType;
		if (d == DeviceCommType.DEVICE_SERIAL){
			device = new SerialDevice();
		}
		else if (d == DeviceCommType.DEVICE_NET){
			device = new InternetDevice();
		}
		else if (d == DeviceCommType.DEVICE_KEYBOARD){
			device = new KeyboardTestDevice();
		}
		else {
			device = new SerialDevice();
		}
				
		state = DeviceIdleState.getInstance();
		
		if (DCMon.TEST_MODE){
			state = DeviceSessionState.getInstance();
			device = new KeyboardTestDevice();
		}
		
		if (device.init())
		{
			System.out.println("DeviceController: Device Initialized on " + SettingsManager.getInstance().commPort);
			//send a q to ensure device is in idle state
			device.send(CHAR_MON_QUIT);
		}
		else {
			System.out.println("Device Not Initialized. Restart required.");
		}
	}
	
	/**
	 * @param state
	 */
	public void changeState(DeviceState stateParam){
		System.out.println("DeviceController: Changing State " + stateParam);
		state = stateParam;
	}
	
	/**
	 * Wait for a session initialization from the device and go through the initialization states.
	 * @return true if session was initialized
	 */
	public boolean initSession(){
		state = DeviceIdleState.getInstance();
		state.process(this, device, monitorController, dcdb);
		//state changed
		if (state instanceof DeviceInitKeyState){
			state.process(this, device, monitorController, dcdb);
		} else return false;
		//state changed
		if (state instanceof DeviceInitScaleState){
			state.process(this, device, monitorController, dcdb);
		} else return false;
		//state changed
		if (state instanceof DeviceSessionState){
			return true;
		} else return false;
	}
	
	public boolean checkSession(){
		if (state instanceof DeviceSessionState){
			state.process(this, device, monitorController, dcdb);
			return true;
		} else return false;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String keyParam) {
		key = keyParam;
	}
	
	public int getTickScale() {
		return tickScale;
	}

	public void setTickScale(int tickScale) {
		this.tickScale = tickScale;
	}

	public void close() {
		device.close();
	}
	
}
