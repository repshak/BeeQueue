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
