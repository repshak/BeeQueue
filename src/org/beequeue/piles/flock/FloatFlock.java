/* This file is generated out of 
 * java/kernel/num/cracker/flock/DoubleFlock.java
 * Please do not edit. 
 *
 * Yours truly, DoubleFlockCodegen
 */
package org.beequeue.piles.flock;

public interface FloatFlock extends Flock<Float> {
  float get(int at);
  void set(int at, float val);
  void setArray(int at, float... vals);
  void insert(int at, float... vals);
  void add(float... vals);
  float[] toArray(int from, int size);
  float[] toArray();
}
