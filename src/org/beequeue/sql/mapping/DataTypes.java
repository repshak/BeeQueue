/** ==== BEGIN LICENSE =====
   Copyright 2004-2007 - Wakeup ORM

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

public interface DataTypes {

  DataType<Long> LONG = new DataType<Long>(){
    
    public Long get(ResultSet rs, Index idx) throws SQLException {
      return rs.getLong(idx.next());
    }
    
    public void set(PreparedStatement pstmt, Index idx, Object value) throws SQLException {
      pstmt.setLong(idx.next(), (Long)value);
    }
  };

  DataType<Long> LONG_NULL = new DataType<Long>(){

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

  DataType<Double> DOUBLE = new DataType<Double>(){
    public Double get(ResultSet rs, Index idx) throws SQLException {
      return rs.getDouble(idx.next());
    }
    
    public void set(PreparedStatement pstmt, Index idx, Object value) throws SQLException {
      pstmt.setDouble(idx.next(), (Double)value);
    }
  };
  
  DataType<String> STRING = new DataType<String>(){
    public String get(ResultSet rs, Index idx) throws SQLException {
      return rs.getString(idx.next());
    }
    
    public void set(PreparedStatement pstmt, Index idx, Object value) throws SQLException {
      pstmt.setString(idx.next(), (String)value);
    }
  };
  DataType<byte[]> BYTES = new DataType<byte[]>(){
    public byte[] get(ResultSet rs, Index idx) throws SQLException {
      return rs.getBytes(idx.next());
    }

    public void set(PreparedStatement pstmt, Index idx, Object value) throws SQLException {
      pstmt.setBytes(idx.next(), (byte[])value);
    }
  };
  DataType<Integer> INTEGER = new DataType<Integer>(){
    public Integer get(ResultSet rs, Index idx) throws SQLException {
      return rs.getInt(idx.next());
    }
    
    public void set(PreparedStatement pstmt, Index idx, Object value) throws SQLException {
      pstmt.setInt(idx.next(), (Integer)value);
    }
  };
  
  DataType<Boolean> BOOLEAN = new DataType<Boolean>(){
    public Boolean get(ResultSet rs, Index idx) throws SQLException {
      return rs.getInt(idx.next()) > 0;
    }

    public void set(PreparedStatement pstmt, Index idx, Object value) throws SQLException {
      pstmt.setInt(idx.next(), (Boolean)value? 1 : 0);
    }
  };
  
  DataType<Date> DATE = new DataType<Date>(){
    public Date get(ResultSet rs, Index idx) throws SQLException {
      return rs.getDate(idx.next());
    }

    public void set(PreparedStatement pstmt, Index idx, Object value) throws SQLException {
      pstmt.setDate(idx.next(), SqlTypesUtil.toSqlDate((Date)value));
    }
  };  

  DataType<Date> TIMESTAMP = new DataType<Date>(){
    public Date get(ResultSet rs, Index idx) throws SQLException {
      return rs.getTimestamp(idx.next());
    }

    public void set(PreparedStatement pstmt, Index idx, Object value) throws SQLException {
      pstmt.setTimestamp(idx.next(), SqlTypesUtil.toTimestamp((Date)value));
    }
  };  

  DataType<TimeZone> TIMEZONE = new DataType<TimeZone>(){
    public TimeZone get(ResultSet rs, Index idx) throws SQLException {
      return SqlTypesUtil.toTimezone(rs.getString(idx.next()));
    }

    public void set(PreparedStatement pstmt, Index idx, Object value) throws SQLException {
      pstmt.setString(idx.next(), SqlTypesUtil.toTimezoneId((TimeZone) value ));
    }
    
  };

  DataType<URL> URL = new DataType<URL>(){

    public java.net.URL get(ResultSet rs, Index idx) throws SQLException {
      return rs.getURL(idx.next());
    }

    public void set(PreparedStatement pstmt, Index idx, Object value) throws SQLException {
      pstmt.setURL(idx.next(),(URL)value);
    }};
}
