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


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.beequeue.sql.Index;
import org.beequeue.sql.SqlPrepare;

abstract public class SqlHelper<T> {

  @SuppressWarnings("unchecked")
  public static Record buildRecord(List<FieldMap> fields, ResultSet rs, Index idx) throws SQLException {
    Record record = new Record();
    for (FieldMap map : fields) {
      record.put(map, map.dataType.get(rs, idx));
    }
    return record;
  }

  public static <I> SqlPrepare<I> prepare(final List<FieldMap> fields) {
    SqlPrepare<I> setParams = new SqlPrepare<I>(){
      public void invoke(PreparedStatement pstmt, I input, Index idx) throws SQLException {
        for (FieldMap field : fields) {
          field.bindValue(pstmt,idx,input);
        }
      }
    };
    return setParams;
  }
  
  public static  <I> WhereCondition<I> where(FieldMap<I> key, String whereField) {
    WhereCondition<I> whereCondition = WhereCondition.build(whereField);
    if( key != null){
      if (whereField == null) {
        whereCondition = WhereCondition.build(key);
      }else{
        whereCondition = WhereCondition.build(whereField,key);
      }
    }
    return whereCondition;
  }
  

}
