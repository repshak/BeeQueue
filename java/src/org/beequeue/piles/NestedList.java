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
package org.beequeue.piles;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.beequeue.util.BeeOperation;


public class NestedList<I,O> implements Iterable<O>, Serializable  {
  private static final long serialVersionUID = 1L;
  private List<O> data = new ArrayList<O>();
  private BeeOperation<I,O> morph ;
  private NestedList<O,?> parent = null;
  
  
  public NestedList(BeeOperation<I, O> morph) {
    this.morph = morph;
  }

  public NestedList<O, ?> getParent() {
    return parent;
  }
  
  synchronized public void setParent(NestedList<O, ?> parent) {
    this.parent = parent;
    for (int i = 0; i < this.data.size() ; i++) {
      parent.add(get(i));
    }
    this.data.clear();
  }

  synchronized public void add(I...vals){
    for (int j = 0; j < vals.length; j++) {
      input(vals[j]);
    }
  }
  
  synchronized public void add(Iterable<I> vals){
    for (I in : vals) {
      input(in);
    }
  }

  private void input(I in) {
    O out = morph.execute(in);
    if(out!=null){
      if(parent != null){
        parent.add(out);
      }else{
        data.add(out);
      }
    }
  }
  

  public Iterator<O> iterator() {
    final Iterator<O> iterator = data.iterator();
    return new Iterator<O>(){

      public boolean hasNext() {
        return iterator.hasNext();
      }

      public O next() {
        return iterator.next();
      }

      public void remove() {
        throw new UnsupportedOperationException();
      }};
  }

  public boolean contains(Object o) {
    return data.contains(o);
  }

  public O get(int index) {
    return data.get(index);
  }

  public int size() {
    return data.size();
  }
  
}
