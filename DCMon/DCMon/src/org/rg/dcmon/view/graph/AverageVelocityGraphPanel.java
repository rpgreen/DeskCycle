package org.rg.dcmon.view.graph;

import java.text.DecimalFormat;

import org.jfree.data.time.Millisecond;
import org.rg.dcmon.session.Session;
import org.rg.dcmon.view.GraphPanel;
import org.rg.dcmon.view.GraphPanel.RelativeDateFormat;

public class AverageVelocityGraphPanel extends GraphPanel {

	public AverageVelocityGraphPanel(){
		super("Time", "Average Velocity (" + Session.getVelocityUnitString() + ") " , "");
	}
	
	public void refresh(Session session){
		long now = System.currentTimeMillis();
		long dt = (now - lastRefreshTime);
		if (dt > minWaitTime){
			double v = session.calculateAverageVelocity();	//cm/ms
			double vScaled = v * Session.calculateVelocityScaleFactor();
			timeseries.addOrUpdate(new Millisecond(), vScaled);
			lastRefreshTime = System.currentTimeMillis();
		}
	}

}
