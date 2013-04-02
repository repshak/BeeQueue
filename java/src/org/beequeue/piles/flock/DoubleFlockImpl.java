/** ==== BEGIN LICENSE =====
   Copyright 2012 - BeeQueue.org

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an &quot;AS IS&quot; BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 
 *  ===== END LICENSE ====== */
package org.beequeue.piles.flock;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class DoubleFlockImpl 
extends AbstractFlock<Double> 
implements DoubleFlock {
  private double[] data = null;

  public DoubleFlockImpl() {
    super();
    initData(0,null);
    this.size = 0 ;
  }

  public Flock<Double> subFlock(int start, int end) {
    return new DoubleFlockImpl(this,start,end-start);
  }

  public  DoubleFlockImpl(DoubleFlock src,int at, int size) {
    super(src.getDelta(), src.getEmpty());
    DoubleFlockImpl srcImpl = (DoubleFlockImpl)src;
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

  public  DoubleFlockImpl(DoubleFlock source) {
    this(source,0,source.size());
  }
  
  public DoubleFlockImpl(int delta, double empty, int size) {
    super(delta, empty);
    initData(size,null);
    this.size = 0 ;
  }
  
  public DoubleFlockImpl(int size) {
    initData(size,null);
    this.size = 0 ;
  }

  
  public static<T> DoubleFlock build(double...vals){
    DoubleFlock flock = new DoubleFlockImpl(vals.length);
    flock.add(vals);
    return flock;
  }

  protected void initData(int size, FlockCommand[] cmds) {
    int calcCapacity = calcCapacity(size);
    double[] newData = new double[calcCapacity];
    if(cmds!=null){
      for (int i = 0; i < cmds.length; i++) {
        cmds[i].doIt(this.data,newData);
      }
    }
    if( this.getEmpty() == null){
      this.setEmpty((double)0);
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
        if( doFill ) fill((double[])destination, getEmpty());
      }
    };
  }

  @Override
  protected void readData(ObjectInput in) throws IOException, ClassNotFoundException {
    initData(this.size,null);
    for (int i = 0; i < this.size; i++) {
      data[i]=in.readDouble();
    }
  }

  @Override
  protected void writeData(ObjectOutput out) throws IOException {
    for (int i = 0; i < this.size; i++) {
      out.writeDouble(data[i]);
    }
  }

  public void add(double... values) {
    insert(size(), values);
  }

  public double get(int at) {
    checkAt(at);
    return this.data[at];
  }

  
  @Override
  protected Double readEmpty(ObjectInput in) throws ClassNotFoundException, IOException {
    return in.readDouble();
  }

  @Override
  protected void writeEmpty(ObjectOutput out) throws IOException {
    out.writeDouble(this.getEmpty());
  }

  public void insert(int at, double... vals) {
    realocate(at, vals.length, false, false);
    for (int i = 0; i < vals.length; i++) {
      this.data[at+i] = vals[i];
    }
  }

  public void set(int at, double val) {
    checkAt(at);
    this.data[at]=val;
  }
  public void setArray(int at, double... values) {
    checkAt(at);
    System.arraycopy(values, 0, this.data, at, values.length);
  }

  public double[] toArray(int from, int size) {
    double[] array = new double[size];
    System.arraycopy(this.data, from, array, 0, size);
    return array;
  }

  public double[] toArray() {
    return toArray(0, size());
  }

  public int getCapacity() {
    return data.length;
  }

  public Double getValue(int at) {
    checkAt(at);
    return this.data[at];
  }

  public void setValue(int at, Double value) {
    checkAt(at);
    this.data[at] = value;
  }

  public void swap(int at1, int at2) {
    double val = this.data[at1];
    this.data[at1] = this.data[at2];
    this.data[at2] = val;
  }

  public void addValue(Double val) {
    add(val);
  }

}
