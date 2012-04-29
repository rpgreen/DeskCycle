/**
 * @(#) MonitorController.java 
 *      Ryan Green 2006
 */

package org.rg.dcmon.monitor;

import org.rg.dcmon.device.DeviceController;
import org.rg.dcmon.dcweb.IDCDB;
import org.rg.dcmon.view.View;
import org.rg.dcmon.session.*;
import org.rg.dcmon.dcweb.*;
import java.util.Date;

public class MonitorController implements IMonitor, Runnable {
	public static String DEBUG_KEY = "00000000000000000000";
	public static int DEBUG_TICKSCALE = 120;
	public static String DEBUG_USERNAME = "demo";
	
	public static int TIME_QUANT = 200;	//5 updates per second
	private View view;
	private Session currentSession;
	private DeviceController deviceController;
	private MonitorState state;
	private IDCDB dcdb;
	private Thread thread;
	
	public MonitorController() {
		view = new View(this);
		dcdb = new XmlRpcConnection();
		deviceController = new DeviceController(this, dcdb);

		state = MonitorState.MON_STATE_INITDEVICE;

		if (DCMon.TEST_MODE) {
			// default session for debugging
			currentSession = new Session(DEBUG_KEY, DEBUG_TICKSCALE, 
					dcdb.getUserId(DEBUG_USERNAME));
			state = MonitorState.MON_STATE_SESSION;
			view.sessionStarted(currentSession);
		}

		/* start running */
		thread = new Thread(this);
		thread.start();
	}

	/**
	 * @param stateParam
	 */
	void changeState(MonitorState stateParam){
		System.out.println("MonitorController: changeState " + stateParam);
		
		switch (stateParam){
		
		case MON_STATE_INITDEVICE:
			//transition
			break;
		case MON_STATE_OPTIONSACTIVE:
			//transition
			break;
		case MON_STATE_SESSION:
			//if (!DCMon.TEST_MODE){
				currentSession = new Session(deviceController.getKey(), 
					deviceController.getTickScale(), 
					dcdb.getUserId(SettingsManager.getInstance().username));
			//}
			view.sessionStarted(currentSession);
			break;
		case MON_STATE_UPLOAD:
			currentSession.setEndTime(new Date());
			view.sessionEnded();
			break;
		default:
			break;
		}
		state = stateParam;
	}
	
	public void run()
	{
		long startTime, duration = 0;
		while (true){
			startTime = System.currentTimeMillis();
			/*custom code begin*/
			
			switch (state){
			
			case MON_STATE_INITDEVICE:
				if (deviceController.initSession())
				{
					changeState(MonitorState.MON_STATE_SESSION);
				}
				break;
			case MON_STATE_OPTIONSACTIVE:
				//do nothing until options is finished
				break;
			case MON_STATE_SESSION:
				deviceController.checkSession();
				view.refresh(currentSession);
				break;
			case MON_STATE_UPLOAD:
				if (dcdb.validateUser(SettingsManager.getInstance().username, 
						SettingsManager.getInstance().pass))
				{
					if (SettingsManager.getInstance().uploadSession)
					{
						dcdb.uploadSession(currentSession);
					}
				}
				else {
					System.out.println("MonitorController: Error: " +
							"Session data not uploaded. Invalid username/password.");
				}
				changeState(MonitorState.MON_STATE_INITDEVICE);
				break;
			default:
				break;
			}
			
			/*custom code end*/
		    duration = System.currentTimeMillis() - startTime;
		    if (duration < TIME_QUANT){
				try{
					Thread.sleep(TIME_QUANT - duration); //wait until delay time passed
				}catch (Exception ex){}
			}
		}
	}

	public void addTick(){
		currentSession.addTick(new Tick());
		
	}

	public void endSession(){
		changeState(MonitorState.MON_STATE_UPLOAD);
	}
	
	//public Session getSession() {
	//	return currentSession;
	//}

	public void close(){
		deviceController.close();
		System.exit(0);
	}
	
}
