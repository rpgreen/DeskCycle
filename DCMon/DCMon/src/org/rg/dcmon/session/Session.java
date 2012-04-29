/**
 * @(#) Session.java 
 *      Ryan Green 2006
 */

package org.rg.dcmon.session;

import java.util.Date;
import org.rg.dcmon.monitor.*;

public class Session{
	public static final int DEFAULT_DATAPOINT_INTERVAL = 5000;	//1 point / 5000 ms
	private int userId;
	private String deviceKey;
	private int tickScale;
	private TickCollection ticks;
	private Date startTime;
	private Date endTime;
	
	public Session(String key, int ts, int user){
		deviceKey = key;
		tickScale = ts;
		userId = user;
		
		ticks = new TickCollection();
 
		startTime = new Date();	
	}
	
		
	/**
	 * @param date
	 */
	public void setEndTime(Date date){
		endTime = date;
	}
	
	/**
	 * @return 
	 */
	public int getTickScale(){
		return tickScale;
	}
	
	/**
	 * @return 
	 */
	public String getDeviceKey(){
		return deviceKey;
	}
	
	/**
	 * @return 
	 */
	public Date getStartTime(){
		return startTime;
	}
	
	/**
	 * @return 
	 */
	public int getUserId(){
		return userId;
	}
	
	/**
	 * @return 
	 */
	public Date getEndTime(){
		return endTime;
	}
	
	/**
	 * @return 
	 */
	public int calculateTotalDistance(){	//cm
		return ticks.count()*tickScale;
	}
	
	/**
	 * @return 
	 */
	public long calculatetElapsedMillis(){
		Date current = new Date();
		return current.getTime() - startTime.getTime();
	}
	
	public int calculateTicksSinceTime(long time){
		int q = 0;
		for (int i=0; i<ticks.count(); i++){
			Tick t = ticks.getTick(i);
			if (t.getTime().getTime() >= time){
				q++;
			}
		}
		return q;
	}
	
	public int calculateTicksInTimeRange(long t1, long t2){
		int q = 0;
		for (int i=0; i<ticks.count(); i++){
			Tick t = ticks.getTick(i);
			long time = t.getTime().getTime();
			if (time >= t1 && time < t2){
				q++;
			}
		}
		return q;
	}
	
	
	/**
	 * @return 
	 */
	public float calculateAverageVelocity(){	//cm/ms
		Date current = new Date();
		long dt = current.getTime() - startTime.getTime();
		
		return (float) ticks.count()* tickScale / dt;
	}
	
	/**
	 * @param tick
	 */
	public void addTick(Tick tick){
		ticks.addTick(tick);
	}
	
	public static double calculateVelocityScaleFactor(){
		VelocityUnit vu = SettingsManager.getInstance().velocityUnit;
		double scaleFactor=1;
		switch (vu){
		//native unit is cm/ms
			case M_S:
				scaleFactor = 10;
				break;
			case CM_S:
				scaleFactor = 1000;
				break;
			case IN_S:	//1cm = 0.3937in
				scaleFactor = 0.3937 * 1000;
				break;
			case FT_S:	//1cm = 0.03281ft
				scaleFactor = 0.03281 * 1000;
				break;
			case KM_H:	//1cm = 0.00001km
				scaleFactor = 36;
				break;
			case MI_H:	//1cm = 0.000006214mi
				scaleFactor = 22.3693629;
				break;
		}
		return scaleFactor;
	}
	
	public static double calculateDistanceScaleFactor(){
		DistanceUnit du = SettingsManager.getInstance().distanceUnit;
		double scaleFactor=1;
		switch (du){
		//native unit is cm
		//M, CM, FT, KM, IN, MI
			case M:
				scaleFactor = 0.01;
				break;
			case CM:
				scaleFactor = 1;
				break;
			case FT:
				scaleFactor = 0.03281;
				break;
			case KM:
				scaleFactor = 0.00001;
				break;
			case IN:
				scaleFactor = 0.3937;
				break;
			case MI:
				scaleFactor = 0.000006214;
				break;
		}	
		return scaleFactor;
	}
	
	
	public static String getVelocityUnitString(){
		VelocityUnit vu = SettingsManager.getInstance().velocityUnit;
		String unitStr;
		
		switch (vu){
			case M_S:
				unitStr = "m/s";
				break;
			case CM_S:
				unitStr = "cm/s";
				break;
			case IN_S:	
				unitStr = "in/s";
				break;
			case FT_S:
				unitStr = "ft/s";
				break;
			case KM_H:	
				unitStr = "km/h";
				break;
			case MI_H:	
				unitStr = "mi/h";
				break;
			default: 
				unitStr = "cm/ms";
				break;
				
		}
		return unitStr;
	}
	
	public static String getDistanceUnitString(){
		DistanceUnit du = SettingsManager.getInstance().distanceUnit;
		String unitStr;
		
		switch (du){
		//native unit is cm
		//M, CM, FT, KM, IN, MI
			case M:
				unitStr = "m";
				break;
			case CM:
				unitStr = "cm";
				break;
			case FT:
				unitStr = "ft";
				break;
			case KM:
				unitStr = "km";
				break;
			case IN:
				unitStr = "in";
				break;
			case MI:
				unitStr = "mi";
				break;
			default: 
				unitStr = "cm";
				break;
		}	
		return unitStr;
	}
	
	public static String calcHMS(long timeInMillis) {
	      long hours, minutes, seconds;
	      timeInMillis /= 1000;
	      hours = timeInMillis / 3600;
	      timeInMillis = timeInMillis - (hours * 3600);
	      minutes = timeInMillis / 60;
	      timeInMillis = timeInMillis - (minutes * 60);
	      seconds = timeInMillis;
	      return "" + hours + "h " + minutes + "m " + seconds + "s "; 
	 }

	
}
