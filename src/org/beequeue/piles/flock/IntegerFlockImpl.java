/* This file is generated out of 
 * java/kernel/num/cracker/flock/impl/DoubleFlockImpl.java
 * Please do not edit. 
 *
 * Yours truly, DoubleFlockCodegen
 */
package org.beequeue.piles.flock;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class IntegerFlockImpl 
extends AbstractFlock<Integer> 
implements IntegerFlock {
  private int[] data = null;

  public IntegerFlockImpl() {
    super();
    initData(0,null);
    this.size = 0 ;
  }

  public Flock<Integer> subFlock(int start, int end) {
    return new IntegerFlockImpl(this,start,end-start);
  }

  public  IntegerFlockImpl(IntegerFlock src,int at, int size) {
    super(src.getDelta(), src.getEmpty());
    IntegerFlockImpl srcImpl = (IntegerFlockImpl)src;
    if(size > 0){
      srcImpl.checkAt(at);
      srcImpl.checkAt(at+size-1);
    }
    initData(size,null);
    this.size = size ;
    if(size > 0){
      System.arraycopy(srcImpl.data, at, this.data, 0, size);
    }
  }

  public  IntegerFlockImpl(IntegerFlock source) {
    this(source,0,source.size());
  }
  
  public IntegerFlockImpl(int delta, int empty, int size) {
    super(delta, empty);
    initData(size,null);
    this.size = 0 ;
  }
  
  public IntegerFlockImpl(int size) {
    initData(size,null);
    this.size = 0 ;
  }

  
  public static<T> IntegerFlock build(int...vals){
    IntegerFlock flock = new IntegerFlockImpl(vals.length);
    flock.add(vals);
    return flock;
  }

  protected void initData(int size, FlockCommand[] cmds) {
    int calcCapacity = calcCapacity(size);
    int[] newData = new int[calcCapacity];
    if(cmds!=null){
      for (int i = 0; i < cmds.length; i++) {
        cmds[i].doIt(this.data,newData);
      }
    }
    if( this.getEmpty() == null){
      this.setEmpty((int)0);
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
        if( doFill ) fill((int[])destination, getEmpty());
      }
    };
  }

  @Override
  protected void readData(ObjectInput in) throws IOException, ClassNotFoundException {
    initData(this.size,null);
    for (int i = 0; i < this.size; i++) {
      data[i]=in.readInt();
    }
  }

  @Override
  protected void writeData(ObjectOutput out) throws IOException {
    for (int i = 0; i < this.size; i++) {
      out.writeInt(data[i]);
    }
  }

  public void add(int... values) {
    insert(size(), values);
  }

  public int get(int at) {
    checkAt(at);
    return this.data[at];
  }

  
  @Override
  protected Integer readEmpty(ObjectInput in) throws ClassNotFoundException, IOException {
    return in.readInt();
  }

  @Override
  protected void writeEmpty(ObjectOutput out) throws IOException {
    out.writeInt(this.getEmpty());
  }

  public void insert(int at, int... vals) {
    realocate(at, vals.length, false, false);
    for (int i = 0; i < vals.length; i++) {
      this.data[at+i] = vals[i];
    }
  }

  public void set(int at, int val) {
    checkAt(at);
    this.data[at]=val;
  }
  public void setArray(int at, int... values) {
    checkAt(at);
    System.arraycopy(values, 0, this.data, at, values.length);
  }

  public int[] toArray(int from, int size) {
    int[] array = new int[size];
    System.arraycopy(this.data, from, array, 0, size);
    return array;
  }

  public int[] toArray() {
    return toArray(0, size());
  }

  public int getCapacity() {
    return data.length;
  }

  public Integer getValue(int at) {
    checkAt(at);
    return this.data[at];
  }

  public void setValue(int at, Integer value) {
    checkAt(at);
    this.data[at] = value;
  }

  public void swap(int at1, int at2) {
    int val = this.data[at1];
    this.data[at1] = this.data[at2];
    this.data[at2] = val;
  }

  public void addValue(Integer val) {
    add(val);
  }

}
