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
import java.util.Iterator;

import org.beequeue.util.Nulls;


public abstract class AbstractFlock<T> 
implements Flock<T>{
  protected int delta = 512 ;
  protected int size = 0;
  private T empty = null ;
  
  public AbstractFlock() {
  }

  public AbstractFlock(int delta, T empty) {
    this.delta = delta;
    this.empty = empty;
  }

  public T getEmpty() {
    return empty;
  }

  protected abstract void allocate(int newCapacity, FlockCommand... copy);
  
  protected abstract FillCommand fill(int to, int size, boolean doFill);
  
  protected CopyCommand copy(int from, int to, int size){
    return new CopyCommand(from,to,size);
  }
  
  final protected  void checkAt(int at) {
    if(at < 0 || at >= size){
      throw new ArrayIndexOutOfBoundsException("size="+size+" at="+at);
    }
  }
  
  
  final public int getDelta() {
    return delta;
  }

  protected void realocate(int at,int insert,boolean doFill, boolean doShrink){
    if(insert == 0)return;
    int newSize = size + insert;
    if(  at > newSize || at > size  ){
      throw new ArrayIndexOutOfBoundsException("size="+size+" at="+at+" newSize="+newSize);
    }
    int newCapacity = calcCapacity(newSize);
    boolean changeCapacity;
    if( !doShrink && newCapacity < getCapacity()){
      newCapacity = getCapacity();
      changeCapacity = false;
    }else{
      changeCapacity = newCapacity != getCapacity();
    }
    boolean grow = insert > 0;
    if(changeCapacity){
      if(grow){
        allocate(newCapacity, 
            copy(0, 0, at),
            copy(at,at+insert,size - at),
            fill(at, insert, doFill));
      }else{
        int copyFrom = at-insert;
        allocate(newCapacity, 
            copy(0, 0, at),
            copy(copyFrom,at,size-copyFrom));
      }
    }else{
      if(grow){
        allocate(newCapacity, 
            copy(at,at+insert,size - at),
            fill(at, insert, doFill));
      }else{
        int copyFrom = at-insert;
        allocate(newCapacity, 
            copy(copyFrom,at,size-copyFrom));
      }
    }
    size = newSize;
  }

  final protected int calcCapacity(int newSize) {
    int div = newSize / delta;
    if( div == 0 || newSize % delta > 0 ){
      div++;
    }
    return div * delta;
  }

  public void insertEmpty(int at, int size) {
    realocate(at,size,true,false);
  }

  public void remove(int at, int size) {
    realocate(at,-size,false,true);
  }

  public void setEmpty(T emptyValue) {
    this.empty = emptyValue;
  }

  public String toString() {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < size(); i++) {
      if(i > 0){
        sb.append(",");
      }
      sb.append(getValue(i));
    }
    return sb.toString();
  }
  
  @SuppressWarnings("unchecked")
  public boolean equals(Object obj) {
    if (obj instanceof Flock) {
      Flock<T> that = (Flock<T>) obj;
      if( size()  == that.size()){
        for (int i = 0; i < size(); i++) {
          if( !Nulls.equals(getValue(i) , that.getValue(i)) ){
            return false;
          }
        }
        return true;
      }
    }
    return false;
  }

  public int hashCode() {
    return -1; // mutable object is not good subject for hashCode
  }

  public void vecswap(int at1, int at2, int size) {
    for (int i = 0; i < size; at1++, at2++, i++) swap(at1, at2);
  }

  public int size() {
    return size;
  }
  
  public void add(Flock<T> vals) {
    insert(this.size, vals);
  }
  
  public void insert(int at, Flock<T> vals) {
    insert(at, vals, 0, vals.size());
  }

  public void add(Flock<T> vals, int off, int size) {
    insert(size(), vals, off, size);
  }

  public void insert(int at, Flock<T> vals, int off, int size) {
    realocate(at, size, false, false);
    for (int i = 0; i < size; i++) {
      this.setValue(at+i, vals.getValue(off+i));
    }
  }

  public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
    this.delta = in.readInt();
    this.size = in.readInt();
    this.empty = readEmpty(in);
    readData(in);
  }

  @SuppressWarnings("unchecked")
  protected T readEmpty(ObjectInput in) throws ClassNotFoundException, IOException {
    return (T) in.readObject();
  }
  
  protected abstract void writeData(ObjectOutput out)throws IOException;
  protected abstract void readData(ObjectInput in)throws IOException, ClassNotFoundException;

  public void writeExternal(ObjectOutput out) throws IOException {
    out.writeInt(this.delta);
    out.writeInt(this.size);
    writeEmpty(out);
    writeData(out);
  }

  protected void writeEmpty(ObjectOutput out) throws IOException {
    out.writeObject(this.empty);
  }

  public Iterator<T> iterator() {
    return new FlockIterator<T>(this);
  }

  
}
