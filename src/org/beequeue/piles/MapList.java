package org.beequeue.piles;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.beequeue.util.Morph;

public class MapList<K,V> extends BoundList<V> {

	private static final long serialVersionUID = 1L;
	
	private Morph<? super V,? extends K> extractKey ;
	
	public MapList(Morph<? super V,? extends K> extractKey){
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
