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
package org.beequeue.coordinator.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.beequeue.sql.Index;
import org.beequeue.sql.JdbcFactory;
import org.beequeue.sql.SqlPrepare;

public interface DbConstants {
	
	JdbcFactory<Long, Object> LONG_JDBC_FACTORY = new JdbcFactory<Long, Object>() {
		@Override
		public Long newInstance(ResultSet rs, Object input, Index idx) throws SQLException {
			return rs.getLong(idx.next());
		}
	};
	
	JdbcFactory<String, Object> STRING_JDBC_FACTORY = new JdbcFactory<String, Object>() {
		@Override
		public String newInstance(ResultSet rs, Object input, Index idx) throws SQLException {
			return rs.getString(idx.next());
		}
	};

	SqlPrepare<String> STRING_SQL_PREPARE = new SqlPrepare<String>() {
		@Override
		public void invoke(PreparedStatement pstmt, String input, Index idx)
				throws SQLException {
			pstmt.setString(idx.next(), input);
			
		}
	};
	
	SqlPrepare<Long> LONG_SQL_PREPARE = new SqlPrepare<Long>() {
		@Override
		public void invoke(PreparedStatement pstmt, Long input, Index idx)
				throws SQLException {
			pstmt.setLong(idx.next(), input);
			
		}
	};

}
