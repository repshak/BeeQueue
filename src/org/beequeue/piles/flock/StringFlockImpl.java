package org.beequeue.piles.flock;



public class StringFlockImpl extends AnyFlock<String> implements StringFlock{
  public static StringFlock build(String...vals){
    StringFlockImpl flock = new StringFlockImpl(vals.length);
    for (int i = 0; i < vals.length; i++) {
      flock.add(vals[i]);
    }
    return flock;
  }

  public StringFlockImpl() {
    super();
  }

  public StringFlockImpl(AnyFlock<String> src, int at, int size) {
    super(src, at, size);
  }

  public StringFlockImpl(AnyFlock<String> source) {
    super(source);
  }

  public StringFlockImpl(int delta, String empty, int size) {
    super(delta, empty, size);
  }

  public StringFlockImpl(int size) {
    super(size);
  }

  public String get(int at) {
    return getValue(at);
  }

  public void set(int at, String val) {
    checkAt(at);
    this.data[at]=val;
  }

  public void setArray(int at, String... values) {
    checkAt(at);
    System.arraycopy(values, 0, this.data, at, values.length);
  }

  public String[] toArray(int from, int size) {
    return toArray(String.class, from, size);
  }

  public String[] toArray() {
    return toArray(String.class);
  }

}
