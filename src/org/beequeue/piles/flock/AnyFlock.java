package org.beequeue.piles.flock;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.lang.reflect.Array;

public class AnyFlock<T> extends AbstractFlock<T> implements Flock<T> {
  protected Object[] data;
  
  public AnyFlock() {
    super();
    initData(0,null);
    this.size = 0 ;
  }

  public  AnyFlock(AnyFlock<T> src,int at, int size) {
    super(src.delta, src.getEmpty());
    if(size > 0){
      src.checkAt(at);
      src.checkAt(at+size-1);
    }
    initData(size,null);
    this.size = size ;
    if(size > 0){
      System.arraycopy(src.data, at, this.data, 0, size);
    }
  }

  public  AnyFlock(AnyFlock<T> source) {
    this(source,0,source.size());
  }
  
  public AnyFlock(int delta, T empty, int size) {
    super(delta, empty);
    initData(size,null);
    this.size = 0 ;
  }
  
  public AnyFlock(int size) {
    super();
    initData(size,null);
    this.size = 0 ;
  }

  
  @SuppressWarnings("unchecked")
  public static<T> AnyFlock<T> build(T...vals){
    AnyFlock<T> flock = new AnyFlock<T>(vals.length);
    for (int i = 0; i < vals.length; i++) {
      flock.add(vals[i]);
    }
    return flock;
  }

  protected void initData(int size, FlockCommand[] cmds) {
    int calcCapacity = calcCapacity(size);
    Object[] newData = new Object[calcCapacity];
    if(cmds!=null){
      for (int i = 0; i < cmds.length; i++) {
        cmds[i].doIt(this.data,newData);
      }
    }
    this.data = newData;
  }

  @Override protected void allocate(int newCapacity, FlockCommand... cmds) {
    if(newCapacity==getCapacity()){
      for (int i = 0; i < cmds.length; i++) {
        cmds[i].doIt(this.data,this.data);
      }
    }else{
      initData(newCapacity, cmds);
    }
  }

  @Override protected FillCommand fill(int to, int size,final boolean doFill) {
    return new FillCommand(to,size){
      @Override public void doIt(Object source, Object destination) {
        if( doFill ) fill((Object[])destination, getEmpty());
      }
    };
  }

  @SuppressWarnings("unchecked")
  public T getValue(int at) {
    checkAt(at);
    return (T) this.data[at];
  }

  public void setValue(int at, T value) {
    checkAt(at);
    this.data[at] = value;
  }

  public void swap(int a, int b) {
    Object value = this.data[a];
    this.data[a] = this.data[b];
    this.data[b] = value;
  }


  public int getCapacity() {
    return data.length;
  }
  
  @SuppressWarnings("unchecked")
  public T[] toArray(Class<T> type, int from, int size){
    T[] array = (T[]) Array.newInstance(type, size);
    for (int i = 0; i < size; i++) {
      array[i] = getValue(from+i);
    }
    return array;
  }

  public T[] toArray(Class<T> type){
    return toArray(type, 0, size);
  }

  public void add(T... vals){
    insert(this.size, vals);
  }

  public void insert(int at, T ... vals){
    realocate(at, vals.length, false, false);
    for (int i = 0; i < vals.length; i++) {
      this.data[at+i] = vals[i];
    }
  }

  @Override
  protected void readData(ObjectInput in) throws IOException, ClassNotFoundException {
    initData(this.size,null);
    for (int i = 0; i < this.size; i++) {
      data[i]=in.readObject();
    }
  }

  @Override
  protected void writeData(ObjectOutput out) throws IOException {
    for (int i = 0; i < this.size; i++) {
      out.writeObject(data[i]);
    }
  }

  public void addValue(T val) {
    int at = this.size;
    realocate(at, 1, false, false);
    this.data[at] = val;
  }

  public Flock<T> subFlock(int start, int end) {
    return new AnyFlock<T>(this,start,end-start);
  }

}
