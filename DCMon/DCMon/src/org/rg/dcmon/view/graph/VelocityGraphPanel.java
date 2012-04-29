/**
 * @(#) VelocityGraphPanel.java 
 *      Ryan Green 2006
 */

package org.rg.dcmon.view.graph;
import org.jfree.data.time.Millisecond;
import org.rg.dcmon.session.*;
import org.rg.dcmon.monitor.*;
import org.rg.dcmon.view.GraphPanel;

public class VelocityGraphPanel extends GraphPanel{
	private double lastDistance;
	private int minIntervalTicks;
	private int velUpdateTime = 2000; 
	private double lastVel;
	private long lastRecalcTime;
	int d;
	
	public VelocityGraphPanel(){
		super("Time", "Velocity (" + Session.getVelocityUnitString() + ") " , "");
		minWaitTime = 1000;	//override
		minIntervalTicks = 2; 
	}
	
	public void refresh(Session session){
		long now = System.currentTimeMillis();
		long dt = (now - lastRefreshTime);
		long dtRecalc = now - lastRecalcTime;
		int thisDist = session.calculateTotalDistance();
		
		if (dt > minWaitTime)
		{
			double v, vScaled;
						
			//if dt > velUpdateTime, recalc vel
			//else update graph with last vel
			//smooths out graph
			if (dtRecalc > velUpdateTime)
			{
				 v = (thisDist - lastDistance) / dtRecalc;
				 vScaled = v * Session.calculateVelocityScaleFactor();
				 
				 lastRecalcTime = System.currentTimeMillis();
				 lastDistance = session.calculateTotalDistance();
			}
			else 
			{
				vScaled = lastVel;
			}
				
			timeseries.addOrUpdate(new Millisecond(), vScaled);
			
			lastRefreshTime = System.currentTimeMillis();
			lastVel = vScaled;
		}
	}
}
