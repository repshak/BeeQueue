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
package org.beequeue.sql;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface SqlConstants {
  
  SqlPrepare<Object> NO_ARGS = null;

  SqlPrepare<String> ONE_STRING = new SqlPrepare<String>(){
    public void invoke(PreparedStatement pstmt, String input, Index idx) throws SQLException {
      pstmt.setString(idx.next(), input);
    }};


  SqlPrepare<Long> ONE_LONG = new SqlPrepare<Long>(){
    public void invoke(PreparedStatement pstmt, Long input, Index idx) throws SQLException {
      pstmt.setLong(idx.next(), input);
    }};

}
