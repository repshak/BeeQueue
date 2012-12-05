package org.beequeue.cache;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.beequeue.util.BeeException;

public class LifeCycleEntry<K, V> {
	public final K k ;
	public final long created = System.currentTimeMillis();
	public final ExponentialDecayWeight weight ;
	final Lock lock = new ReentrantLock();
	final Condition ready  = lock.newCondition(); 
	
	private  V v  = null;
	
	public V value() {
		if( v == null ){
			lock.lock();
			try{ 
				ready.await(); 
			} catch (InterruptedException e) {
				throw new BeeException(e);
			}finally{
				lock.unlock();
			}
		}
		return v;
	}

	public void load(LoadDeleteStrategy<K, V> strategy) {
		lock.lock();
		try{ 
			this.v = strategy.load(k);
			ready.signalAll(); 
		}finally{
			lock.unlock();
		}
	}

	public LifeCycleEntry(K k,long averageWeight) {
		this.k = k;
		this.weight = new ExponentialDecayWeight(System.currentTimeMillis(), averageWeight);
	}

}
