/**
 * @(#) Console.java 
 *      Ryan Green 2006
 */

package org.rg.dcmon.view;

import java.awt.Font;

/**
 * A CtrConsole window to output debugging information.
 */
//
//A simple Java CtrConsole for your application (Swing version)
//Requires Java 1.1.5 or higher
//
//Disclaimer the use of this source is at your own risk. 
//
//Permision to use and distribute into your own applications
//
//RJHM van den Bergh , rvdb@comweb.nl

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.JPanel;

public class Console extends WindowAdapter implements WindowListener, ActionListener, Runnable
{
	public Frame frame;
	private TextArea textArea;
	private Thread reader;
	private Thread reader2;
	private boolean quit;
					
	private final PipedInputStream pin=new PipedInputStream(); 
	private final PipedInputStream pin2=new PipedInputStream(); 

	Thread errorThrower; // just for testing (Throws an Exception at this CtrConsole
	
	public Console()
	{
		// create all components and add them
		frame=new Frame("DCMon Console");
		//frame.setUndecorated(true);
		frame.setBackground(Color.BLACK);
		Dimension screenSize=Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize=new Dimension((int)(screenSize.width/3),(int)(screenSize.height/2));
		int x=(int)(frameSize.width/2);
		int y=(int)(frameSize.height/2);
		frame.setBounds(x,y,frameSize.width,frameSize.height);
		frame.setLocation(screenSize.width - 4*x, screenSize.height/3);
		
		JPanel panel = new JPanel(null, true);
		panel.setOpaque(true);
		panel.setBackground(Color.BLACK);
		panel.setForeground(Color.WHITE);
		
		panel.setLayout(new BorderLayout());
		textArea=new TextArea();
		textArea.setEditable(false);
		textArea.setBackground(Color.BLACK);
		textArea.setForeground(Color.WHITE);
		Button button=new Button("clear");
		panel.add(textArea,BorderLayout.CENTER);
		panel.add(button,BorderLayout.SOUTH);
		frame.add(panel);
		
		panel.setVisible(true);
		frame.setVisible(true);		

		
		frame.addWindowListener(this);		
		button.addActionListener(this);
		
		frame.setAlwaysOnTop(false);
		
		try
		{
			PipedOutputStream pout=new PipedOutputStream(this.pin);
			System.setOut(new PrintStream(pout,true)); 
		} 
		catch (java.io.IOException io)
		{
			textArea.append("Couldn't redirect STDOUT to this CtrConsole\n"+io.getMessage());
		}
		catch (SecurityException se)
		{
			textArea.append("Couldn't redirect STDOUT to this CtrConsole\n"+se.getMessage());
	    } 
		
		try 
		{
			PipedOutputStream pout2=new PipedOutputStream(this.pin2);
			System.setErr(new PrintStream(pout2,true));
		} 
		catch (java.io.IOException io)
		{
			textArea.append("Couldn't redirect STDERR to this CtrConsole\n"+io.getMessage());
		}
		catch (SecurityException se)
		{
			textArea.append("Couldn't redirect STDERR to this CtrConsole\n"+se.getMessage());
	    } 		
			
		quit=false; // signals the Threads that they should exit
				
		// Starting two seperate threads to read from the PipedInputStreams				
		//
		reader=new Thread(this);
		reader.setDaemon(true);	
		reader.start();	
		//
		reader2=new Thread(this);	
		reader2.setDaemon(true);	
		reader2.start();
							
	}
	
	public synchronized void windowClosed(WindowEvent evt)
	{
		quit=true;
		this.notifyAll(); // stop all threads
		try { reader.join(1000);pin.close();   } catch (Exception e){}		
		try { reader2.join(1000);pin2.close(); } catch (Exception e){}
		System.exit(0);
	}		
		
	public synchronized void windowClosing(WindowEvent evt)
	{
		frame.setVisible(false); // default behaviour of JFrame	
		frame.dispose();
	}
	
	public synchronized void actionPerformed(ActionEvent evt)
	{
		textArea.setText("");
	}

	public synchronized void run()
	{
		try
		{			
			while (Thread.currentThread()==reader)
			{
				try { this.wait(100);}catch(InterruptedException ie) {}
				if (pin.available()!=0)
				{
					String input=this.readLine(pin);
					//textArea.append(Long.toString(GameTimer.getCurrentTime()) + " ms: ");
					textArea.append(input);
				}
				if (quit) return;
			}
		
			while (Thread.currentThread()==reader2)
			{
				try { this.wait(100);}catch(InterruptedException ie) {}
				if (pin2.available()!=0)
				{
					String input=this.readLine(pin2);
					//textArea.append(Long.toString(GameTimer.getCurrentTime())  + "ms: ");
					textArea.append(input);
				}
				if (quit) return;
			}			
		} catch (Exception e)
		{
			textArea.append("\nCtrConsole reports an Internal error.");
			textArea.append("The error is: "+e);			
		}
		

	}
	
	public synchronized String readLine(PipedInputStream in) throws IOException
	{
		String input="";
		do
		{
			int available=in.available();
			if (available==0) break;
			byte b[]=new byte[available];
			in.read(b);
			input=input+new String(b,0,b.length);														
		}while( !input.endsWith("\n") &&  !input.endsWith("\r\n") && !quit);
		return input;
	}	
		
}