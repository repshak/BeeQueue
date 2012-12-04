package org.beequeue.piles.lifecycle;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

abstract public class LifeCycleMap<K,V> implements Sweepable {
	public LifeCycleMap(long maxLife, long halfLife, long boredomLimit, int maxEntries) {
		this.maxLife = maxLife;
		this.halfLife = halfLife;
		this.boredomLimit = boredomLimit;
		this.maxEntries = maxEntries;
	}
	public final long maxLife ;
	public final long halfLife;
	public final long boredomLimit ;
	public final int maxEntries ;

	private ConcurrentMap<K, V> map = new ConcurrentHashMap<K, V>();
	
	abstract public V get(K k);
	abstract public V put(K k, V v);

}
