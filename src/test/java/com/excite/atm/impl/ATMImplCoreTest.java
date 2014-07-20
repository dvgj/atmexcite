package com.excite.atm.impl;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Locale;
import java.util.ResourceBundle;

import junit.framework.TestCase;

import com.excite.atm.core.ATM;
import com.excite.atm.core.Notes;
import com.excite.atm.util.InstanceFactory;

/**
 * Unit test for simple App.
 */
public class ATMImplCoreTest 
    extends TestCase
{
	static Locale locale = new Locale("en");
	static ResourceBundle messages = ResourceBundle.getBundle("messages", locale);

    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public ATMImplCoreTest( String testName )
    {
        super( testName );
        
        //init the notes.xml for testing
        try {
        FileOutputStream fos = new FileOutputStream("notes.xml");
        FileInputStream fis = new FileInputStream("notes-test.xml");
        int available = fis.available();
        byte buf[] = new byte[available];
        fis.read(buf);
        fis.close();
        fos.write(buf);;
        fos.close();
        } catch (Exception e) {
        	fail("Unable to initialize notes.xml for testing : " + e.getMessage());
        }
    }



	/**
	 * Test $20 withdrawal
	 */
	public void testWithdraw20() {
		try {
			ATM atm = InstanceFactory.getATMInstance();
			Notes notes[] = atm.withdraw(20);
			if (notes != null) {
				for (Notes each : notes) {
					if (each.getDenomination() == 20) {
						assertEquals("1 note of $20", 1, each.getBalance());
					}
				}
			}
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}    

	/**
	 * Test $50 withdrawal
	 */
	public void testWithdraw50() {
		try {
			ATM atm = InstanceFactory.getATMInstance();
			Notes notes[] = atm.withdraw(50);
			if (notes != null) {
				for (Notes each : notes) {
					if (each.getDenomination() == 50) {
						assertEquals("1 note of $50", 1, each.getBalance());
					}
				}
			}
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	/**
	 * Test $20 and $50 withdrawal
	 */
	public void testWithdraw70() {
		try {
			ATM atm = InstanceFactory.getATMInstance();
			Notes notes[] = atm.withdraw(70);
			if (notes != null) {
				for (Notes each : notes) {
					if (each.getDenomination() == 20 || each.getDenomination() == 50) {
						assertEquals("1 note of $20 or $50", 1, each.getBalance());
					}
				}
			}
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
}
