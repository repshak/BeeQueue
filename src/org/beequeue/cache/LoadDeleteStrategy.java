package org.beequeue.cache;

public interface LoadDeleteStrategy<K,V> {
	V load(K k);
	void delete(K k, V v);
}
