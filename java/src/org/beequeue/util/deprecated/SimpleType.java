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
/**
 * Wakeup OR 
 * www.apache.org/licenses/LICENSE-2.0.html  
 */
package org.beequeue.util.deprecated;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

import org.beequeue.util.EnumMorph;
import org.beequeue.util.Throwables;


public enum SimpleType implements SimpleTypeInterface{
  STRING(String.class){
    public Object execute(String s){
      return s;
    }
  },
  BOOLEAN(Boolean.class,Boolean.TYPE){
    public Object execute(String s){
      return new Boolean(s);
    }
  },
  INTEGER(Integer.class,Integer.TYPE){
    public Object execute(String s){
      return new Integer(s);
    }
  },
  LONG(Long.class,Long.TYPE){
    public Object execute(String s){
      return new Long(s);
    }
  },
  SHORT(Short.class,Short.TYPE){
    public Object execute(String s){
      return new Short(s);
    }
  },
  FLOAT(Float.class,Float.TYPE){
    public Object execute(String s){
      return new Float(s);
    }
  },
  DOUBLE(Double.class,Double.TYPE){
    public Object execute(String s){
      return new Double(s);
    }
  },
  DATE(Date.class){
    public Object execute(String s){
      try {
        return new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z").parse(s);
      } catch (ParseException e) {
        return new Date(s);
      }
    }
  },
  FILE(File.class){
    public Object execute(String s){
      return new File(s);
    }
  },
  PATTERN(Pattern.class){
    public Object execute(String s){
      return Pattern.compile(s);
    }
  },
  CLASS(Class.class){
    public Object execute(String s){
      try {
        return Class.forName(s);
      } catch (ClassNotFoundException e) {
        throw Throwables.cast(RuntimeException.class, e);
      }
    }
  }
  ;
  public final Class[] classes;

  private SimpleType(Class... classes){
    this.classes = classes;
  }

  public boolean match(Class classToTest) {
    for (int i = 0; i < classes.length; i++) {
      if(classes[i].isAssignableFrom(classToTest) ){
        return true;
      }
    }
    return false;
  }
  
  public Object toObject(String s){
    return execute(s);
  }
  
  public static SimpleTypeInterface find(Class classToTest){
    SimpleType[] values = values();
    for (int i = 0; i < values.length; i++) {
      SimpleType type = values[i];
      if( type.match(classToTest) ){
        return type;
      }
    }
    if (Enum.class.isAssignableFrom(classToTest)) {
      return new EnumMorph(classToTest);
    }
    return null;
  }
}
