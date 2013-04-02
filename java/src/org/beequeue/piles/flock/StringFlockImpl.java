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



public class StringFlockImpl extends AnyFlock<String> implements StringFlock{
  public static StringFlock build(String...vals){
    StringFlockImpl flock = new StringFlockImpl(vals.length);
    for (int i = 0; i < vals.length; i++) {
      flock.add(vals[i]);
    }
    return flock;
  }

  public StringFlockImpl() {
    super();
  }

  public StringFlockImpl(AnyFlock<String> src, int at, int size) {
    super(src, at, size);
  }

  public StringFlockImpl(AnyFlock<String> source) {
    super(source);
  }

  public StringFlockImpl(int delta, String empty, int size) {
    super(delta, empty, size);
  }

  public StringFlockImpl(int size) {
    super(size);
  }

  public String get(int at) {
    return getValue(at);
  }

  public void set(int at, String val) {
    checkAt(at);
    this.data[at]=val;
  }

  public void setArray(int at, String... values) {
    checkAt(at);
    System.arraycopy(values, 0, this.data, at, values.length);
  }

  public String[] toArray(int from, int size) {
    return toArray(String.class, from, size);
  }

  public String[] toArray() {
    return toArray(String.class);
  }

}
