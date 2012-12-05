package org.beequeue.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class LifeCycleCache<K,V> implements Sweepable {
	
	private static final long HIT_MASS = 1000L;

	public LifeCycleCache(LoadDeleteStrategy<K, V> strategy, long maxLife, long decayRate, int maxEntries) {
		this.strategy = strategy;
		this.maxLife = maxLife;
		this.decayRate = decayRate;
		this.maxEntries = maxEntries;
		this.lowWaterMarkEntriesCount = (maxEntries * 900)/1000;
		this.highWaterMarkEntriesCount = (maxEntries * 1100)/1000;
	}

	final private LoadDeleteStrategy<K, V> strategy ;
	public final long maxLife ;
	public final long decayRate;
	public final int maxEntries ;
	public final int lowWaterMarkEntriesCount ;
	public final int highWaterMarkEntriesCount ;

	private ConcurrentMap<K, LifeCycleEntry<K, V>> map = new ConcurrentHashMap<K, LifeCycleEntry<K, V>>();
	private long averageWeight = HIT_MASS;
	
	public boolean containsKey(K k){
		return map.containsKey(k);
	}

	@Override
	public void sweep(long currentTimeStamp) {
		if(map.size() > maxEntries) {
			long minW = Long.MAX_VALUE;
			long maxW = Long.MIN_VALUE;
			long sum = 0;
			int count = 0 ;
			List<K> toDelete = new ArrayList<K>();
			List<K> toSort = new ArrayList<K>();
			for (LifeCycleEntry<K, V> e : map.values()) {
				if( (currentTimeStamp - e.created) > maxLife ){
					toDelete.add(e.k);
				}else{
					long w = e.weight.adjustWeightToCurrentTime(currentTimeStamp, decayRate);
					if( minW > w ) minW = w;
					if( maxW < w ) maxW = w;
					sum += w ;
					count += 1 ;
					int index = count;
					long delta = maxW - minW ;
					if( delta > 0 ){
						index = (int)(((w - minW) * (long)count) / delta) ;
					}
					toSort.add(index, e.k);
				}
			}
			if(count > 0) this.averageWeight = sum/count;
			int countToDelete = map.size() - lowWaterMarkEntriesCount;
			while(toDelete.size() < countToDelete){
				toDelete.add(toSort.remove(0));
			}
			for (K k : toDelete) {
				LifeCycleEntry<K, V> remove = map.remove(k);
				if(remove!=null){
					strategy.delete(k, remove.value());
				}
			}
		}
	}
	
	public V get(K k){
		LifeCycleEntry<K, V> entry = map.get(k);
		if(entry == null){
			LifeCycleEntry<K, V> entryCandidate = new LifeCycleEntry<K, V>(k,this.averageWeight);
			LifeCycleEntry<K, V> alreadyThere = map.putIfAbsent(k, entry);
			if(alreadyThere == null){
				entryCandidate.load(strategy);
				entry = entryCandidate ;
			}else{
				entry = alreadyThere;
				entry.weight.hit(decayRate, HIT_MASS);
			}
			if(map.size()>highWaterMarkEntriesCount){
				sweep(System.currentTimeMillis());
			}
		}
		return entry.value();
	}
	

}
