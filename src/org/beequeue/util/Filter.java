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
package org.beequeue.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public interface Filter<T> extends BeeOperation<T,Boolean> {
  class Util {
    
    public static <T> List<T> keepMatches(Collection<T> collection, Filter <? super T> filter){
      return filter(collection, filter,false);
    }
    public static <T> List<T> throwAwayMatches(Collection<T> collection, Filter <? super T> filter){
      return filter(collection, filter,true);
    }
    
    public static <T> List<T> filter(Collection<T> collection, Filter <? super T> filter, boolean reverse){
      ArrayList<T> result = new ArrayList<T>();
      for (T t : collection) {
        if( filter.execute(t) ^ reverse ){
          result.add(t);
        }
      }
      return result;
    }
  }

  Filter<Object> TRUE = new Filter<Object>(){
    public Boolean execute(Object input) {
      return true;
    }};

  Filter<Object> FALSE = new Filter<Object>(){
    public Boolean execute(Object input) {
      return false;
    }};
}
