/* This file is generated out of 
 * java/kernel/num/cracker/flock/DoubleFlock.java
 * Please do not edit. 
 *
 * Yours truly, DoubleFlockCodegen
 */
package org.beequeue.piles.flock;

public interface IntegerFlock extends Flock<Integer> {
  int get(int at);
  void set(int at, int val);
  void setArray(int at, int... vals);
  void insert(int at, int... vals);
  void add(int... vals);
  int[] toArray(int from, int size);
  int[] toArray();
}
