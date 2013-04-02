/** ==== BEGIN LICENSE =====
   Copyright 2012 - BeeQueue.org

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an &quot;AS IS&quot; BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 
 *  ===== END LICENSE ====== */
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
	
	public LifeCycleEntry(K k,long averageWeight,V v) {
		this.k = k;
		this.weight = new ExponentialDecayWeight(System.currentTimeMillis(), averageWeight);
		this.v = v;
	}

}
