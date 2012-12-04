package org.beequeue.piles.lifecycle;

public interface LifeCycleListener<K,V> {
	void onDelete (K k, V v);
	void onPut    (K k, V vOld, V vNew);
	void onEvict  (K k, V v);
}
