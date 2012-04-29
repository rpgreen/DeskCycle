/**
 * @(#) DeviceState.java 
 *      Ryan Green 2006
 */

package org.rg.dcmon.device;

import org.rg.dcmon.dcweb.IDCDB;
import org.rg.dcmon.monitor.IMonitor;

public abstract class DeviceState{

	public abstract void process(DeviceController devCtrl, IDevice dev, IMonitor mon, IDCDB db);
		
}
