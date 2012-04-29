package org.rg.dcmon.view.graph;


import org.jfree.data.time.Millisecond;
import org.rg.dcmon.session.*;
import org.rg.dcmon.view.GraphPanel;

public class DistanceGraphPanel extends GraphPanel{
	
	public DistanceGraphPanel(){
		super("Time", "Distance (" + Session.getDistanceUnitString() + ") ", "" );
	}
	
	public void refresh(Session session){
		long now = System.currentTimeMillis();
		long dt = (now - lastRefreshTime);
		if (dt > minWaitTime){
			double d = session.calculateTotalDistance();
			double dScaled = d * Session.calculateDistanceScaleFactor();
			timeseries.addOrUpdate(new Millisecond(), dScaled);
			lastRefreshTime = System.currentTimeMillis();
		}
	}
}
