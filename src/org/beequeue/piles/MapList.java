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
package org.beequeue.piles;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.beequeue.util.BeeOperation;

public class MapList<K,V> extends BoundList<V> {

	private static final long serialVersionUID = 1L;
	
	private BeeOperation<? super V,? extends K> extractKey ;
	
	public MapList(BeeOperation<? super V,? extends K> extractKey){
		super();
		this.extractKey = extractKey ;
		setUpdateListener(new Listener() {
			@Override
			public void updated() {
				MapList.this.refresh();
			}
		});
	}

	private Map<K,V> map = null;
	public Map<K,V> map(){
		Map<K, V> localCopy = map;
		if( localCopy == null ){
			localCopy = new HashMap<K, V>();
			for (V v : this) {
				localCopy.put(extractKey.doIt(v), v);
			}
			localCopy = Collections.unmodifiableMap(localCopy);
			this.map = localCopy ;
		}
		return localCopy;
	}
	
	
	public void refresh(){
		this.map=null;
	}
	
	
	

}
