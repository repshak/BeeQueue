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

import java.io.Externalizable;

public interface Flock<T> extends Externalizable, Iterable<T> {
  int size();
  int getCapacity();
  int getDelta();
  T getEmpty();
  void setEmpty(T emptyValue);
  T getValue(int at);
  void setValue(int at, T value);
  void addValue(T val);
  void swap(int at1, int at2);
  void vecswap(int at1, int at2, int size);
  void remove(int at, int size);
  void insertEmpty(int at, int size);
  void insert(int at, Flock<T> vals);
  void insert(int at, Flock<T> vals, int off, int size);
  Flock<T> subFlock(int start, int end);
  void add(Flock<T> vals);
  void add(Flock<T> vals, int off, int size) ;
}
