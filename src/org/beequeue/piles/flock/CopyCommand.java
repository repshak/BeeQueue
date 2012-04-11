package org.beequeue.piles.flock;

public class CopyCommand extends FlockCommand{
  public final int from;

  public CopyCommand( int from, int to, int size) {
    super(to,size);
    this.from = from;
  }
  
  public void doIt(Object source, Object destination){
    if(size > 0){
      System.arraycopy(source, from, destination, to, size);
    }else if(size < 0){
      throw new ArrayIndexOutOfBoundsException(toString());
    }
    //size==0 - do nothing
  }
}
