package org.beequeue.piles.flock;

public abstract class FillCommand extends FlockCommand{
  public FillCommand(int to, int size) {
    super(to,size);
  }
  protected void fill(long[] destination, long empty){
    int upTo = to + size;
    for (int i = to; i < upTo; i++) {
      destination[i] = empty;
    }
  }
  protected void fill(float[] destination, float empty){
    int upTo = to + size;
    for (int i = to; i < upTo; i++) {
      destination[i] = empty;
    }
  }
  protected void fill(int[] destination, int empty){
    int upTo = to + size;
    for (int i = to; i < upTo; i++) {
      destination[i] = empty;
    }
  }
  protected void fill(double[] destination, double empty){
    int upTo = to + size;
    for (int i = to; i < upTo; i++) {
      destination[i] = empty;
    }
  }
  protected void fill(byte[] destination, byte empty){
    int upTo = to + size;
    for (int i = to; i < upTo; i++) {
      destination[i] = empty;
    }
  }
  protected void fill(boolean[] destination, boolean empty){
    int upTo = to + size;
    for (int i = to; i < upTo; i++) {
      destination[i] = empty;
    }
  }
  protected void fill(short[] destination, short empty){
    int upTo = to + size;
    for (int i = to; i < upTo; i++) {
      destination[i] = empty;
    }
  }
  protected void fill(Object[] destination, Object empty){
    int upTo = to + size;
    for (int i = to; i < upTo; i++) {
      destination[i] = empty;
    }
  }
}
