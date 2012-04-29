/**
 * @(#) IDevice.java 
 *      Ryan Green 2006
 */

package org.rg.dcmon.device;

public interface IDevice{
	/**
	 * @param c
	 */
	void send(char c);
	
	/**
	 * @param str
	 */
	void send(String str);
	
	/**
	 * @return 
	 */
	char receive();
	
	/**
	 * @return 
	 */
	int numCharsAvailable();
	
	/**
	 * @return 
	 */
	boolean init();
	
	void close();
	
}
