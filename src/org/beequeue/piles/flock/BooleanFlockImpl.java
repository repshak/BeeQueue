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

public class BooleanFlockImpl 
extends AbstractFlock<Boolean> 
implements BooleanFlock {
  private boolean[] data = null;

  public BooleanFlockImpl() {
    super();
    initData(0,null);
    this.size = 0 ;
  }

  public Flock<Boolean> subFlock(int start, int end) {
    return new BooleanFlockImpl(this,start,end-start);
  }

  public  BooleanFlockImpl(BooleanFlock src,int at, int size) {
    super(src.getDelta(), src.getEmpty());
    BooleanFlockImpl srcImpl = (BooleanFlockImpl)src;
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

  public  BooleanFlockImpl(BooleanFlock source) {
    this(source,0,source.size());
  }
  
  public BooleanFlockImpl(int delta, boolean empty, int size) {
    super(delta, empty);
    initData(size,null);
    this.size = 0 ;
  }
  
  public BooleanFlockImpl(int size) {
    initData(size,null);
    this.size = 0 ;
  }

  
  public static<T> BooleanFlock build(boolean...vals){
    BooleanFlock flock = new BooleanFlockImpl(vals.length);
    flock.add(vals);
    return flock;
  }

  protected void initData(int size, FlockCommand[] cmds) {
    int calcCapacity = calcCapacity(size);
    boolean[] newData = new boolean[calcCapacity];
    if(cmds!=null){
      for (int i = 0; i < cmds.length; i++) {
        cmds[i].doIt(this.data,newData);
      }
    }
    if( this.getEmpty() == null){
      this.setEmpty(false);
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
        if( doFill ) fill((boolean[])destination, getEmpty());
      }
    };
  }

  @Override
  protected void readData(ObjectInput in) throws IOException, ClassNotFoundException {
    initData(this.size,null);
    for (int i = 0; i < this.size; i++) {
      data[i]=in.readBoolean();
    }
  }

  @Override
  protected void writeData(ObjectOutput out) throws IOException {
    for (int i = 0; i < this.size; i++) {
      out.writeBoolean(data[i]);
    }
  }

  public void add(boolean... values) {
    insert(size(), values);
  }

  public boolean get(int at) {
    checkAt(at);
    return this.data[at];
  }

  
  @Override
  protected Boolean readEmpty(ObjectInput in) throws ClassNotFoundException, IOException {
    return in.readBoolean();
  }

  @Override
  protected void writeEmpty(ObjectOutput out) throws IOException {
    out.writeBoolean(this.getEmpty());
  }

  public void insert(int at, boolean... vals) {
    realocate(at, vals.length, false, false);
    for (int i = 0; i < vals.length; i++) {
      this.data[at+i] = vals[i];
    }
  }

  public void set(int at, boolean val) {
    checkAt(at);
    this.data[at]=val;
  }
  public void setArray(int at, boolean... values) {
    checkAt(at);
    System.arraycopy(values, 0, this.data, at, values.length);
  }

  public boolean[] toArray(int from, int size) {
    boolean[] array = new boolean[size];
    System.arraycopy(this.data, from, array, 0, size);
    return array;
  }

  public boolean[] toArray() {
    return toArray(0, size());
  }

  public int getCapacity() {
    return data.length;
  }

  public Boolean getValue(int at) {
    checkAt(at);
    return this.data[at];
  }

  public void setValue(int at, Boolean value) {
    checkAt(at);
    this.data[at] = value;
  }

  public void swap(int at1, int at2) {
    boolean val = this.data[at1];
    this.data[at1] = this.data[at2];
    this.data[at2] = val;
  }

  public void addValue(Boolean val) {
    add(val);
  }

}
