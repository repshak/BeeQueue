/* This file is generated out of 
 * java/kernel/num/cracker/flock/DoubleFlock.java
 * Please do not edit. 
 *
 * Yours truly, DoubleFlockCodegen
 */
package org.beequeue.piles.flock;

public interface ByteFlock extends Flock<Byte> {
  byte get(int at);
  void set(int at, byte val);
  void setArray(int at, byte... vals);
  void insert(int at, byte... vals);
  void add(byte... vals);
  byte[] toArray(int from, int size);
  byte[] toArray();
}
