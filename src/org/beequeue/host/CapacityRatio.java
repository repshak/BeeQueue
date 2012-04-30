package org.beequeue.host;

public class CapacityRatio {
	public double value;
	public double max;
	
	public double ratio(){
		return value/max;
	}
	
	public double percent(){
		return ratio() * 100;
	}
}
