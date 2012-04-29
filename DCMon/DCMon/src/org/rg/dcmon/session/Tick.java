/**
 * @(#) Tick.java 
 *      Ryan Green 2006
 */

package org.rg.dcmon.session;

import java.util.Date;

public class Tick{
	private Date datetime;
	
	public Tick()
	{
		datetime = new Date();
	}
	
	public Date getTime(){
		return datetime;
	}
}
