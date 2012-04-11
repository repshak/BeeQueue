package org.beequeue.piles.flock;

import java.util.Iterator;

public class FlockIteratorIndex<T> implements Iterator<T> {
  private IntegerFlock index;
  private Flock<T> flock;
  private int pos = 0 ;

  public FlockIteratorIndex(Flock<T> flock, IntegerFlock index) {
    this.flock = flock ;
    this.index = index ;
  }

  public boolean hasNext() {
    return pos < index.size();
  }

  public T next() {
    return flock.getValue(index.get(pos++));
  }

  public void remove() {
    throw new UnsupportedOperationException();
  }
}
