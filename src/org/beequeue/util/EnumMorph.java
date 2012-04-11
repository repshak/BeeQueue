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

import java.lang.reflect.Method;
import java.util.Arrays;

public class EnumMorph implements SimpleTypeInterface {
private Class<? extends Enum> enumClass;
private Enum[] values = null;

public EnumMorph(Class<? extends Enum> enumClass) {
  this.enumClass = enumClass;
}

public Enum[] getValues() {
  ensureValues();
  return values;
}

private void ensureValues() {
  try {
    if( values == null){
      synchronized (this){
        if(values == null){
          Method method = enumClass.getMethod("values", new Class[0]);
          values  = (Enum[]) method.invoke(null, new Object[0]);
        }
      }
    }
  } catch (Exception e) {
    throw Throwables.cast(RuntimeException.class,e);
  }
}

Enum findByName(String name){
  ensureValues();
  for (int i = 0; i < values.length; i++) {
    if(values[i].name().equals(name)){
      return values[i];
    }
  }
  throw new IllegalArgumentException("no such enum:"+ Arrays.asList(values));
}


  public Object doIt(String s){
    return findByName(s);
  }
  public Object toObject(String s){
    return doIt(s);
  }
}
