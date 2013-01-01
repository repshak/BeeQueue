package org.beequeue.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class LifeCycleCache<K,V> implements Sweepable {
	public static class Config{
		public final long maxLife ;
		public final long decayRate;
		public final int maxEntries ;
		public final int lowWaterMarkEntriesCount ;
		public final int highWaterMarkEntriesCount ;
		public Config(long maxLife, long decayRate, int maxEntries) {
			this.maxLife = maxLife;
			this.decayRate = decayRate;
			this.maxEntries = maxEntries;
			this.lowWaterMarkEntriesCount = (maxEntries * 900)/1000;
			this.highWaterMarkEntriesCount = (maxEntries * 1100)/1000;
		}
	}
	private static final long HIT_MASS = 1000L;

	public LifeCycleCache(LoadDeleteStrategy<K, V> strategy, Config config) {
		this.strategy = strategy;
		this.config = config;
	}

	final private LoadDeleteStrategy<K, V> strategy ;
	final public Config config;
	private ConcurrentMap<K, LifeCycleEntry<K, V>> map = new ConcurrentHashMap<K, LifeCycleEntry<K, V>>();
	private long averageWeight = HIT_MASS;
	
	public boolean containsKey(K k){
		return map.containsKey(k);
	}

	@Override
	public void sweep(long currentTimeStamp) {
		if(map.size() > this.config.maxEntries) {
			long minW = Long.MAX_VALUE;
			long maxW = Long.MIN_VALUE;
			long sum = 0;
			int count = 0 ;
			List<K> toDelete = new ArrayList<K>();
			List<K> toSort = new ArrayList<K>();
			//Poor man sorting, not precise but should be linear enough in tail
			for (LifeCycleEntry<K, V> e : map.values()) {
				if( (currentTimeStamp - e.created) > this.config.maxLife ){
					toDelete.add(e.k);
				}else{
					long w = e.weight.adjustWeightToCurrentTime(currentTimeStamp, this.config.decayRate);
					if( minW > w ) minW = w;
					if( maxW < w ) maxW = w;
					sum += w ;
					int index = count;
					long delta = maxW - minW ;
					if( delta > 0 ){
						index = (int)(((w - minW) * (long)count) / delta) ;
					}
					toSort.add(index, e.k);
					count += 1 ;
				}
			}
			if(count > 0) this.averageWeight = sum/count;
			int countToDelete = map.size() - this.config.lowWaterMarkEntriesCount;
			while(toDelete.size() < countToDelete){
				toDelete.add(toSort.remove(0));
			}
			for (K k : toDelete) {
				remove(k);
			}
		}
	}

	protected boolean remove(K k) {
		LifeCycleEntry<K, V> remove = map.remove(k);
		if(remove!=null){
			strategy.delete(k, remove.value());
			return true;
		}
		return false;
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
				entry.weight.hit(this.config.decayRate, HIT_MASS);
			}
			if(map.size()>this.config.highWaterMarkEntriesCount){
				sweep(System.currentTimeMillis());
			}
		}
		return entry.value();
	}
	public V put (K k, V v){
		LifeCycleEntry<K, V> entryCandidate = new LifeCycleEntry<K, V>(k,this.averageWeight,v);
		LifeCycleEntry<K, V> alreadyThere = map.put(k,entryCandidate);
		if(alreadyThere == null){
			V alreadyThereValue = alreadyThere.value();
			V replaceValue = entryCandidate.value();
			strategy.replace(k,replaceValue,alreadyThereValue);
			return alreadyThereValue;
		}
		return null;
	}
	
}
