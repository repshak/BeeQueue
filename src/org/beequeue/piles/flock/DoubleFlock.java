package org.beequeue.piles.flock;

public interface DoubleFlock extends Flock<Double> {
  double get(int at);
  void set(int at, double val);
  void setArray(int at, double... vals);
  void insert(int at, double... vals);
  void add(double... vals);
  double[] toArray(int from, int size);
  double[] toArray();
}
