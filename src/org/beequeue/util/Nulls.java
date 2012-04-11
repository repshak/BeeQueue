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

import java.util.Collection;
import java.util.Iterator;

/**
 * Null handling
 *
 */
public class Nulls {
  
  public static <T> T fallback(T test, T def){
    return test == null ? def : test ;
  }
  
  public static Double numFallback(Double test, Double def){
    return test == null || Double.isNaN(test) ?  def : test ;
  }

  public static <T> Integer checkNull(T v1, T v2, boolean nullFirst) {
    if(v1 == null && v2 != null){
      return nullFirst? -1 : 1;
    }else if(v1 != null && v2 == null){
      return nullFirst? 1 : -1;
    }else if(v1 == null && v2 == null){
      return 0;
    }
    return null;
  }
  
  public static <T extends Comparable<T>> int compare(T a, T b, boolean nullFirst){
    Integer checkNull = checkNull(a, b, nullFirst);
    if(checkNull != null){
      return checkNull;
    }
    return a.compareTo(b);
  }
  
  public static <T extends Comparable<T>> int compare(Collection<T> v1, Collection<T> v2, boolean shortListFirst , boolean nullFirst) {
    Integer checkNull = checkNull(v1,v2,nullFirst);
    if(checkNull!=null){
      return checkNull;
    }
    Iterator<T> i1 = v1.iterator(); 
    Iterator<T> i2 = v2.iterator();
    for(;;){
      boolean hasNext1 = i1.hasNext();   
      boolean hasNext2 = i2.hasNext();
      if( !hasNext1 && !hasNext2 ){
        return 0;
      }if( hasNext1 && !hasNext2 ){
        return shortListFirst ? 1 : -1;
      }else if( !hasNext1 && hasNext2 ){
        return shortListFirst ? -1 : 1;
      }else{
        int compare = compare(i1.next(), i2.next(), nullFirst);
        if(compare!=0){
          return compare;
        }
      }
    }
  }
  
  public static boolean equals(Object a, Object b){
    if(a==b){
      return true;
    }else if(a!=null && b!=null){
      return a.equals(b);
    }else{
      return false;
    }
  }
}
