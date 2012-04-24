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


import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.TimeZone;

import org.beequeue.sql.Index;
import org.beequeue.sql.SqlTypesUtil;

public interface DbTypes {

  DbType<Long> LONG = new DbType<Long>(){
    
    public Long get(ResultSet rs, Index idx) throws SQLException {
      return rs.getLong(idx.next());
    }
    
    public void set(PreparedStatement pstmt, Index idx, Object value) throws SQLException {
      pstmt.setLong(idx.next(), (Long)value);
    }
  };

  DbType<Long> LONG_NULL = new DbType<Long>(){

    public Long get(ResultSet rs, Index idx) throws SQLException {
      long value = rs.getLong(idx.next());
      if(rs.wasNull()){
        return null;
      }else{
        return value;
      }
    }

    public void set(PreparedStatement pstmt, Index idx, Object value) throws SQLException {
      if(value == null){
        pstmt.setObject(idx.next(), null);
      }else{
        pstmt.setLong(idx.next(), (Long)value);
      }
    }
  };

  DbType<Double> DOUBLE = new DbType<Double>(){
    public Double get(ResultSet rs, Index idx) throws SQLException {
      return rs.getDouble(idx.next());
    }
    
    public void set(PreparedStatement pstmt, Index idx, Object value) throws SQLException {
      pstmt.setDouble(idx.next(), (Double)value);
    }
  };
  
  DbType<String> STRING = new DbType<String>(){
    public String get(ResultSet rs, Index idx) throws SQLException {
      return rs.getString(idx.next());
    }
    
    public void set(PreparedStatement pstmt, Index idx, Object value) throws SQLException {
      pstmt.setString(idx.next(), (String)value);
    }
  };
  DbType<byte[]> BYTES = new DbType<byte[]>(){
    public byte[] get(ResultSet rs, Index idx) throws SQLException {
      return rs.getBytes(idx.next());
    }

    public void set(PreparedStatement pstmt, Index idx, Object value) throws SQLException {
      pstmt.setBytes(idx.next(), (byte[])value);
    }
  };
  DbType<Integer> INTEGER = new DbType<Integer>(){
    public Integer get(ResultSet rs, Index idx) throws SQLException {
      return rs.getInt(idx.next());
    }
    
    public void set(PreparedStatement pstmt, Index idx, Object value) throws SQLException {
      pstmt.setInt(idx.next(), (Integer)value);
    }
  };
  
  DbType<Boolean> BOOLEAN = new DbType<Boolean>(){
    public Boolean get(ResultSet rs, Index idx) throws SQLException {
      return rs.getInt(idx.next()) > 0;
    }

    public void set(PreparedStatement pstmt, Index idx, Object value) throws SQLException {
      pstmt.setInt(idx.next(), (Boolean)value? 1 : 0);
    }
  };
  
  DbType<Date> DATE = new DbType<Date>(){
    public Date get(ResultSet rs, Index idx) throws SQLException {
      return rs.getDate(idx.next());
    }

    public void set(PreparedStatement pstmt, Index idx, Object value) throws SQLException {
      pstmt.setDate(idx.next(), SqlTypesUtil.toSqlDate((Date)value));
    }
  };  

  DbType<Date> TIMESTAMP = new DbType<Date>(){
    public Date get(ResultSet rs, Index idx) throws SQLException {
      return rs.getTimestamp(idx.next());
    }

    public void set(PreparedStatement pstmt, Index idx, Object value) throws SQLException {
      pstmt.setTimestamp(idx.next(), SqlTypesUtil.toTimestamp((Date)value));
    }
  };  

  DbType<TimeZone> TIMEZONE = new DbType<TimeZone>(){
    public TimeZone get(ResultSet rs, Index idx) throws SQLException {
      return SqlTypesUtil.toTimezone(rs.getString(idx.next()));
    }

    public void set(PreparedStatement pstmt, Index idx, Object value) throws SQLException {
      pstmt.setString(idx.next(), SqlTypesUtil.toTimezoneId((TimeZone) value ));
    }
    
  };

  DbType<URL> URL = new DbType<URL>(){

    public java.net.URL get(ResultSet rs, Index idx) throws SQLException {
      return rs.getURL(idx.next());
    }

    public void set(PreparedStatement pstmt, Index idx, Object value) throws SQLException {
      pstmt.setURL(idx.next(),(URL)value);
    }};
}
