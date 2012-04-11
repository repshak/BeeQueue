/* This file is generated out of 
 * java/kernel/num/cracker/flock/DoubleFlock.java
 * Please do not edit. 
 *
 * Yours truly, DoubleFlockCodegen
 */
package org.beequeue.piles.flock;

public interface BooleanFlock extends Flock<Boolean> {
  boolean get(int at);
  void set(int at, boolean val);
  void setArray(int at, boolean... vals);
  void insert(int at, boolean... vals);
  void add(boolean... vals);
  boolean[] toArray(int from, int size);
  boolean[] toArray();
}
