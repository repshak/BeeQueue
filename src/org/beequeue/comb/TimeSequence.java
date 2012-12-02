package org.beequeue.comb;

import java.util.concurrent.atomic.AtomicReference;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TimeSequence implements Comparable<TimeSequence> {
	private static AtomicReference<TimeSequence> ref = new AtomicReference<TimeSequence>();
	
	public static TimeSequence next(){
		for(;;){
			long currentTimeMillis = System.currentTimeMillis();
			TimeSequence current = ref.get();
			TimeSequence replace = (current == null || currentTimeMillis != current.time) 
				?	new TimeSequence(currentTimeMillis) 
				:   new TimeSequence(current);
			if(ref.compareAndSet(current, replace)){
				return replace;
			}
		}
	}
	public final long time;
	public final int counter;
	
	private TimeSequence(long time){
		this.time = time ;
		this.counter = 0;
	}

	private TimeSequence(TimeSequence ts){
		this.time = ts.time ;
		this.counter = ts.counter+1;
	}
	
	@JsonCreator
	public TimeSequence(@JsonProperty("time") long time,@JsonProperty("counter") int counter){
		this.time = time;
		this.counter = counter;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + counter;
		result = prime * result + (int) (time ^ (time >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TimeSequence other = (TimeSequence) obj;
		if (counter != other.counter)
			return false;
		if (time != other.time)
			return false;
		return true;
	}

	@Override
	public int compareTo(TimeSequence that) {
		return this == that ? 0 : that == null ? -1 : compareNotNull(that) ;
	}

	private int compareNotNull(TimeSequence that) {
		long dTime = that.time - this.time;
		return dTime == 0 ? that.counter - this.counter : dTime < 0 ? -1 : 1 ;
	}
	
	
	
}
