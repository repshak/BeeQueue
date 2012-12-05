package org.beequeue.cache;

public class ExponentialDecayWeight {
	private long lastHitTimeMills ;
	private long weight ;

	public ExponentialDecayWeight() {
		this(0,0);
	}
	public ExponentialDecayWeight(long lastHitTimeMills, long weight) {
		this.lastHitTimeMills = lastHitTimeMills;
		this.weight = weight;
	}

	public long getLastHitTimeMills() {
		return lastHitTimeMills;
	}

	public long getWeight() {
		return weight;
	}

	final public void hit(long halfLife, long mass){
		hit(halfLife, mass, System.currentTimeMillis());
	}
	
	final public void hit(long halfLife, long mass, long currentTimeMillis) {
		this.weight = mass + adjustWeightToCurrentTime(currentTimeMillis,halfLife);
		this.lastHitTimeMills = currentTimeMillis;
	}
	
	final public long adjustWeightToCurrentTime(long currentTimeMillis, long halfLife) {
		long newWeight = this.weight;
		if(this.lastHitTimeMills != 0){
			long delta = currentTimeMillis - this.lastHitTimeMills ;
			while(delta > halfLife){
				delta = delta - halfLife;
				newWeight /= 2;
			}
			newWeight = newWeight - newWeight * delta / ( halfLife * 2 ) ;
		}
		return newWeight;
	}

}
