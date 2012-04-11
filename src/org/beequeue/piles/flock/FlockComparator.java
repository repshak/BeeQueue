package org.beequeue.piles.flock;

public interface FlockComparator<T,V> {
  int compare(Flock<T> flock, int at1, int at2);
  int compareIt(Flock<T> flock, int at, V val);
  int compareValue(V v1, V v2);
  V get(Flock<T> flock,int at);
}
