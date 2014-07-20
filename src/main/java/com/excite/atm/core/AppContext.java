package com.excite.atm.core;

import java.util.Properties;

/**
 * Class for storing application context data and certain properties
 * This data is made available in the thread using ThreadLocal
 * 
 * @see com.excite.atm.core.ATM
 * 
 * @author James David
 *
 */
public interface  AppContext {	
	
	public void setAppProperties(Properties prop);
	
	public Properties getAppProperties();

}
