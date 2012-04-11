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
package org.beequeue.sql.mapping;


import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.beequeue.sql.Index;
/**
 * Mapping data type that stores enum name in database field
 * @param <T> enum type
 */
public class EnumNameType<T extends Enum> implements DataType<T> {
  Class<T> enumClass ;
  private T[] values = null;
  
  public EnumNameType(Class<T> enumClass) {
    super();
    this.enumClass = enumClass;
  }

  @SuppressWarnings("unchecked")
  public T get(ResultSet rs, Index idx) 
  throws SQLException {
    try {
      if( values == null){
        synchronized (this){
          if(values == null){
            Method method = enumClass.getMethod("values", new Class[0]);
            values  = (T[]) method.invoke(null, new Object[0]);
          }
        }
      }
    } catch (Exception e) {
      throw new SQLException(e.toString());
    }
    String name = rs.getString(idx.next());
    for (int i = 0; i < values.length; i++) {
      if( values[i].name().equals(name) ){
        return values[i];
      }
    }
    return null;
  }

  public void set(PreparedStatement pstmt, Index idx, Object value)
  throws SQLException {
    pstmt.setString(idx.next(), ((Enum)value).name());
  }

  public static <T extends Enum> EnumNameType<T> newEnumType(Class <T> clazz){
    return new EnumNameType<T>(clazz);
  }
}
