package org.beequeue.cache;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.concurrent.atomic.AtomicReference;

import org.beequeue.util.Morph;

public class ThreeStageCache<K,V> implements Sweepable {
	final public LifeCycleCache.Config strongConfig;
	final public LifeCycleCache.Config softConfig;
	final public LifeCycleCache.Config weakConfig;
	final public LoadDeleteStrategy<K, V> strongStrategy;
	
	final private LifeCycleCache<K, V> strong;
	final private Ref<K,V> soft;
	final private Ref<K,V> weak;
	

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
		this.soft = new Ref<K, V>(false, new Morph<Void, LifeCycleCache<K, V>>() {
			@Override
			public LifeCycleCache<K, V> doIt(Void input) {
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
		this.weak = new Ref<K, V>(true, new Morph<Void, LifeCycleCache<K, V>>() {
			@Override
			public LifeCycleCache<K, V> doIt(Void input) {
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
	public static class Ref<K,V> {
		AtomicReference<Reference<LifeCycleCache<K, V>>> atomic = new AtomicReference<Reference<LifeCycleCache<K,V>>>();
		final boolean weak ;
		final Morph<Void, LifeCycleCache<K, V>> cacheConstructor;

		public Ref(boolean weak, Morph<Void, LifeCycleCache<K, V>> cacheConstructor) {
			this.weak = weak;
			this.cacheConstructor = cacheConstructor;
		}


		public LifeCycleCache<K, V> get(){
			Reference<LifeCycleCache<K, V>> ref;
			for(;;){
				ref = atomic.get();
				if(ref != null ){
					LifeCycleCache<K, V> cache = ref.get();
					if(cache!=null){
						return cache;
					}
				}
				Reference<LifeCycleCache<K, V>> newRef = weak 
						? new WeakReference<LifeCycleCache<K,V>>(cacheConstructor.doIt(null))  
						: new SoftReference<LifeCycleCache<K,V>>(cacheConstructor.doIt(null)) ;
				atomic.compareAndSet(ref, newRef);
			}
		}
	}
	@Override
	public void sweep(long currentTimeStamp) {
		strong.sweep(currentTimeStamp);
		soft.get().sweep(currentTimeStamp);
		weak.get().sweep(currentTimeStamp);
	}
	

}
