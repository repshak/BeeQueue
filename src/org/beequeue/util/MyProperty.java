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
import java.lang.reflect.Array;
import java.lang.reflect.Type;

public abstract class MyProperty {
  private int index ;

  public MyProperty(int index) {
    super();
    this.index = index;
  }

  public Object getOrCreate(Object value)throws Exception {
    try{
      Object newValue = getInternal(value);
      if (newValue == null) {
        if( isArray() ){
          Object array = Array.newInstance(getArrayType().getComponentType(), index+1);
          newValue = getArrayType().getComponentType().newInstance();
          setInternal(value, array);
          Array.set(array, index, newValue);
        }else{
          newValue = getArrayType().newInstance();
          setInternal(value, newValue);
        }
      }else{
        if( isArray() ){
          Object oldArray = newValue;
          if( index < Array.getLength(oldArray) ){
            newValue = Array.get(oldArray, index);
            if(newValue ==null){
              newValue = getArrayType().getComponentType().newInstance();
              Array.set(oldArray, index, newValue);
            }
          }else{
            Object newArray = Array.newInstance(getArrayType().getComponentType(), index+1);
            System.arraycopy(oldArray, 0, newArray, 0, Array.getLength(oldArray));
            newValue = getArrayType().getComponentType().newInstance();
            setInternal(value, newArray);
            Array.set(newArray, index, newValue);
          }
        }
      }
      return newValue;
    }catch (Exception e) {
      throw Throwables.cast(RuntimeException.class, e);
    }
  }

  public boolean isArray() {
    return !isSimpeType(getArrayType()) && getArrayType().isArray();
  }
  
  /**
   * TODO add MIME parsing into {@link SimpleType} is assumed that byte array 
   * is not going to be used to each element independently
   */ 
  private boolean isSimpeType(Class<?> arrayType) {
    return SimpleType.find(arrayType)!=null || byte[].class.equals(arrayType) ;
  }

  abstract protected Object getInternal(Object value) throws Exception;
  
  public Object get(Object value) throws Exception{
    if(isArray()){
      return getOrCreate(value);
    }else{
      return getInternal(value);
    }
  }
  
  public void set(Object value, Object newValue) throws Exception{
    if(isArray()){
      Object oldArray = getInternal(value);
      if( oldArray != null && index < Array.getLength(oldArray) ){
        newValue = Array.get(oldArray, index);
        Array.set(oldArray, index, newValue);
      }else{
        Object newArray;
        if(oldArray != null){
          int max = Math.max(index+1,Array.getLength(oldArray));
          newArray = Array.newInstance(getArrayType().getComponentType(), max);
          System.arraycopy(oldArray, 0, newArray, 0, Array.getLength(oldArray));
        }else{
          newArray = Array.newInstance(getArrayType().getComponentType(), index+1);
        }
        setInternal(value, newArray);
        Array.set(newArray, index, newValue);
      }
    }else{
      setInternal(value, newValue);
    }
  }
  
  abstract protected void setInternal(Object value, Object newValue) throws Exception;

  abstract protected Class<?> getArrayType() ;

  abstract public <T extends Annotation> T getPropertyAnnotation(Class<T> annotationClass);
  
  abstract public <T extends Annotation> T getTypeAnnotation(Class<T> annotationClass);
  
  public Class<?> getPropertyType() {
    if(isArray()){
      return getArrayType().getComponentType();
    }else{
      return getArrayType();
    }
  }

  abstract public Type getGenericType();
  
}
