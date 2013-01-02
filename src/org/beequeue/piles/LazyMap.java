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


import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import org.beequeue.util.BeeOperation;
import org.beequeue.util.PassThruTransformation;
import org.beequeue.util.Tuple;


public class LazyMap<K, V> extends LinkedHashMap<K, V> {
  private static final long serialVersionUID = 1L;
  
  public LazyMap() {
  }

  public LazyMap(Collection<Tuple<K, V>> collection) {
    for (Tuple<K, V> tuple : collection) {
      add(tuple);
    }
  }

  public LazyMap(Tuple<K, V>... array) {
    for (Tuple<K, V> tuple : array) {
      add(tuple);
    }
  }
  
  public LazyMap<K, V> add(K k,V v){
    put(k,v);
    return this;
  }
  
  public LazyMap<K, V> add(Map<K,V> m){
    putAll(m);
    return this;
  }
  
  public LazyMap<K, V> add(Tuple<K,V> t){
    return add(t.o1,t.o2);
  }

  public static <K,V> LazyMap<K,V> extractKeys(BeeOperation<V,K> extractKey, Collection<V> collection){
    LazyMap<K, V> lazyMap = new LazyMap<K,V>();
    for (V v : collection) {
      lazyMap.put(extractKey.doIt(v), v);
    }
    return lazyMap;
  }
  public static <K2,K,V> LazyMap<K,V> morphKeys(BeeOperation<K2,K> morphKey, Map<K2,V> toMorph){
    return new LazyMap<K,V>().morphKeysInto(morphKey,toMorph);
  }

  
  public static <V2,K,V> LazyMap<K,V> morphValues(BeeOperation<V2,V> morphValue, Map<K,V2> toMorph){
    return new LazyMap<K,V>().morphValuesInto(morphValue,toMorph);
  }

  public static <K2,V2,K,V> LazyMap<K,V> morph(BeeOperation<K2,K> morphKey, BeeOperation<V2,V> morphValue, Map<K2,V2> toMorph){
    return new LazyMap<K,V>().morphInto(morphKey,morphValue,toMorph);
  }

  public <K2> LazyMap<K,V> morphKeysInto(BeeOperation<K2,K> morphKey, Map<K2,V> toMorph){
    final PassThruTransformation<V> passThruValue = new PassThruTransformation<V>();
    return morphInto(morphKey,passThruValue,toMorph);
  }
  
  public <V2> LazyMap<K,V> morphValuesInto(BeeOperation<V2,V> morphValue, Map<K,V2> toMorph){
    final PassThruTransformation<K> passThruKey = new PassThruTransformation<K>();
    return morphInto(passThruKey,morphValue,toMorph);
  }

  public <K2,V2> LazyMap<K,V> morphInto(BeeOperation<K2,K> morphKey, BeeOperation<V2,V> morphValue, Map<K2,V2> toMorph){
    for (K2 k2 : toMorph.keySet()) {
      put(morphKey.doIt(k2),morphValue.doIt(toMorph.get(k2)));
    }
    return this;
  }
}
