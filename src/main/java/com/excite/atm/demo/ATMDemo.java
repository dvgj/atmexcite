package com.excite.atm.demo;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.excite.atm.core.ATM;
import com.excite.atm.core.Notes;
import com.excite.atm.util.InstanceFactory;

/**
 * This is a demo class, the one that should be run to view the 
 * functionality provided by the <code>ATMImpl</code>.
 * 
 * This class functions as a command line tool, accepting input from
 * the command line and executing appropriate methods on the implementation
 * and displaying the output in console.
 * 
 * for eg.
 * Available commands :
 * amount -> just enter the amount to withdraw and show balance
 * quit -> to exit the application
 * >100
 * $20 -> 98 notes available
 * $50 -> 96 notes available
 * For $100 [0 notes of 20] [2 notes of 50] cash dispensed
 * $20 -> 98 notes available
 * $50 -> 94 notes available
 * Balance : 6660
 * >50
 * $20 -> 98 notes available
 * $50 -> 94 notes available
 * For $50 [0 notes of 20] [1 notes of 50] cash dispensed
 * $20 -> 98 notes available
 * $50 -> 93 notes available
 * Balance : 6610
 * >quit
 * Thank you. Bye.
 * 
 * 
 * @author James David
 *
 */

public class ATMDemo {
	private static Logger log = Logger.getLogger(ATMDemo.class.getName());
	public static final String BAL="bal";
	public static final String WD="wd";
	public static final String QUIT="quit";
	
	public static void main(String args[]) throws Exception {

		ATM demo = InstanceFactory.getATMInstance();
		
		System.out.println("Available commands : \namount -> just enter the amount to withdraw and show balance\nquit -> to exit the application");
		
		Scanner sc = new Scanner(System.in);
        
        	System.out.print(">");
        	
	        while(sc.hasNextLine()) {
	        	
	        	Notes[] cash = null;
	        	String cmd = sc.nextLine();
	        	String arg = cmd;
	        	if (!"quit".equals(cmd)) {
	        		cmd = WD;
	        	}
	        	switch (cmd) {	        	
	        	case WD:
	        		try {
	        			int amount = Integer.parseInt(arg);
	        			cash = demo.withdraw(amount);
	        			print(cash, amount);
	        			StringBuffer sb = new StringBuffer();
	        			
	        			Notes[] notes = demo.getBalanceNotes();
	        			for (Notes note : notes) {
	        				sb.append (note.toString());
	        				System.out.println("$" + note.getDenomination() + " -> " + note.getBalance() + " notes available");
	    
	        			}
	        			log.log(Level.FINE, "Current Balance of notes \n" + sb.toString());

	        			System.out.println("Balance : " + demo.getBalance());
	        		} catch (NumberFormatException ne) {
	        			System.err.println("ERROR : Please provide a valid amount ");
	        		} catch (Exception e) {
	        			System.err.println("ERROR : " + e.getCause().getMessage());
	        		}
	        		break;
	        	case QUIT:
	        		System.out.println("Thank you. Bye.");
	        		System.exit(0);
	        		break;
	        		
	        	}
	        	System.out.print(">");
	        }        
        
	}
	
/*
 * utility method for pretty print of balances.
 * 
 */
	private static void print(Notes[] cash, int amount) {
		
		int ilen = cash.length;
		System.out.print("For $" + amount);
		
		for (Notes each : cash) {
			System.out.print(" ["+each.getBalance() + " notes of " + each.getDenomination() +"]");
		}
		System.out.println(" cash dispensed");
	}
}
