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

public class ByteFlockImpl 
extends AbstractFlock<Byte> 
implements ByteFlock {
  private byte[] data = null;

  public ByteFlockImpl() {
    super();
    initData(0,null);
    this.size = 0 ;
  }

  public Flock<Byte> subFlock(int start, int end) {
    return new ByteFlockImpl(this,start,end-start);
  }

  public  ByteFlockImpl(ByteFlock src,int at, int size) {
    super(src.getDelta(), src.getEmpty());
    ByteFlockImpl srcImpl = (ByteFlockImpl)src;
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

  public  ByteFlockImpl(ByteFlock source) {
    this(source,0,source.size());
  }
  
  public ByteFlockImpl(int delta, byte empty, int size) {
    super(delta, empty);
    initData(size,null);
    this.size = 0 ;
  }
  
  public ByteFlockImpl(int size) {
    initData(size,null);
    this.size = 0 ;
  }

  
  public static<T> ByteFlock build(byte...vals){
    ByteFlock flock = new ByteFlockImpl(vals.length);
    flock.add(vals);
    return flock;
  }

  protected void initData(int size, FlockCommand[] cmds) {
    int calcCapacity = calcCapacity(size);
    byte[] newData = new byte[calcCapacity];
    if(cmds!=null){
      for (int i = 0; i < cmds.length; i++) {
        cmds[i].doIt(this.data,newData);
      }
    }
    if( this.getEmpty() == null){
      this.setEmpty((byte)0);
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
        if( doFill ) fill((byte[])destination, getEmpty());
      }
    };
  }

  @Override
  protected void readData(ObjectInput in) throws IOException, ClassNotFoundException {
    initData(this.size,null);
    for (int i = 0; i < this.size; i++) {
      data[i]=in.readByte();
    }
  }

  @Override
  protected void writeData(ObjectOutput out) throws IOException {
    for (int i = 0; i < this.size; i++) {
      out.writeByte(data[i]);
    }
  }

  public void add(byte... values) {
    insert(size(), values);
  }

  public byte get(int at) {
    checkAt(at);
    return this.data[at];
  }

  
  @Override
  protected Byte readEmpty(ObjectInput in) throws ClassNotFoundException, IOException {
    return in.readByte();
  }

  @Override
  protected void writeEmpty(ObjectOutput out) throws IOException {
    out.writeByte(this.getEmpty());
  }

  public void insert(int at, byte... vals) {
    realocate(at, vals.length, false, false);
    for (int i = 0; i < vals.length; i++) {
      this.data[at+i] = vals[i];
    }
  }

  public void set(int at, byte val) {
    checkAt(at);
    this.data[at]=val;
  }
  public void setArray(int at, byte... values) {
    checkAt(at);
    System.arraycopy(values, 0, this.data, at, values.length);
  }

  public byte[] toArray(int from, int size) {
    byte[] array = new byte[size];
    System.arraycopy(this.data, from, array, 0, size);
    return array;
  }

  public byte[] toArray() {
    return toArray(0, size());
  }

  public int getCapacity() {
    return data.length;
  }

  public Byte getValue(int at) {
    checkAt(at);
    return this.data[at];
  }

  public void setValue(int at, Byte value) {
    checkAt(at);
    this.data[at] = value;
  }

  public void swap(int at1, int at2) {
    byte val = this.data[at1];
    this.data[at1] = this.data[at2];
    this.data[at2] = val;
  }

  public void addValue(Byte val) {
    add(val);
  }

}
