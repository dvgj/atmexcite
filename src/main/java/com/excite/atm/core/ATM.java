package com.excite.atm.core;

/**
 * Binding interface for any implementation willing to provide
 * a basic ATM services.
 * 
 * @author James David
 *
 */
public interface ATM {

	public static ThreadLocal<AppContext> context = new ThreadLocal<AppContext>();
	
	public Notes[] withdraw(int amount) throws Exception;
	
	public Notes[] getBalanceNotes() throws Exception; 
	
	public int getBalance() throws Exception;
		
	
}
