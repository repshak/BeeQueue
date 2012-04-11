/**
 * 
 */
package org.beequeue.piles.flock;

import java.util.Iterator;

final public class FlockIterator<T> implements Iterator<T> {
  private Flock<T> flock;
  private int pos = 0 ;

  public FlockIterator(Flock<T> flock) {
    this.flock = flock ;
  }

  public boolean hasNext() {
    return pos < flock.size();
  }

  public T next() {
    return flock.getValue(pos++);
  }

  public void remove() {
    throw new UnsupportedOperationException();
  }
}