package org.beequeue.util;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class TypeFactoryCache {
	private TypeFactoryCache(){}
	public static final TypeFactoryCache instance = new TypeFactoryCache();
	private ConcurrentMap<String, TypeFactory<?>> map = new ConcurrentHashMap<String, TypeFactory<?>>();
	
	public TypeFactory<?> getTypeFactory(String className){
		if( Strings.isEmpty(className) ){
			return null;
		}
		if( map.containsKey(className) ){
			return map.get(className);
		}
		try {
			@SuppressWarnings("rawtypes")
			Class forName = Class.forName(className);
			@SuppressWarnings({ "rawtypes", "unchecked" })
			TypeFactory tf = new TypeFactory(forName);
			map.put(className, tf);
			return tf;
		} catch (Exception ignore) {
			map.put(className, null);
		}
		return null;
	}
	
}
