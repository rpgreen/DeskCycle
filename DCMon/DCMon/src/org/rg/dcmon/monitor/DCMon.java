/**
 * @(#) DCMon.java 
 *      Ryan Green 2006
 */

package org.rg.dcmon.monitor;

public class DCMon{
	public static boolean TEST_MODE = false;
	public static final String APP_TITLE = "DCMon";
	private MonitorController monitorController;
	
	public static void main(String[] args) 
	{
		if (args.length > 0){
			if (args[0].equals("-testmode")){
				//System.out.println("** Operating in Test Mode **");
				TEST_MODE = true;
			}
		}
		new MonitorController();
	}
		
}
