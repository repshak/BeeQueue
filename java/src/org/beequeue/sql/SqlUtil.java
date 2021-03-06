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

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;


public class SqlUtil {
	
	public static <T> int doUpdateInsertUpdate(Connection connection, Update<T> update, Update<T> insert, T input ){
		int rc = doUpdateInsert(connection, update, insert, input);
		if( rc == 0){
			rc = update.update(connection, input);
		}
		return rc;
	}

	public static <T> int doUpdateInsert(Connection connection, Update<T> update, Update<T> insert, T input) {
		int rc = update.update(connection, input);
		if( rc == 0 ){
			try{
				rc = insert.update(connection, input);
			}catch (Exception ignore) {}
		}
		return rc;
	}

	/**
	 * builds line like this "?, ?, ?, ?" if you pass 4 as argument
	 */
	public static String buildPlaceholderList(int count){
		StringBuffer sb = new StringBuffer(count*3-2);
		for (int i = 0; i < count; i++) {
			if(i != 0){
				sb.append(", ");
			}
			sb.append("?");
		}
		return sb.toString();
	}

	public static boolean toBoolean(String s) {
		return " TtYy".indexOf(s.charAt(0)) > 0;
	}
	public static String fromBoolean(boolean b) {
		return b ? "Y" : "N";
	}

	public static Long getNullableLong(ResultSet rs, int idx) throws SQLException {
		long v = rs.getLong(idx);
		return v==0 && rs.wasNull() ? null :  v ;
	}
  

}
