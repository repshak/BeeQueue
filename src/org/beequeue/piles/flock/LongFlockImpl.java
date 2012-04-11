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

public class LongFlockImpl 
extends AbstractFlock<Long> 
implements LongFlock {
  private long[] data = null;

  public LongFlockImpl() {
    super();
    initData(0,null);
    this.size = 0 ;
  }

  public Flock<Long> subFlock(int start, int end) {
    return new LongFlockImpl(this,start,end-start);
  }

  public  LongFlockImpl(LongFlock src,int at, int size) {
    super(src.getDelta(), src.getEmpty());
    LongFlockImpl srcImpl = (LongFlockImpl)src;
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

  public  LongFlockImpl(LongFlock source) {
    this(source,0,source.size());
  }
  
  public LongFlockImpl(int delta, long empty, int size) {
    super(delta, empty);
    initData(size,null);
    this.size = 0 ;
  }
  
  public LongFlockImpl(int size) {
    initData(size,null);
    this.size = 0 ;
  }

  
  public static<T> LongFlock build(long...vals){
    LongFlock flock = new LongFlockImpl(vals.length);
    flock.add(vals);
    return flock;
  }

  protected void initData(int size, FlockCommand[] cmds) {
    int calcCapacity = calcCapacity(size);
    long[] newData = new long[calcCapacity];
    if(cmds!=null){
      for (int i = 0; i < cmds.length; i++) {
        cmds[i].doIt(this.data,newData);
      }
    }
    if( this.getEmpty() == null){
      this.setEmpty((long)0);
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
        if( doFill ) fill((long[])destination, getEmpty());
      }
    };
  }

  @Override
  protected void readData(ObjectInput in) throws IOException, ClassNotFoundException {
    initData(this.size,null);
    for (int i = 0; i < this.size; i++) {
      data[i]=in.readLong();
    }
  }

  @Override
  protected void writeData(ObjectOutput out) throws IOException {
    for (int i = 0; i < this.size; i++) {
      out.writeLong(data[i]);
    }
  }

  public void add(long... values) {
    insert(size(), values);
  }

  public long get(int at) {
    checkAt(at);
    return this.data[at];
  }

  
  @Override
  protected Long readEmpty(ObjectInput in) throws ClassNotFoundException, IOException {
    return in.readLong();
  }

  @Override
  protected void writeEmpty(ObjectOutput out) throws IOException {
    out.writeLong(this.getEmpty());
  }

  public void insert(int at, long... vals) {
    realocate(at, vals.length, false, false);
    for (int i = 0; i < vals.length; i++) {
      this.data[at+i] = vals[i];
    }
  }

  public void set(int at, long val) {
    checkAt(at);
    this.data[at]=val;
  }
  public void setArray(int at, long... values) {
    checkAt(at);
    System.arraycopy(values, 0, this.data, at, values.length);
  }

  public long[] toArray(int from, int size) {
    long[] array = new long[size];
    System.arraycopy(this.data, from, array, 0, size);
    return array;
  }

  public long[] toArray() {
    return toArray(0, size());
  }

  public int getCapacity() {
    return data.length;
  }

  public Long getValue(int at) {
    checkAt(at);
    return this.data[at];
  }

  public void setValue(int at, Long value) {
    checkAt(at);
    this.data[at] = value;
  }

  public void swap(int at1, int at2) {
    long val = this.data[at1];
    this.data[at1] = this.data[at2];
    this.data[at2] = val;
  }

  public void addValue(Long val) {
    add(val);
  }

}
