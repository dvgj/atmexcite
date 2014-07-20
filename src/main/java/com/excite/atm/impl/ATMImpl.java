package com.excite.atm.impl;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;


import java.util.logging.Logger;

import javax.xml.bind.annotation.*;

import com.excite.atm.core.ATM;
import com.excite.atm.core.ATMInvocationHandler;
import com.excite.atm.core.AppContext;
import com.excite.atm.core.Notes;
import com.excite.atm.exceptions.ATMException;
import com.excite.atm.util.XMLLoader;


/**
 * 
 * Singleton class, to store various denominations of notes,
 * allow withdrawal and display cash balance.
 * 
 * @author James David
 *
 */

@XmlRootElement(name = "atm")
@XmlType(propOrder = {"notes"})
public final class ATMImpl implements ATM {
	private static Logger log = Logger.getLogger(ATMImpl.class.getName());
	
	/* local used for loading error messages */
	static Locale locale = new Locale("en");
	static ResourceBundle messages = ResourceBundle.getBundle("messages", locale);
	
	/* for holding the singleton instance */
	private static ATM atm = null;
	
	/** contains the notes, each denomination and their balances. */
	private Notes[] notes = null;
	
	/** interim field to store notes and thier balances. */
	private Map<Integer, Notes> hmNotes = null;
	
	/** private constructor
	 * 
	 * @see #getInstance()
	 * @throws Exception
	 */
	private ATMImpl() throws Exception {

	}
	
	/**
	 * creates instance of this class, initialized the class with appropriate values
	 * from xml file.  Wraps the instance with a <code>Proxy</code> interface
	 * such that any method invoked on this instance are intercepted for 
	 * any logging, auditing and additional validation/check purposes.
	 * 
	 * @return the only instance of this class.
	 * @throws Exception
	 */
	public static synchronized ATM getInstance() throws Exception {
		if (atm == null) {
				ATMImpl impl = new ATMImpl();
								
				impl.init();				
				
				ATMInvocationHandler handler = new ATMInvocationHandler(impl);
				atm = (ATM) Proxy.newProxyInstance(ATM.class.getClassLoader(), new Class[]{ATM.class}, handler);
		}
		return atm;
	}
	
	/**
	 * get the value for a given property key.
	 * 
	 * @param propKey 
	 *        the key used for retrieving the property
	 *        
	 * @return the property
	 */
	public Object getAppProperty(String propKey) {
		Properties prop = ((AppContext) context.get()).getAppProperties();
		return prop.get(propKey);
	}
	
	/**
	 * store any value for a given key.
	 * 
	 * @param propKey
	 *        property key
	 * @param val
	 *        property value
	 */
	public void setAppProperty(String propKey, Object val) {
		Properties prop = ((AppContext) context.get()).getAppProperties();
		prop.put(propKey, val);
	}

	/*
	 * initializes this instance with values from the xml configured in 
	 * the property file(s).  Reads the xml file and initializes the
	 * notes containing the denomination and balances.  
	 * 
	 */
	private void init() throws Exception {

		try {
			String notesFileName = (String) getAppProperty(Constants.FN_NOTES_APP_PROP_KEY);
			ATMImpl fromFile = (ATMImpl) XMLLoader.jaxbXMLToObject(ATMImpl.class, notesFileName);
			notes = fromFile.getNotes();
			
			buildNotesMap(notes);
			
		} catch (Exception e) {
			log.log(Level.SEVERE, "Unable to initialize ATM with notes", e);	
			throw new java.lang.InstantiationException(e.getMessage());
		} 
	}
	
	/*
	 * utility method to convert the notes array into a map object for
	 * easy processing during withdrawal.
	 * 
	 */
	private void buildNotesMap(Notes[] notes) {
		hmNotes = new HashMap<Integer, Notes>();
		
		for (Notes each : notes) {
			Notes existing = (Notes) hmNotes.get(each.getDenomination());
			if (existing != null) {
				int newbalance = existing.getBalance() + each.getBalance();				
				existing.setBalance(newbalance);
			} else {
				hmNotes.put(each.getDenomination(), each);
			}
		}
		
	}

    /**
     * returns the notes array held by this object.
     * 
     * @return
     */
	public Notes[] getNotes() {
		return notes;
	}
	/**
	 * used for updating the notes held by this object. 
	 * @param notes
	 */
	public void setNotes(Notes[] notes) {
		this.notes = notes;
	}
	
	/**
	 * The process of dispensing cash and maintaining balance.
	 * internally invokes a recursive method to drill down the
	 * denominations.
	 * 
	 * And, once a proper result is arrived, note balances are 
	 * adjusted accordingly and cash is considered dispensed.
	 * 
	 * @param amount
	 *        amount to withdraw
	 * @throws Exception
	 */
	public synchronized Notes[] withdraw(int amount) throws Exception {
		log.log(Level.INFO, "withdraw : " + amount);
		int denominations[] = getSupportedDenominations();
		
		int ilen = denominations.length;
		int[] result = new int[ilen];
		
		_withdraw(denominations, amount, ilen - 1, result);
		
		Notes[] retVal = new Notes[ilen];
		StringBuffer sbLog = new StringBuffer();
		for (int i = 0; i < ilen; i++) {
			sbLog.append("[" + denominations[i]+"s = " + result[i] + " notes],");
			
			Notes tmpNotes = new Notes();
			tmpNotes.setDenomination(denominations[i]);
			tmpNotes.setBalance(result[i]);
			retVal[i] = tmpNotes;
		}
		
		
		log.log(Level.INFO, sbLog.toString());
		
		doWithdraw(denominations, result);
		
		
		
		return retVal;
		
	}
	
