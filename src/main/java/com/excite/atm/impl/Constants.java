package com.excite.atm.impl;

/**
 * Contains certain constant values used by the application.
 * 
 * Naming convention followed :
 *     FN_              File name.
 *     _APP_PROP_KEY    A key to the data available in properties file.
 *     ERR_             Error code prefix.
 *     
 *     
 * @author James David
 *
 */
public class Constants {
	
	/** properties file for initializing/configuring the logging engine */
	public static final String FN_LOG = "/log.properties";
	
	/** properties file for loading certain application specific data */
	public static final String FN_APPPROPERTIES = "/appl_config.properties";
	
	/** implementation classes are configured here.
	 * interface name vs implementation name that should be used.
	 */
	public static final String FN_IMPLCONFIG_APP_PROP_KEY = "pk_impl_config_filename";
	
	/** points to the xml file name where the denominations and their balances 
	 * are maintained.
	 */
	public static final String FN_NOTES_APP_PROP_KEY      = "pk_note_config_filename";
	
	/** constant for ATM */
	public static final String ATM = "ATM";
	
	/** key to supported denominations */
	
	public static final String SUPPORTED_DENOMINATIONS_PROP_KEY = "pk_supported_denominations";
	
	/** key for interim caching of calculated data */
	public static final String SUPPORTED_DENOMINATIONS = "supported_denominations";
	
	/** Insufficient cash at ATM */
	public static final String ERR_ATM_001 = "ERR_ATM_001";
	
	/** Unable to dispense cash for specified amount, incorrect denominations, must be in 20s and/or 50s */
	public static final String ERR_ATM_002 = "ERR_ATM_002";
	
	/** Insufficient cash of denomination */
	public static final String ERR_ATM_003 = "ERR_ATM_003";
	
	/** Denominations not available or incorrect denominations (must be in 20s and/or 50s) */
	public static final String ERR_ATM_004 = "ERR_ATM_004";
	
}
