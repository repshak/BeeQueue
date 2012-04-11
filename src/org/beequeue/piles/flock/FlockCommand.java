package org.beequeue.piles.flock;

import org.beequeue.util.ToStringUtil;

public abstract class FlockCommand {
  public final int to;
  public final int size;
  
  public FlockCommand(int to, int size) {
    this.to = to;
    this.size = size;
  }
  public abstract void doIt(Object source, Object destination);
  @Override
  public String toString() {
    return ToStringUtil.toString(this);
  }

  
}
