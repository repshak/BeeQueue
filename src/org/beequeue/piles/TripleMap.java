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

import org.beequeue.util.Triple;
import org.beequeue.util.Tuple;



public class TripleMap<K, V1, V2, V3> extends LazyMap<K, Triple<V1, V2, V3>> {
  private static final long serialVersionUID = 1L;
  
  public TripleMap() {
    super();
  }

  public TripleMap(Collection<Tuple<K, Triple<V1, V2, V3>>> collection) {
    super(collection);
  }

  public TripleMap(Tuple<K, Triple<V1, V2, V3>>... array) {
    super(array);
  }

  public TripleMap<K, V1, V2, V3> add(K k, V1 o1, V2 o2, V3 o3){
    put(k,new Triple<V1, V2, V3>(o1,o2,o3));
    return this;
  }
}
