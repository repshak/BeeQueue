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

public class ShortFlockImpl 
extends AbstractFlock<Short> 
implements ShortFlock {
  private short[] data = null;

  public ShortFlockImpl() {
    super();
    initData(0,null);
    this.size = 0 ;
  }

  public Flock<Short> subFlock(int start, int end) {
    return new ShortFlockImpl(this,start,end-start);
  }

  public  ShortFlockImpl(ShortFlock src,int at, int size) {
    super(src.getDelta(), src.getEmpty());
    ShortFlockImpl srcImpl = (ShortFlockImpl)src;
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

  public  ShortFlockImpl(ShortFlock source) {
    this(source,0,source.size());
  }
  
  public ShortFlockImpl(int delta, short empty, int size) {
    super(delta, empty);
    initData(size,null);
    this.size = 0 ;
  }
  
  public ShortFlockImpl(int size) {
    initData(size,null);
    this.size = 0 ;
  }

  
  public static<T> ShortFlock build(short...vals){
    ShortFlock flock = new ShortFlockImpl(vals.length);
    flock.add(vals);
    return flock;
  }

  protected void initData(int size, FlockCommand[] cmds) {
    int calcCapacity = calcCapacity(size);
    short[] newData = new short[calcCapacity];
    if(cmds!=null){
      for (int i = 0; i < cmds.length; i++) {
        cmds[i].doIt(this.data,newData);
      }
    }
    if( this.getEmpty() == null){
      this.setEmpty((short)0);
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
        if( doFill ) fill((short[])destination, getEmpty());
      }
    };
  }

  @Override
  protected void readData(ObjectInput in) throws IOException, ClassNotFoundException {
    initData(this.size,null);
    for (int i = 0; i < this.size; i++) {
      data[i]=in.readShort();
    }
  }

  @Override
  protected void writeData(ObjectOutput out) throws IOException {
    for (int i = 0; i < this.size; i++) {
      out.writeShort(data[i]);
    }
  }

  public void add(short... values) {
    insert(size(), values);
  }

  public short get(int at) {
    checkAt(at);
    return this.data[at];
  }

  
  @Override
  protected Short readEmpty(ObjectInput in) throws ClassNotFoundException, IOException {
    return in.readShort();
  }

  @Override
  protected void writeEmpty(ObjectOutput out) throws IOException {
    out.writeShort(this.getEmpty());
  }

  public void insert(int at, short... vals) {
    realocate(at, vals.length, false, false);
    for (int i = 0; i < vals.length; i++) {
      this.data[at+i] = vals[i];
    }
  }

  public void set(int at, short val) {
    checkAt(at);
    this.data[at]=val;
  }
  public void setArray(int at, short... values) {
    checkAt(at);
    System.arraycopy(values, 0, this.data, at, values.length);
  }

  public short[] toArray(int from, int size) {
    short[] array = new short[size];
    System.arraycopy(this.data, from, array, 0, size);
    return array;
  }

  public short[] toArray() {
    return toArray(0, size());
  }

  public int getCapacity() {
    return data.length;
  }

  public Short getValue(int at) {
    checkAt(at);
    return this.data[at];
  }

  public void setValue(int at, Short value) {
    checkAt(at);
    this.data[at] = value;
  }

  public void swap(int at1, int at2) {
    short val = this.data[at1];
    this.data[at1] = this.data[at2];
    this.data[at2] = val;
  }

  public void addValue(Short val) {
    add(val);
  }

}
