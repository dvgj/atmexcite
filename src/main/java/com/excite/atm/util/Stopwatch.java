package com.excite.atm.util;

public class Stopwatch {
	long timer;
	
	public Stopwatch() {
		
	}
	public Stopwatch start() {
		timer = System.currentTimeMillis();
		return this;
	}
	public long getElapsedTime() {
		return System.currentTimeMillis() - timer;		
	}

}
