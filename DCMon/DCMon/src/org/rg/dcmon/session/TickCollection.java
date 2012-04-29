/**
 * @(#) TickCollection.java 
 *      Ryan Green 2006
 */

package org.rg.dcmon.session;

import java.util.*;

public class TickCollection {//extends Vector{
	private Vector ticks;
	
	public TickCollection(){
		ticks = new Vector();
	}
	
	/**
	 * @param tick
	 */
	public void addTick(Tick tick){
		ticks.add(tick);
	}
	
	public void clear(){
		ticks.clear();
	}
	
	public int count(){
		return ticks.size();
	}
	
	public Tick getTick(int index){
		return (Tick)ticks.elementAt(index);
	}
}
