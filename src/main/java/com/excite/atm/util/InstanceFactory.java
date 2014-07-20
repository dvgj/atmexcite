package com.excite.atm.util;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import com.excite.atm.core.ATM;
import com.excite.atm.core.AppContext;
import com.excite.atm.impl.AppContextImpl;
import com.excite.atm.impl.Constants;


/**
 * 
 * This class is used for instantiating the logging system
 * load application properties, implementation classes and
 * instantiating the implementation class.
 * 
 * @author James David
 *
 */
public class InstanceFactory {
	private static Logger log = null;
	
	private static Properties implConfig = null;
    /* run the initialization methods */
	static {
		init();
	}

	/* initialize the logger,
	 * load the application properties
	 * based on success of earlier step, load the implementations
	 * 
	 * if initialization fails, terminate the application.
	 */
	private static void init() {
		boolean initSuccess = true;
		initLogger();
		initSuccess = loadAppProperties();

		if (initSuccess) {
			initSuccess = loadImplConfigs();
		}

		if (!initSuccess) {
			System.err.println("Unable to initialize system");
			System.exit(1);
		}

	}
	/*
	 * initialize the logging system.
	 */
	private static boolean initLogger() {
		try {
			
			LogManager.getLogManager().readConfiguration(InstanceFactory.class.getResourceAsStream(Constants.FN_LOG));
			
			log = Logger.getLogger(InstanceFactory.class.getName());
			log.log(Level.INFO, "Logging system initialized");
			return true;
		} catch (Exception e) {	
			e.printStackTrace(System.err);
			return false;
		} 
	}
	
	/*
	 * load the application properties. 
	 */
	private static boolean loadAppProperties() {
		
		AppContext appContext = (AppContext) ATM.context.get();
		if (appContext == null) {
			Properties appProperties = (Properties) loadProperties(Constants.FN_APPPROPERTIES);
			appContext = new AppContextImpl();
			appContext.setAppProperties(appProperties);
			ATM.context.set(appContext);
		}
		return (appContext == null) ? false : true;
		
	}
	/*
	 * utility method to load a specific property file.
	 */
	private static Properties loadProperties(String propFileName) {
		
		try {
			Properties prop = new Properties();
			prop.load(InstanceFactory.class.getResourceAsStream(propFileName));
			return prop;
		} catch (IOException e) {
			log.log(Level.SEVERE, "Unable to load " + propFileName, e);
		}
		return null;
		
	}
	
	/*
	 * method to load the implementations from property file. 
	 */
	private static boolean loadImplConfigs() {
		
		if (implConfig == null) {
			AppContext appContext = (AppContext) ATM.context.get();
			String appPropFileName = appContext.getAppProperties().getProperty(Constants.FN_IMPLCONFIG_APP_PROP_KEY);
			implConfig = loadProperties(appPropFileName);
		}
		return (implConfig == null) ? false : true;
		
	}
	/**
	 * Based on the configurations, instantiate a appropriate
	 * <code>ATM</code> implementation class.
	 * 
	 * @return
	 * @throws Exception
	 */
	public static ATM getATMInstance() throws Exception {
		
		String atmImplClassName = implConfig.getProperty(Constants.ATM);
		ATM atm = (ATM) getInstance(atmImplClassName);
		return atm;
		
	}
	
		
	/*
	 * utility method to create a instance of a class 
	 * using reflection.
	 * 
	 * this method does not use the constructor.
	 * 
	 * requires a getInstance method on the target class
	 * and leaves the instantiation mechanism to the 
	 * target class.  
	 *
	 */
	private static Object getInstance(String implClassName) throws Exception {
		
		try {
			Class<?> clz = Class.forName(implClassName);

			Method getInstance = clz
					.getMethod("getInstance", (Class<?>[]) null);

			Object instance = getInstance.invoke(clz, (Object[]) null);

			return instance;

		} catch (Exception e) {
			log.log(Level.SEVERE, "Unable to create instance for "
					+ implClassName, e);
			throw e;
		}
		
	}
}
