package org.rg.dcmon.device;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class KeyboardTestDevice implements IDevice, KeyListener {
	private BufferedReader br;
	private Vector buffer;
	
	public void send(char c) {
		System.out.println("KeyboardTestDevice: " + c);
	}

	public void send(String str) {
		System.out.println("KeyboardTestDevice: " + str);
	}

	public char receive() {
		char c = (Character) buffer.elementAt(0);
		buffer.removeElementAt(0);
		return c;
	}

	public int numCharsAvailable() {
		return buffer.size();
	}

	public boolean init() {
		br = new BufferedReader(new InputStreamReader(System.in));
		buffer = new Vector();
		
		Frame frame = new Frame();
		frame.setTitle("KeyboardTestDevice I/O Panel");
		frame.setVisible(true);
		frame.setSize(new Dimension(200,200));
		Panel p = new Panel();
		p.setSize(new Dimension(200,200));
		p.setVisible(true);
		p.addKeyListener(this);
		frame.add(p);
	
		return true;
	}

	public void keyPressed(KeyEvent arg0) {
		//buffer.add(arg0.getKeyChar());
	}

	public void keyReleased(KeyEvent arg0) {
		
	}

	public void keyTyped(KeyEvent arg0) {
		System.out.println("KeyboardTestDevice: Received " + arg0.getKeyChar());
		buffer.add(arg0.getKeyChar());
	}

	public void close() {
		// TODO Auto-generated method stub
		
	}

}
