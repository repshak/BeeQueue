package org.beequeue.util;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.concurrent.atomic.AtomicReference;


public class NotStrongReference<T> {
	AtomicReference<Reference<T>> atomic = new AtomicReference<Reference<T>>();
	final boolean weak ;
	final BeeOperation<Void, T> constructor;

	public NotStrongReference(boolean weak, BeeOperation<Void, T> constructor) {
		this.weak = weak;
		this.constructor = constructor;
	}


	public T get(){
		Reference<T> ref;
		for(;;){
			ref = atomic.get();
			if(ref != null ){
				T cache = ref.get();
				if(cache!=null){
					return cache;
				}
			}
			T newInstance = constructor.doIt(null);
			Reference<T> newRef = weak 
					? new WeakReference<T>(newInstance)  
					: new SoftReference<T>(newInstance) ;
			if(atomic.compareAndSet(ref, newRef)){
				return newInstance;
			}
		}
	}
}