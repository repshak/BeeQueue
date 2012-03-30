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
package beehive.piles;

import java.util.Collection;

import beehive.util.Tuple;


public class TupleMap<K, V1, V2> extends LazyMap<K, Tuple<V1, V2>> {
  private static final long serialVersionUID = 1L;

  public TupleMap() {
    super();
  }

  public TupleMap(Collection<Tuple<K, Tuple<V1, V2>>> collection) {
    super(collection);
  }

  public TupleMap(Tuple<K, Tuple<V1, V2>>... array) {
    super(array);
  }
  
  public TupleMap<K, V1, V2> add(K k, V1 o1, V2 o2){
    put(k,new Tuple<V1, V2>(o1,o2));
    return this;
  }
}
