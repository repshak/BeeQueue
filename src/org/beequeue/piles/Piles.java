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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.beequeue.util.Morph;


public class Piles {
  public static <T> T fallback(T toTry, T toFallback){
    return toTry == null ? toFallback : toTry ;
  }

  public static <T> Integer find(T[] array, T toSearchFor ) {
    return find(array, toSearchFor, null );
  }
  public static <T> Integer find(T[] array, T toSearchFor, Comparator<T> compare ) {
    return find(Arrays.asList(array), toSearchFor, compare );
  }

  public static <T> Integer find(List<T> list, T toSearchFor) {
    return find(list,toSearchFor,null);
  }
  
  public static <T> Integer find(List<T> list, T toSearchFor, Comparator<T> compare) {
    if( compare == null ){
      compare = new Comparator<T>(){
        public int compare(T o1, T o2) {
          return o1.equals(o2) ? 0 : -1;
        }
      };
    }
    for (int i = 0; i < list.size(); i++) {
      if( compare.compare(toSearchFor, list.get(i)) == 0) {
        return i;
      }
    }
    return null;
  }

  public static Integer findIgnoringCase(String[] array, String toSearchFor) {
    return findIgnoringCase(Arrays.asList(array), toSearchFor );
  }
  
  public static Integer findIgnoringCase(List<String> list, String toSearchFor) {
    Comparator<String> compare = new Comparator<String>(){
      public int compare(String o1, String o2) {
        return o1.equalsIgnoreCase(o2) ? 0 : -1;
      }
    };
    return find(list, toSearchFor, compare);
  }
  
  public static <K,K2,V2> SortedMap<K2,V2> ensureSortedMap(Map<K,SortedMap<K2,V2>> parent, K key){
    return ensureAny(parent, key, new Morph<Void, SortedMap<K2,V2>>(){
      public SortedMap<K2,V2> doIt(Void input) {
        return new TreeMap<K2, V2>();
      }}) ;
  }
  public static <K,K2,V2> Map<K2,V2> ensureMap(Map<K,Map<K2,V2>> parent, K key){
    return ensureAny(parent, key, new Morph<Void, Map<K2,V2>>(){
      public Map<K2,V2> doIt(Void input) {
        return new HashMap<K2, V2>();
      }}) ;
  }
  
  public static <K,V> List<V> ensureList(Map<K,List<V>> parent, K key){
    return ensureAny(parent, key, new Morph<Void, List<V>>(){
      public List<V> doIt(Void input) {
        return new ArrayList<V>();
      }}) ;
  }

  public static <K,V> V ensureAny(Map<K,V> parent, K key, Morph<Void,V> coustruct){
    V val = parent.get(key);
    if(val==null){
      val = coustruct.doIt(null);
      parent.put(key, val);
    }   
    return val ;
  }

  public static <K,T>  void rename(Map<K, T> map, K oldKey, K newKey) {
    T rename = map.remove(oldKey);
    if(rename != null){
      map.put(newKey, rename);
    }
  }
  public static <T,C extends Collection<T>> C addAll(C collection, Iterable<T> toAdd) {
    for (T t : toAdd) {
      collection.add(t);
    }
    return collection;
  }
  
  public static <T> String buildListString(Collection<T> list, String separator, Morph<? super T,String> morph ){
    StringBuilder sb = new StringBuilder();
    for (T t : list) {
      if( sb.length() > 0 ){
        sb.append(separator);
      }
      sb.append(morph.doIt(t));
    }
    return sb.toString();
  }

}
