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
package org.beequeue.sql.mapping;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.beequeue.piles.LazyMap;
import org.beequeue.util.BeeException;
import org.beequeue.util.BeeOperation;

/**
 * Convenience object allow to query and inspect data from tables 
 * in type safe way.
 *  
 */
public class Record {
  @SuppressWarnings("unchecked")
  private Map<FieldMap,Object> data = new HashMap<FieldMap, Object>();
  
  @SuppressWarnings("unchecked")
  public Map<String,Object> toBeanMap(){
    return LazyMap.morphKeys(new BeeOperation<FieldMap, String>(){
      public String doIt(FieldMap input) {
        return input.beanPath;
      }} , data) ;
  }
  
  @SuppressWarnings("unchecked")
  public <T> T get(FieldMap<T> key) {
    return (T) data.get(key);
  }

  @SuppressWarnings("unchecked")
  public Set<FieldMap> keySet() {
    return data.keySet();
  }

  @SuppressWarnings("unchecked")
  public <T> T put(FieldMap<T> key, T value) {
    return (T)data.put(key, value);
  }

  public int size() {
    return data.size();
  }
  
  @SuppressWarnings("unchecked")
  public <T> T build(Class<T> newClass){
    try {
      T r = newClass.newInstance();
      return build(r);
    } catch (Exception e) {
      throw new BeeException(e);
    }
  }

  @SuppressWarnings("unchecked")
  public <T> T build(T r) {
    Set<FieldMap> keySet = data.keySet();
    for (Iterator<FieldMap> iter = keySet.iterator(); iter.hasNext();) {
      FieldMap key = iter.next();
      key.populateBean(r, data.get(key));
      
    }
    return r;
  }
  
}
