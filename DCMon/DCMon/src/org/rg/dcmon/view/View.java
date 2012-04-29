/**
 * @(#) View.java 
 *      Ryan Green 2006
 */

package org.rg.dcmon.view;

import org.rg.dcmon.monitor.MonitorController;
import javax.swing.*;
import org.rg.dcmon.monitor.*;
import org.rg.dcmon.session.*;
import org.rg.dcmon.view.graph.*;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;
import net.infonode.docking.*;
import net.infonode.docking.util.*;
import net.infonode.docking.theme.*;

public class View implements ActionListener, ItemListener, ViewSerializer
{
	public static final int APP_WIDTH = 800;
	public static final int APP_HEIGHT = 600;
	private int WIDTH_COMPACT = 400;
	private int HEIGHT_COMPACT = 200;
	private int WIDTH_SESSION = 625;//320;
	private int HEIGHT_SESSION = 470;//800;
	private MonitorController monitorController;
	//private GraphPanel graphPanel1;
	//private GraphPanel graphPanel2;
	private OptionsPanel optionsPanel;
	//private DataPanel dataPanel;
	private Console console;
	private JFrame primaryFrame;
	private JMenuBar menuBar;
	private Vector components;
	private JLabel waitingLabel;
	private net.infonode.docking.View[] views;
	private DockingWindow dataWindow;
	
	public View(MonitorController mc) 
	{
		monitorController = mc;
		console = new Console();
		console.frame.setVisible(DCMon.TEST_MODE);
		components = new Vector();
		init();
	}
	
	public void init() 
	{
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			System.out.println("View: Error setting native LAF: " + e);
		}
		
		primaryFrame = new JFrame(DCMon.APP_TITLE + " - Idle");
		primaryFrame.setSize(new Dimension(WIDTH_COMPACT, HEIGHT_COMPACT));
		primaryFrame.setLocationByPlatform(true);
		
		buildMenu();
		primaryFrame.setVisible(true);
		//primaryFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		//JPanel container = new JPanel(new FlowLayout(FlowLayout.LEFT));
		RootWindow container = new RootWindow(this);
		container.setBackground(Color.LIGHT_GRAY);
		DockingWindowsTheme theme = new ShapedGradientDockingTheme();
		container.getRootWindowProperties().addSuperObject(
		  theme.getRootWindowProperties());
		primaryFrame.setContentPane(container);
		//primaryFrame.setResizable(false);
		
