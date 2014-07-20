package com.excite.atm.impl;

import java.util.Properties;

import com.excite.atm.core.AppContext;

/**
 * 
 * Implementation for the <code>AppContext</code provided
 * as part of the interfaces.
 * 
 * Contains data loaded from appl_config.properties, as well as
 * certain calculated values to serve as a interim cache for the
 * application.
 * 
 * The instance of this classes is available inside the <code>ThreadLocal</code>
 * of the executing thread.
 * 
 * 
 * @author James David
 *
 */
public class AppContextImpl implements AppContext {
	/** stores the proerties read from file as well as data stored by the application as interim cache */
	private Properties appProperties;


	/**
	 * @see com.excite.atm.core.AppContext#getAppProperties()
	 */
	public Properties getAppProperties() {
		return appProperties;
	}

	/**
	 * @see com.excite.atm.core.AppContext#setAppProperties(Properties)
	 */
	public void setAppProperties(Properties appProperties) {
		this.appProperties = appProperties;
	}
	
}
