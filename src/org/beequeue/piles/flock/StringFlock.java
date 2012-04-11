package org.beequeue.piles.flock;


public interface StringFlock extends Flock<String> {
  String get(int at);
  void setArray(int at, String... vals);
  void set(int at, String vals);
  void add(String... vals);
  void insert(int at, String ... vals);
  String[] toArray(int from, int size);
  String[] toArray();
}
