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
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class BeanUtil {

  public static Object extractProperty(Object bean, String propertyPath) {
    try {
      String[] path = propertyPath.split("\\.");
      if (path.length == 0) {
        return null;
      }
      int curentPart = 0;
      Object value = bean;
      do {
        if (value instanceof Map) {
          Map map = (Map) value;
          String key = combinePath(path, curentPart);
          return map.get(key);
        }
        if(value == null){
          return null;
        }
        Class<? extends Object> valueClass = value.getClass();
        String propName = path[curentPart];
        value = getMyProperty(valueClass, propName).get(value);
      } while (++curentPart < path.length);
      return value;
    } catch (Exception e) {
      throw new RuntimeException("bean=" + bean + " propertyPath=" + propertyPath, e);
    }
  }

  static Pattern INDEXED_PROPERTY = Pattern.compile("^(\\w+)\\[(\\d+)\\]$");
  private static MyProperty getMyProperty(Class<? extends Object> valueClass, String propName)
      throws Exception {
    MyProperty pd ;
    int index = 0 ;
    Matcher matcher = INDEXED_PROPERTY.matcher(propName);
    if(matcher.matches()){
      propName = matcher.group(1);
      index = Integer.parseInt(matcher.group(2));
    }
    try{
      BeanInfo beanInfo = BeanProperty.getBeanInfo(valueClass);
      pd = new BeanProperty(BeanProperty.findProperty(beanInfo, propName),index);
    }catch(Exception e){
      Field declaredField = valueClass.getField(propName);
      
      if( declaredField != null && (declaredField.getModifiers() & Modifier.PUBLIC) > 0 ){
        pd = new FieldProperty(declaredField,index);
      }else{
        throw e;
      }
    }
    return pd;
  }



  @SuppressWarnings("unchecked")
  public static void populatePropertyAsString(Object bean, String propertyPath,
      String valueAsString) {
    try{
      String[] path = propertyPath.split("\\.");
      if (path.length == 0) {
        throw new RuntimeException("something wrong with propertyPath:"
            + propertyPath);
      }
      int curentPart = 0;
      Object value = bean;
      do {
        if (value instanceof Map) {
          Map map = (Map) value;
          String key = combinePath(path, curentPart);
          map.put(key, valueAsString);
          return;
        }
        Class<? extends Object> valueClass = value.getClass();
        MyProperty pd = getMyProperty(valueClass, path[curentPart]);
        if (curentPart < path.length - 1) {
          value = pd.getOrCreate(value);
        } else {
          Class<?> propertyType = pd.getPropertyType();
          SimpleTypeInterface find = SimpleType.find(propertyType);
          pd.set(value, find.toObject(valueAsString));
          return;
        }
      } while (++curentPart < path.length);
    } catch (Exception e) {
      throw new RuntimeException("bean=" + bean + " propertyPath="
          + propertyPath + " value="+valueAsString, e);
    }
  }

  private static String combinePath(String[] path, int curentPart) {
    StringBuilder sb = new StringBuilder();
    for (int i = curentPart; i < path.length; i++) {
      if (i > curentPart)
        sb.append(".");
      sb.append(path[i]);
    }
    return sb.toString();
  }

  public static void populateProperty(Object bean, String propertyPath,
      Object valueToset) {
    String[] path = propertyPath.split("\\.");
    if (path.length == 0) {
      throw new RuntimeException("something wrong with propertyPath:"
          + propertyPath);
    }
    int curentPart = 0;
    Object value = bean;
    try {
      do {
        Class<? extends Object> valueClass = value.getClass();
        MyProperty pd = getMyProperty(valueClass, path[curentPart]);
        boolean lastPathPart = curentPart >= path.length - 1;
        if (lastPathPart) {
          pd.set(value, valueToset);
          return;
        } else {
          value = pd.getOrCreate(value);
        }
      } while (++curentPart < path.length);
    } catch (Exception e) {
      throw new RuntimeException("",e);
    }
  }

  
  public static void injectMap(Object bean, Map<String,?> attributes, Filter<String> filter) {
    for (String propertyPath : attributes.keySet() ) {
      if(filter!=null && !filter.doIt(propertyPath)){continue;}
      BeanUtil.populatePropertyAsString(bean, propertyPath, attributes.get(propertyPath).toString());
    }
  }

}
