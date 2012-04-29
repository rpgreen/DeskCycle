/**
 * @(#) IDCDB.java 
 *      Ryan Green 2006
 */

package org.rg.dcmon.dcweb;

import org.rg.dcmon.session.Session;

public interface IDCDB{
	/**
	 * @param session
	 * @return 
	 */
	boolean uploadSession(Session session);
	
	/**
	 * @param username
	 * @param pass
	 * @return 
	 */
	boolean validateUser(String username, String pass);
	
	/**
	 * @param key
	 * @return 
	 */
	boolean validateDeviceKey(String key);
	
	/**
	 * @param username
	 * @return 
	 */
	int getUserId(String username);
	
	
}
