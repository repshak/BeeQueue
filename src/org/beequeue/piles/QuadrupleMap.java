/** ==== BEGIN LICENSE =====
   Copyright 2004-2007 - Wakeup ORM

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

import java.util.Collection;

import org.beequeue.util.Quadruple;
import org.beequeue.util.Tuple;



public class QuadrupleMap<K, V1, V2, V3, V4> extends LazyMap<K, Quadruple<V1, V2, V3, V4>> {
  private static final long serialVersionUID = 1L;

  public QuadrupleMap() {
    super();
  }

  public QuadrupleMap(Collection<Tuple<K, Quadruple<V1, V2, V3, V4>>> collection) {
    super(collection);
  }

  public QuadrupleMap(Tuple<K, Quadruple<V1, V2, V3, V4>>... array) {
    super(array);
  }

  public QuadrupleMap<K, V1, V2, V3,V4> add(K k, V1 o1, V2 o2, V3 o3, V4 o4){
    put(k,new Quadruple<V1, V2, V3, V4>(o1,o2,o3,o4));
    return this;
  }
}
