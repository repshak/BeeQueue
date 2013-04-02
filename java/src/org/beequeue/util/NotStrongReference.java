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
			T newInstance = constructor.execute(null);
			Reference<T> newRef = weak 
					? new WeakReference<T>(newInstance)  
					: new SoftReference<T>(newInstance) ;
			if(atomic.compareAndSet(ref, newRef)){
				return newInstance;
			}
		}
	}
}
