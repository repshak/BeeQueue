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

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Type;

public class FieldProperty extends MyProperty {

  Field field;
  
  
  public FieldProperty(Field declaredField, int index) {
    super(index);
    this.field = declaredField;
  }


  protected Class<?> getArrayType() {
    return field.getType();
  }

  protected void setInternal(Object value, Object newValue) throws Exception {
    field.set(value,newValue);
  }

  protected Object getInternal(Object value) throws Exception{
    return field.get(value);
  }


  @Override
  public <T extends Annotation> T getPropertyAnnotation(Class<T> annotationClass) {
    return field.getAnnotation(annotationClass);
  }


  @Override
  public <T extends Annotation> T getTypeAnnotation(Class<T> annotationClass) {
    return field.getType().getAnnotation(annotationClass);
  }


  @Override
  public Type getGenericType() {
    return field.getGenericType();
  }
  
  


}
