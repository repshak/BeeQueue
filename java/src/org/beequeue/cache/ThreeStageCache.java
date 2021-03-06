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


import org.beequeue.util.BeeOperation;
import org.beequeue.util.NotStrongReference;

public class ThreeStageCache<K,V> implements Sweepable {
	final public LifeCycleCache.Config strongConfig;
	final public LifeCycleCache.Config softConfig;
	final public LifeCycleCache.Config weakConfig;
	final public LoadDeleteStrategy<K, V> strongStrategy;
	
	final private LifeCycleCache<K, V> strong;
	final private NotStrongReference<LifeCycleCache<K, V>> soft;
	final private NotStrongReference<LifeCycleCache<K, V>> weak;
	

	public ThreeStageCache(LifeCycleCache.Config strongConfig, LifeCycleCache.Config softConfig,
			LifeCycleCache.Config weakConfig, LoadDeleteStrategy<K, V> strongStrategy) {
		super();
		this.strongConfig = strongConfig;
		this.softConfig = softConfig;
		this.weakConfig = weakConfig;
		this.strongStrategy = strongStrategy;
		this.strong = new LifeCycleCache<K, V>(new LoadDeleteStrategy<K, V>() {
			@Override
			public V load(K k) {
				return ThreeStageCache.this.strongStrategy.load(k);
			}

			@Override
			public void delete(K k, V v) {
				ThreeStageCache.this.strongStrategy.delete(k,v);
				ThreeStageCache.this.soft.get().put(k, v);
			}

			@Override
			public void replace(K k, V vNew, V old) {
				ThreeStageCache.this.strongStrategy.replace(k,vNew, old);
			}
		}, strongConfig);
		this.soft = new NotStrongReference<LifeCycleCache<K, V>>(false, new BeeOperation<Void, LifeCycleCache<K, V>>() {
			@Override
			public LifeCycleCache<K, V> execute(Void input) {
				return new LifeCycleCache<K, V>(new LoadDeleteStrategy<K, V>() {
					@Override
					public V load(K k) {
						return null;
					}

					@Override
					public void delete(K k, V v) {
						ThreeStageCache.this.weak.get().put(k, v);
					}

					@Override
					public void replace(K k, V vNew, V old) {
					}
				},ThreeStageCache.this.softConfig);
			}
		});
		this.weak = new NotStrongReference<LifeCycleCache<K, V>>(true, new BeeOperation<Void, LifeCycleCache<K, V>>() {
			@Override
			public LifeCycleCache<K, V> execute(Void input) {
				return new LifeCycleCache<K, V>(new LoadDeleteStrategy<K, V>() {
					@Override
					public V load(K k) {
						return null;
					}

					@Override
					public void delete(K k, V v) {
					}

					@Override
					public void replace(K k, V vNew, V old) {
					}
				},ThreeStageCache.this.weakConfig);
			}
		});
	}
	@Override
	public void sweep(long currentTimeStamp) {
		strong.sweep(currentTimeStamp);
		soft.get().sweep(currentTimeStamp);
		weak.get().sweep(currentTimeStamp);
	}
	

}
