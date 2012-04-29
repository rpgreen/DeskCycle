/**
 * @(#) DataPanel.java 
 *      Ryan Green 2006
 */

package org.rg.dcmon.view;

import java.awt.*;
import java.awt.Dimension;
import java.text.*;
import org.jfree.data.time.Millisecond;
import org.rg.dcmon.monitor.SettingsManager;
import org.rg.dcmon.session.*;
import javax.swing.*;
import java.util.Date;

public class DataPanel extends JPanel {
	protected int width = 300;
	protected int height = 150;
	protected int minWaitTime = SettingsManager.getInstance().updateInterval;
	protected long lastRefreshTime;
	
	private JLabel userLabel;
	private JLabel distLabel;
	private JLabel vLabel;
	private JLabel avLabel;
	private JLabel stLabel;
	private JLabel etLabel;
	
	private String userLabelStr = "Username:   ";
	private String distLabelStr = "Sesion Distance:   ";
	private String vLabelStr = "Current Velocity:   ";
	private String avLabelStr = "Session Velocity:   ";
	private String stLabelStr = "Session Start Time:    ";
	private String etLabelStr = "Elapsed Time:   ";
	
	public DataPanel(){
		super(null,true);
		init();
	}
		
	public void init()
	{   	
		//this.setBackground(Color.LIGHT_GRAY);
		this.setSize(new Dimension(width, height));
		this.setMinimumSize(new Dimension(width, height));
		this.setPreferredSize(new Dimension(width, height));
		this.setVisible(true);
		this.setOpaque(true);
		GridLayout layout =  new GridLayout(6,1);
		layout.setHgap(5);
		layout.setVgap(5);
		//layout.s
		setLayout(layout);
		
		userLabel = new JLabel("Username: ");
		distLabel = new JLabel("Sesion Distance: ");
		vLabel = new JLabel("Current Velocity: ");
		avLabel = new JLabel("Session Velocity: ");
		stLabel = new JLabel("Session Start Time : ");
		etLabel = new JLabel("Elapsed Time: ");
		
		Font font = new Font("Arial", Font.PLAIN, 11);
		userLabel.setFont(font);
		distLabel.setFont(font);
		vLabel.setFont(font);
		avLabel.setFont(font);
		stLabel.setFont(font);
		etLabel.setFont(font);
		
		add(userLabel);
		add(stLabel);
		add(etLabel);
		add(distLabel);
		//add(vLabel);		//not a good one to have here
		add(avLabel);
	}

	public void refresh(Session session)
	{	
		long now = System.currentTimeMillis();
		long dt = (now - lastRefreshTime);
		if (dt > minWaitTime){
			DecimalFormat d = new DecimalFormat("0.00");
			
			userLabel.setText(userLabelStr + SettingsManager.getInstance().username);
			stLabel.setText(stLabelStr + session.getStartTime().toString());
			
			long elapsed = (new Date().getTime()) - session.getStartTime().getTime();
			etLabel.setText(etLabelStr + Session.calcHMS(elapsed));
			
			double totalDist = session.calculateTotalDistance() * 
				Session.calculateDistanceScaleFactor();
			distLabel.setText(distLabelStr + d.format(totalDist) + Session.getDistanceUnitString());
			
			double v = (double)(session.getTickScale() *
					session.calculateTicksSinceTime(lastRefreshTime)) / (double)dt; 	//cm / ms
			double vScaled = v * Session.calculateVelocityScaleFactor();
			//vLabel.setText(vLabelStr + d.format(vScaled) + Session.getVelocityUnitString());
			
			avLabel.setText(avLabelStr + d.format(session.calculateAverageVelocity()*Session.calculateVelocityScaleFactor()) 
					+ Session.getVelocityUnitString());

			lastRefreshTime = System.currentTimeMillis();
		}

	}
	

}