	/*
	 * method to adjust the balances of notes based on 
	 * result of calculation on the number of notes needed from
	 * each denomination. 
	 * 
	 * after appropriate adjustment is made, the current
	 * balances are serialized back to the xml.
	 */
	private void doWithdraw(int[] denominations, int[] result) throws Exception {
		int ilen = denominations.length;
		
		for (int i = 0; i< ilen; i++) {
			if (result[i] > 0) {
				Notes note = (Notes)hmNotes.get(denominations[i]);
				int availableNotes = (int) note.getBalance();
				int balance = availableNotes - result[i];
				note.setBalance(balance);
				hmNotes.put(denominations[i], note);
			}
		}
		
		String notesFileName = (String) getAppProperty(Constants.FN_NOTES_APP_PROP_KEY);
		try {
			XMLLoader.jaxbObjectToXML(this, notesFileName);
		} catch (Exception e) {
			init(); //revert back to last known good data.
			throw e;
		}
	}

    /*
     * recursive method to drill down the denominations and check
     * if there is enough balance in each denomination.
     * 
     * this method is also responsible for throwing a descriptive
     * exception in case the transaction fails.
     * 
     */
	private void _withdraw(int[] denominations, int amount, int index, int[] result) throws Exception {
		
		if (index == (denominations.length - 1) && amount > getBalance()) {
			//Insufficient cash at ATM
			throw new ATMException(messages.getString(Constants.ERR_ATM_001));
		}
		
		int balance = amount;
		int availableNotes = (int) ((Notes)hmNotes.get(denominations[index])).getBalance();
		
		if (amount < denominations[index]) {  
			/* if the amount is less than the current denomination, and we have
			 * lesser denominations available, proceed to continue the transaction
			 * with lesser denominations.
			 */
			
			if (index > 0) {
				_withdraw(denominations, balance, index - 1, result);
			} else {
				//Unable to dispense cash for specified amount, incorrect denominations, must be in 20s and/or 50s
				throw new ATMException(messages.getString(Constants.ERR_ATM_002));
			}
		} else {
		
			int div = amount / denominations[index];
			if (availableNotes < div) {		
				if (index > 0) {
					if (availableNotes > 0) {
						result[index] = availableNotes;
						balance = balance - (availableNotes * denominations[index]);
					}
					_withdraw(denominations, balance, index - 1, result);
				}
			} else {
				int mod = amount % denominations[index];
				balance = amount - (div * denominations[index]);
				if (mod == 0) {					
					result[index] = div;			
				} else if (index > 0) {
					result[index] = div;	
					_withdraw(denominations, balance, index - 1, result);
				} else {
					//Denominations not available or incorrect denominations (must be in 20s and/or 50s)
					throw new ATMException(messages.getString(Constants.ERR_ATM_003));
				}
			}
		}
	}

	
	private int[] getSupportedDenominations() {
		
		int denominations[] = (int[]) getAppProperty(Constants.SUPPORTED_DENOMINATIONS);
		if (denominations == null) {
			String tmp_denominations = (String) getAppProperty(Constants.SUPPORTED_DENOMINATIONS_PROP_KEY);
			String str_denominations[] = tmp_denominations.split("[,]");
			int tmp_iDenominations[] = new int[str_denominations.length];
			int ilen = tmp_iDenominations.length;
			for (int i = 0; i < ilen; i++) {
				String eachDenom = str_denominations[i];
				eachDenom = eachDenom.trim();
				tmp_iDenominations[i] = Integer.parseInt(eachDenom);
			}
			
			denominations = tmp_iDenominations;
			Arrays.sort(denominations);
			
			setAppProperty(Constants.SUPPORTED_DENOMINATIONS, denominations);
		}
		return denominations;
	}


	/**
	 * Show the balance notes for each denomination.
	 * @throws Exception
	 */
	@XmlTransient
	public Notes[] getBalanceNotes() throws Exception {
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(bos);
		oos.writeObject(notes);
		oos.close();
		bos.close();
		byte buf[] = bos.toByteArray();
		ByteArrayInputStream bis = new ByteArrayInputStream(buf);
		ObjectInputStream ois = new ObjectInputStream(bis);
		Notes[] clonedNotes = (Notes[]) ois.readObject();
		
		return clonedNotes;
		
	}
	
	public int getBalance() {
		int totalAmount = 0;
		for (Notes note : notes) {
			totalAmount += (note.getBalance() * note.getDenomination());
		}
		return totalAmount;
	}
	
}
