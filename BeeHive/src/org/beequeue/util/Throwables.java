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
package org.beequeue.util;

import java.io.PrintWriter;
import java.io.StringWriter;


public class Throwables {

  /**
   * <b>Cast exception to <code>&lt;T></code></b>
   * <p> 
   * Check if throwable <code>e</code> already belong to 
   * hierarchy of throwables assignable to <code>&lt;T></code>. 
   * If true cast it, to <code>&lt;T></code>. If not try to 
   * create <code>&lt;T></code> with <code>e</code> as cause. If not 
   * successful throw {@link RuntimeException} with <code>e</code> 
   * as cause.
   *  
   * <p>
   * @param <T>
   * @param clazz define hierarchy of throwables
   * @param cause subject throwable
   * @return
   */
  @SuppressWarnings("unchecked")
  public static <T extends Throwable > T cast(Class<T> clazz, Throwable cause) {
    if( clazz.isAssignableFrom(cause.getClass()) ){
      return (T)cause;
    }else{
      try{
        T newInstance = clazz.newInstance();
        newInstance.initCause(cause);
        return newInstance;
      }catch(Throwable th){}
      try{
        return clazz
          .getConstructor(new Class[]{Throwable.class})
          .newInstance(new Object[]{cause});
      }catch(Throwable th){}
      throw new RuntimeException("cannot cast",cause);
    }
  }
  
  public static String toString(Throwable t){
    StringWriter stringWriter = new StringWriter();
    PrintWriter printWriter = new PrintWriter(stringWriter);
    t.printStackTrace(printWriter);
    printWriter.close();
    return stringWriter.toString();
  }

}
