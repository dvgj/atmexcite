package com.excite.atm.core;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.logging.Logger;

import com.excite.atm.util.Stopwatch;

/**
 * 
 * A simple AOP using Java <code>Proxy</code> class
 * This class just logs the time taken for each methods
 * for the given implementation.
 * 
 * @author James David
 *
 */
public class ATMInvocationHandler implements InvocationHandler {
	private static Logger log = Logger.getLogger(ATMInvocationHandler.class.getName());	
	
	/** The actual implementation class */
	private ATM impl = null;
	
	/** 
	 * Creates a new {@code ATMInvocationHandler} with the given implementation class.
	 * 
	 * @param impl
	 *        Implementation class for {@code ATM} interface
	 */
	public ATMInvocationHandler(ATM impl) {
		this.impl = impl;
	}
	
	/**
	 * Invokes the method and logs the time taken.
	 * 
	 * @see java.lang.reflect.InfocationHandler$invoke()
	 */
	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
			Stopwatch timer = new Stopwatch().start();	
			Object retval = method.invoke(impl, args);
			log.info("Time taken for executing method " + method.getName() + " : " + timer.getElapsedTime() + "ms");
			return retval;
	}

}