		setWaitMode();
	}
	
	private void setWaitMode()
	{
		primaryFrame.setTitle(DCMon.APP_TITLE + " - Idle");
		waitingLabel = new JLabel("Waiting for Device...");
		RootWindow r = (RootWindow)primaryFrame.getContentPane();
		r.setWindow(new net.infonode.docking.View("DCMon Idle",null, waitingLabel));
		primaryFrame.setSize(new Dimension(WIDTH_COMPACT, HEIGHT_COMPACT));
	}
	
	private void addComponent(Component c)
	{
		//primaryFrame.getContentPane().add(c);
		components.add(c);
	}
	
	private void removeComponent(Component c)
	{
		primaryFrame.getContentPane().remove(c);
		components.remove(c);
	}
	
	private void buildMenu() 
	{
		menuBar = new JMenuBar();
		
		JMenu menu = new JMenu("File");
		menu.setMnemonic(KeyEvent.VK_F);
		menu.getAccessibleContext().setAccessibleDescription("File");
		
		JMenuItem menuItem = new JMenuItem("Exit", KeyEvent.VK_T);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E,
				ActionEvent.ALT_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription("Exit");
		menuItem.addActionListener(this);
		menu.add(menuItem);
		
		JMenu menu2 = new JMenu("Tools");
		menu2.setMnemonic(KeyEvent.VK_T);
		menu2.getAccessibleContext().setAccessibleDescription("Tools");
		JMenuItem menuItem2 = new JMenuItem("Options...", KeyEvent.VK_O);
		menuItem2.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,
				ActionEvent.ALT_MASK));
		menuItem2.getAccessibleContext().setAccessibleDescription("Options...");
		menuItem2.addActionListener(this);
		menu2.add(menuItem2);
		
		JMenu menu3 = new JMenu("Window");
		menu3.setMnemonic(KeyEvent.VK_W);
		menu3.getAccessibleContext().setAccessibleDescription("Window");
		JMenuItem menuItem3 = new JCheckBoxMenuItem("Show Console", DCMon.TEST_MODE);
		//menuItem3.set
		menuItem3.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
				ActionEvent.ALT_MASK));
		menuItem3.getAccessibleContext().setAccessibleDescription("Options...");
		menuItem3.addActionListener(this);
		menuItem3.addItemListener(this);
		menu3.add(menuItem3);
		
		JMenu menu4 = new JMenu("Help");
		menu4.setMnemonic(KeyEvent.VK_H);
		menu4.getAccessibleContext().setAccessibleDescription("Help");
		JMenuItem menuItem4 = new JMenuItem("About...", KeyEvent.VK_A);
		menuItem4.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A,
				ActionEvent.ALT_MASK));
		menuItem4.getAccessibleContext().setAccessibleDescription("About...");
		menuItem4.addActionListener(this);
		menu4.add(menuItem4);
		
		menuBar.add(menu);
		menuBar.add(menu2);
		menuBar.add(menu3);
		menuBar.add(menu4);
		
		primaryFrame.setJMenuBar(menuBar);
	}
	
	public void refresh(Session session) 
	{
		for (int i=0; i<components.size(); i++){
			if (components.elementAt(i) instanceof GraphPanel){
				GraphPanel gp = (GraphPanel) components.elementAt(i);
				gp.refresh(session);
			}
			if (components.elementAt(i) instanceof DataPanel){
				DataPanel dp = (DataPanel) components.elementAt(i);
				dp.refresh(session);
			}
		}
	}
	
	public void close() 
	{
		primaryFrame.dispose();
		monitorController.close();
	}
	
	public void itemStateChanged(ItemEvent arg0) 
	{
		JMenuItem source = (JMenuItem) (arg0.getSource());
		//System.out.println(source.getText());
		if (source.getText().equals("Show Console")) {
			if (arg0.getStateChange() == ItemEvent.DESELECTED)
			{
				console.frame.setVisible(false);
			}
			else {
				console.frame.setVisible(true);
			}
		}
	}
	
	public void actionPerformed(ActionEvent arg0) 
	{
		JMenuItem source = (JMenuItem) (arg0.getSource());
		//System.out.println(source.getText());
		if (source.getText().equals("Exit")) {
			close();
		}
		if (source.getText().equals("Options...")) {
			showOptionsPanel();
		}
		if (source.getText().equals("About...")) {
			showAboutPanel();
		}
	}
	
	public void sessionStarted(Session session)
	{
		primaryFrame.setTitle(DCMon.APP_TITLE + " - Session Active");
		removeComponent(waitingLabel);
		
		primaryFrame.setSize(new Dimension(
				WIDTH_SESSION, HEIGHT_SESSION));
		
		initPanels(session);
		
		//primaryFrame.pack();
	}
	
	private void initPanels(Session session)
	{
		VelocityGraphPanel v = new VelocityGraphPanel();
		AverageVelocityGraphPanel av = new AverageVelocityGraphPanel();
		DistanceGraphPanel d = new DistanceGraphPanel();
		DataPanel dp = new DataPanel();
		
		addComponent(v);
		addComponent(av);
		addComponent(d);
		addComponent(dp);
		
		v.initGraph(session);
		av.initGraph(session);
		d.initGraph(session);
		
		RootWindow r = (RootWindow)primaryFrame.getContentPane();
		
		//reset panels on first session, otherwise keep them where they are
		//if (views == null)
		//{
			views = 
				new net.infonode.docking.View[4];
				
			views[0] = new net.infonode.docking.View("Velocity Graph", null, v);
			views[1] = new net.infonode.docking.View("Average Velocity Graph", null, av);    
			views[2] = new net.infonode.docking.View("Distance Graph", null, d);
			views[3] = new net.infonode.docking.View("Data Panel", null, dp);

			SplitWindow s1 = new SplitWindow(true, views[0], views[1]);
			SplitWindow s2 = new SplitWindow(true, views[2], views[3]);
			
			dataWindow = new SplitWindow(false, s1, s2);
		//}
		
		r.setWindow(dataWindow);
		
		r.revalidate();
		r.repaint();
	}
	
	public void sessionEnded()
	{
		for (int i=0; i<components.size(); i++){
			if (components.elementAt(i) instanceof GraphPanel){
				GraphPanel gp = (GraphPanel) components.elementAt(i);
				primaryFrame.getContentPane().remove(gp);
			}
			if (components.elementAt(i) instanceof DataPanel){
				DataPanel dp = (DataPanel) components.elementAt(i);
				primaryFrame.getContentPane().remove(dp);
			}
		}
		components.clear();
		RootWindow r = (RootWindow)primaryFrame.getContentPane();
		r.getWindow().removeAll();
		setWaitMode();
	}
	
	private void showAboutPanel()
	{
		JFrame frame = new JFrame("About");
		frame.setLocationByPlatform(true);
		frame.setVisible(true);
		frame.setSize(300,200);
		JPanel p = new JPanel();

		BoxLayout b = new BoxLayout(p, BoxLayout.Y_AXIS);
		p.setLayout(b);
		//quick/dirty
		p.add(new JLabel("                          "));
		p.add(new JLabel("                          "));
		p.add(new JLabel("                          "));
		p.add(new JLabel("                           DCMon 1.0         "));
		p.add(new JLabel("                           Ryan Green        "));
		p.add(new JLabel("                           ryang@engr.mun.ca "));
		frame.setContentPane(p);
		
		p.revalidate();
		p.repaint();
		
	}
	
	private void showOptionsPanel()
	{
		JFrame frame = new JFrame("Options");
		frame.setLocationByPlatform(true);
		frame.setVisible(true);
		frame.setSize(OptionsPanel.WIDTH, OptionsPanel.HEIGHT);
		OptionsPanel p = new OptionsPanel(frame);
		
		//p.validate();
		
		frame.setContentPane(p);
		
		p.validate();
	}
	
	
	public net.infonode.docking.View readView(ObjectInputStream arg0) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	public void writeView(net.infonode.docking.View arg0, ObjectOutputStream arg1) throws IOException {
		// TODO Auto-generated method stub
	}
	
}
