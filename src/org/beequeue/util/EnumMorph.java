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
