package com.excite.atm.impl;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Locale;
import java.util.ResourceBundle;

import junit.framework.TestCase;

import com.excite.atm.core.ATM;
import com.excite.atm.util.InstanceFactory;

/**
 * Unit test for simple App.
 */
public class ATMImplErrorsTest 
    extends TestCase
{
	static Locale locale = new Locale("en");
	static ResourceBundle messages = ResourceBundle.getBundle("messages", locale);

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public ATMImplErrorsTest( String testName )
    {
        super( testName );
    }

    /**
     * reset the notes.xml for demo
     */
    public void tearDown() {
    	//reset the notes.xml for demo
        try {
        FileOutputStream fos = new FileOutputStream("notes.xml");
        FileInputStream fis = new FileInputStream("notes-demo.xml");
        int available = fis.available();
        byte buf[] = new byte[available];
        fis.read(buf);
        fis.close();
        fos.write(buf);;
        fos.close();
        } catch (Exception e) {
        	fail("Unable to reset notes.xml for demo : " + e.getMessage());
        }
    }
    /*
     * utility method to execute withdraw and return the error message.
     */
	private String _withdraw(int amount) {
		try {
			ATM atm = InstanceFactory.getATMInstance();
			atm.withdraw(amount);
			return "";
		} catch (Exception e) {
			return e.getCause().getMessage();			
		}
	}
	
	
	/**
	 * Test all errors.
	 * 
	 * ERR_ATM_001=Insufficient cash at ATM
	 * ERR_ATM_002=Unable to dispense cash for specified amount, incorrect denominations, must be in 20s and/or 50s
	 * ERR_ATM_003=Insufficient cash of denomination 
	 * ERR_ATM_004=Denominations not available or incorrect denominations (must be in 20s and/or 50s)
	 */
	
	public void testErrors() {
		_InsufficientBalance();
		_IncorrectDenominations();
		_IncorrectDenom();
	}
	/**
	 * Test
	 * ERR_ATM_001=Insufficient cash at ATM
	 */
	private void _InsufficientBalance() {
		
		try {
			ATM atm = InstanceFactory.getATMInstance();
			int balance = atm.getBalance();
			String msg = _withdraw(balance + 1);
			assertEquals(Constants.ERR_ATM_001, messages.getString(Constants.ERR_ATM_001), msg);
		} catch (Exception e) {
			fail(e.getMessage());
		}
		
		
	}
	
	/**
	 * Test
	 * ERR_ATM_002=Unable to dispense cash for specified amount, incorrect denominations, must be in 20s and/or 50s
	 */
	private void _IncorrectDenominations() {
		try {			
			String msg = _withdraw(10);
			assertEquals(Constants.ERR_ATM_002, messages.getString(Constants.ERR_ATM_002), msg);			
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	
	/**
	 * Test
	 * ERR_ATM_004=Denominations not available or incorrect denominations (must be in 20s and/or 50s)
	 * 
	 */
	private void _IncorrectDenom() {
		try {
			String msg = _withdraw(80);
			assertEquals(Constants.ERR_ATM_003, messages.getString(Constants.ERR_ATM_003), msg);
			
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	
}
