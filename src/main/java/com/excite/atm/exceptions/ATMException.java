package com.excite.atm.exceptions;

/**
 * Basic exception class for all exceptions raised
 * from the <code>ATM</code> implementation.
 * 
 * @see com.excite.atm.impl.ATMImpl
 * 
 * @author James David
 *
 */
public class ATMException extends Exception {

	public ATMException() {
		super("Unexpected error has occurred");
	}
	
	public ATMException(String msg) {
		super(msg);
	}
}
