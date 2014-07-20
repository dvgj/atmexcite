package com.excite.atm.core;


import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
 
/**
 * Primary class, persisted in XML for keeping track
 * of each denomination and the available notes.
 *  
 * @author James David
 *
 */
@XmlRootElement(name = "notes")
@XmlType(propOrder = {"denomination", "balance"})
public class Notes implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3887602105819486841L;

	/** the note denomination */
	private int denomination;
	
	/** number of notes available */
	private int balance;
	
	/**
	 * returns the denomination.
	 * 
	 * @return
	 */
	public int getDenomination() {
		return denomination;
	}
	
	/**
	 * set the denomination.
	 * 
	 * @param denomination
	 *        the denomination value
	 */
	public void setDenomination(int denomination) {
		this.denomination = denomination;
	}
	
	/**
	 * return the number of notes available.
	 * 
	 * @return
	 */
	public int getBalance() {
		return balance;
	}
	
	/**
	 * set the number of notes available.
	 * 
	 * @param balance
	 * 
	 *        number of notes available
	 */
	public void setBalance(int balance) {
		this.balance = balance;
	}
	
	/**
	 * return details on the denomination and the available notes.
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "[$" + getDenomination() + " :  " + getBalance() + " notes]\n";		
	}
}
