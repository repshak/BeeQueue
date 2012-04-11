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

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

public class BeanProperty extends MyProperty {
  public static BeanInfo getBeanInfo(Class<?> clazz) {
    try {
      return Introspector.getBeanInfo(clazz);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static PropertyDescriptor findProperty(BeanInfo beanInfo,
      String propertyToSearch) {
    PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
    for (int i = 0; i < propertyDescriptors.length; i++) {
      PropertyDescriptor descriptor = propertyDescriptors[i];
      if (descriptor.getName().equals(propertyToSearch)) {
        return descriptor;
      }
    }
    throw new RuntimeException("no such property:" + propertyToSearch);
  }

  PropertyDescriptor pd;
  public BeanProperty(PropertyDescriptor pd, int index) {
    super(index);
    this.pd = pd;
  }

  public <T extends Annotation> T getPropertyAnnotation(Class<T> annotationClass){
    return pd.getReadMethod().getAnnotation(annotationClass);
  }
  
  public <T extends Annotation> T getTypeAnnotation(Class<T> annotationClass){
    return pd.getPropertyType().getAnnotation(annotationClass);
  }

  protected Object getInternal(Object value) throws Exception {
    return pd.getReadMethod().invoke(value, new Object[0]);
  }

  protected void setInternal(Object value, Object newValue) throws Exception {
    try{
      Method writeMethod = pd.getWriteMethod();
      writeMethod.invoke(value, new Object[] { newValue });
    }catch (Exception e) {
      throw new RuntimeException("Cannot write property:" + pd.getName() + " in " + value.getClass(), e);
    }
  }

  protected Class<?> getArrayType() {
    return pd.getPropertyType();
  }

  @Override
  public Type getGenericType() {
    return pd.getReadMethod().getGenericReturnType();
  }

}
