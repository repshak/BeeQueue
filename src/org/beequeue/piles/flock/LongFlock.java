/* This file is generated out of 
 * java/kernel/num/cracker/flock/DoubleFlock.java
 * Please do not edit. 
 *
 * Yours truly, DoubleFlockCodegen
 */
package org.beequeue.piles.flock;

public interface LongFlock extends Flock<Long> {
  long get(int at);
  void set(int at, long val);
  void setArray(int at, long... vals);
  void insert(int at, long... vals);
  void add(long... vals);
  long[] toArray(int from, int size);
  long[] toArray();
}
