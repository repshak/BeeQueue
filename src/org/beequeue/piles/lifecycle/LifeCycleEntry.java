package org.beequeue.piles.lifecycle;

public class LifeCycleEntry<K, V> {
	public final K k ;
	public final V v ;
	public final long created = System.currentTimeMillis();
	private long used = 0;
	private long weight = 0 ;
	
	public LifeCycleEntry(K k, V v) {
		this.k = k;
		this.v = v;
	}

	public void onGet(long halfLife, long cost){
			onGet(halfLife, cost, System.currentTimeMillis());
	}

	protected void onGet(long halfLife, long cost, long currentTimeMillis) {
		weight = cost + adjustWeightToTime(currentTimeMillis,halfLife);
		used = currentTimeMillis;
	}

	public long adjustWeightToTime(long currentTimeMillis, long halfLife) {
		long newWeight = weight;
		if(used != 0){
			long delta = currentTimeMillis - used ;
			while(delta > halfLife){
				delta = delta - halfLife;
				newWeight /= 2;
			}
			newWeight = newWeight - newWeight * delta / ( halfLife * 2 ) ;
		}
		return newWeight;
	}
}
