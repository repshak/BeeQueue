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
import java.util.EnumSet;

import org.beequeue.sql.Index;

public class EnumSetType<T extends Enum<T>> implements DataType<EnumSet<T>> {
  Class<T> enumClass ;
  private T[] values = null;
  
  public EnumSetType(Class<T> enumClass) {
    super();
    this.enumClass = enumClass;
  }

  @SuppressWarnings("unchecked")
  public EnumSet<T> get(ResultSet rs, Index idx) 
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
    return buildSet(EnumSet.noneOf(enumClass), values, rs.getInt(idx.next()));
  }

  static <T extends Enum<T>> EnumSet<T> buildSet(EnumSet<T> result, T[] values, int intValue) {
    for (int i = 0; i < 16; i++) {
      if( (intValue & 1) > 0  ){
        result.add(values[i]);
      }
      intValue >>= 1;
    }
    return result;
  }

  @SuppressWarnings("unchecked")
  public void set(PreparedStatement pstmt, Index idx, Object value)
  throws SQLException {
    pstmt.setInt(idx.next(), buildInt((EnumSet<T>)value));
  }

  static <T extends Enum<T>> int buildInt(EnumSet<T> enumSet) {
    int intResult  = 0 ;
    for (Enum<T> en : enumSet) {
      intResult += (1 << en.ordinal()) ;
    }
    return intResult;
  }

  public static <T extends Enum<T>> EnumSetType<T> newSetType(Class <T> clazz){
    return new EnumSetType<T>(clazz);
  }
}
