package org.beequeue.cache;

public interface LoadDeleteStrategy<K,V> {
	V load(K k);
	void replace(K k, V v, V old);
	void delete(K k, V v);
}
