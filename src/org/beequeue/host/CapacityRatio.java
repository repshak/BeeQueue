package org.beequeue.host;

public class CapacityRatio {
	public double value;
	public double min = 0.;
	public double max;
	
	public double ratio(){
		return (value-min)/(max-min);
	}
	
	public double percent(){
		return ratio() * 100;
	}
}
