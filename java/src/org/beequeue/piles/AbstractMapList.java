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

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class AbstractMapList<K,V> extends BoundList<V> {

	private static final long serialVersionUID = 1L;
	
	@JsonIgnore
	abstract protected BeeOperation<? super V,? extends K> getExtractKey();
	
	public AbstractMapList(){
		super();
		setUpdateListener(new Listener() {
			@Override
			public void updated() {
				AbstractMapList.this.refresh();
			}
		});
	}

	private Map<K,V> map = null;
	public Map<K,V> map(){
		Map<K, V> localCopy = map;
		if( localCopy == null ){
			localCopy = new HashMap<K, V>();
			for (V v : this) {
				localCopy.put(getExtractKey().execute(v), v);
			}
			localCopy = Collections.unmodifiableMap(localCopy);
			this.map = localCopy ;
		}
		return localCopy;
	}
	
	private Map<K,Integer> indexMap = null;
	public Map<K,Integer> indexMap(){
		Map<K, Integer> localCopy = indexMap;
		if( localCopy == null ){
			localCopy = new HashMap<K, Integer>();
			for (int i = 0; i < this.size(); i++) {
				localCopy.put(getExtractKey().execute(get(i)), i);
			}
			localCopy = Collections.unmodifiableMap(localCopy);
			this.indexMap = localCopy ;
		}
		return localCopy;
	}
	
	public void refresh(){
		this.map=null;
		this.indexMap=null;
	}
	
	
	

}
