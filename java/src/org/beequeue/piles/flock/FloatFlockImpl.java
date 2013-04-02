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

public class FloatFlockImpl 
extends AbstractFlock<Float> 
implements FloatFlock {
  private float[] data = null;

  public FloatFlockImpl() {
    super();
    initData(0,null);
    this.size = 0 ;
  }

  public Flock<Float> subFlock(int start, int end) {
    return new FloatFlockImpl(this,start,end-start);
  }

  public  FloatFlockImpl(FloatFlock src,int at, int size) {
    super(src.getDelta(), src.getEmpty());
    FloatFlockImpl srcImpl = (FloatFlockImpl)src;
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

  public  FloatFlockImpl(FloatFlock source) {
    this(source,0,source.size());
  }
  
  public FloatFlockImpl(int delta, float empty, int size) {
    super(delta, empty);
    initData(size,null);
    this.size = 0 ;
  }
  
  public FloatFlockImpl(int size) {
    initData(size,null);
    this.size = 0 ;
  }

  
  public static<T> FloatFlock build(float...vals){
    FloatFlock flock = new FloatFlockImpl(vals.length);
    flock.add(vals);
    return flock;
  }

  protected void initData(int size, FlockCommand[] cmds) {
    int calcCapacity = calcCapacity(size);
    float[] newData = new float[calcCapacity];
    if(cmds!=null){
      for (int i = 0; i < cmds.length; i++) {
        cmds[i].doIt(this.data,newData);
      }
    }
    if( this.getEmpty() == null){
      this.setEmpty((float)0);
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
        if( doFill ) fill((float[])destination, getEmpty());
      }
    };
  }

  @Override
  protected void readData(ObjectInput in) throws IOException, ClassNotFoundException {
    initData(this.size,null);
    for (int i = 0; i < this.size; i++) {
      data[i]=in.readFloat();
    }
  }

  @Override
  protected void writeData(ObjectOutput out) throws IOException {
    for (int i = 0; i < this.size; i++) {
      out.writeFloat(data[i]);
    }
  }

  public void add(float... values) {
    insert(size(), values);
  }

  public float get(int at) {
    checkAt(at);
    return this.data[at];
  }

  
  @Override
  protected Float readEmpty(ObjectInput in) throws ClassNotFoundException, IOException {
    return in.readFloat();
  }

  @Override
  protected void writeEmpty(ObjectOutput out) throws IOException {
    out.writeFloat(this.getEmpty());
  }

  public void insert(int at, float... vals) {
    realocate(at, vals.length, false, false);
    for (int i = 0; i < vals.length; i++) {
      this.data[at+i] = vals[i];
    }
  }

  public void set(int at, float val) {
    checkAt(at);
    this.data[at]=val;
  }
  public void setArray(int at, float... values) {
    checkAt(at);
    System.arraycopy(values, 0, this.data, at, values.length);
  }

  public float[] toArray(int from, int size) {
    float[] array = new float[size];
    System.arraycopy(this.data, from, array, 0, size);
    return array;
  }

  public float[] toArray() {
    return toArray(0, size());
  }

  public int getCapacity() {
    return data.length;
  }

  public Float getValue(int at) {
    checkAt(at);
    return this.data[at];
  }

  public void setValue(int at, Float value) {
    checkAt(at);
    this.data[at] = value;
  }

  public void swap(int at1, int at2) {
    float val = this.data[at1];
    this.data[at1] = this.data[at2];
    this.data[at2] = val;
  }

  public void addValue(Float val) {
    add(val);
  }

}
