/* This file is generated out of 
 * java/kernel/num/cracker/flock/DoubleFlock.java
 * Please do not edit. 
 *
 * Yours truly, DoubleFlockCodegen
 */
package org.beequeue.piles.flock;

public interface ShortFlock extends Flock<Short> {
  short get(int at);
  void set(int at, short val);
  void setArray(int at, short... vals);
  void insert(int at, short... vals);
  void add(short... vals);
  short[] toArray(int from, int size);
  short[] toArray();
}
