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
package org.beequeue.sql;

import java.util.Date;
import java.util.TimeZone;

public class SqlTypesUtil {
  
  public static String toTimezoneId(TimeZone timeZone) {
    return timeZone == null ?  null : timeZone.getID();
  }

  public static TimeZone toTimezone(String tzId) {
    return tzId == null ? null : TimeZone.getTimeZone(tzId);
  }    

  public static java.sql.Date toSqlDate(Date date){
    if (date instanceof java.sql.Date) {
      return (java.sql.Date) date;
    }
    return date == null ? null : new java.sql.Date(date.getTime());
  }

  public static java.sql.Timestamp toTimestamp(Date date){
    if (date instanceof java.sql.Timestamp) {
      return (java.sql.Timestamp) date;
    }
    return date == null ? null : new java.sql.Timestamp(date.getTime());
  }
  
}
