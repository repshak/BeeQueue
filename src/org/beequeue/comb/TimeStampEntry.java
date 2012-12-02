package org.beequeue.comb;


public class TimeStampEntry<T> {
	public final TimeSequence ts;
	public final T data;
	
	public TimeStampEntry(TimeSequence ts, T data) {
		this.ts = ts;
		this.data = data;
	}
}
