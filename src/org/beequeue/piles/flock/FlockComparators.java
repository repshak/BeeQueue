package org.beequeue.piles.flock;

import org.beequeue.util.Nulls;

public class FlockComparators {
  public static FlockComparator<String,String> STRING = FlockComparators.comparatorNullsFirst(String.class);
  public static FlockComparator<Byte,Byte> BYTE = FlockComparators.comparatorNullsFirst(Byte.class);
  public static FlockComparator<Long,Long> LONG = FlockComparators.comparatorNullsFirst(Long.class);
  public static FlockComparator<Integer,Integer> INT = FlockComparators.comparatorNullsFirst(Integer.class);
  public static FlockComparator<Float,Float> FLOAT = FlockComparators.comparatorNullsFirst(Float.class);
  public static FlockComparator<Double,Double> DOUBLE = FlockComparators.comparatorNullsFirst(Double.class);
  public static FlockComparator<Short,Short> SHORT = FlockComparators.comparatorNullsFirst(Short.class);
  public static FlockComparator<Boolean,Boolean> BOOL = FlockComparators.comparatorNullsFirst(Boolean.class);
  
  public static <T extends Comparable<T>> FlockComparator<T,T> comparatorNullsFirst(Class<T> clazz) {
    return new FlockComparator<T,T>(){
      public int compare(Flock<T> flock, int at1, int at2) {
        return compareValue(flock.getValue(at1), flock.getValue(at2));
      }
      public int compareIt(Flock<T> flock, int at, T val) {
        return compareValue(flock.getValue(at), val);
      }
      public T get(Flock<T> flock, int at) {
        return flock.getValue(at);
      }
      public int compareValue(T v1, T v2) {
        return Nulls.compare(v1, v2, true);
      }
    };
  }

  public static <T extends Comparable<T>> FlockComparator<T,T> comparatorNullsLast(Class<T> clazz) {
    return new FlockComparator<T,T>(){
      public int compare(Flock<T> flock, int at1, int at2) {
        return compareValue(flock.getValue(at1), flock.getValue(at2));
      }
      public int compareIt(Flock<T> flock, int at, T val) {
        return compareValue(flock.getValue(at), val);
      }
      public T get(Flock<T> flock, int at) {
        return flock.getValue(at);
      }
      public int compareValue(T v1, T v2) {
        return Nulls.compare(v1, v2, false);
      }
    };
  }

  public static <T,V> FlockComparator<T,V> reverse(final FlockComparator<T,V> original) {
    return new FlockComparator<T,V>(){
      public int compare(Flock<T> flock, int at1, int at2) {
        return -original.compare(flock, at1, at2);
      }
      
      public int compareIt(Flock<T> flock, int at, V val) {
        return -original.compareIt(flock, at, val);
      }
      
      public int compareValue(V v1, V v2) {
        return -original.compareValue(v1, v2);
      }
      
      public V get(Flock<T> flock, int at) {
        return original.get(flock, at);
      }
    };
  }
}
