package org.beequeue.coordinator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.beequeue.sql.DalException;
import org.beequeue.sql.Index;
import org.beequeue.sql.JdbcFactory;
import org.beequeue.sql.Select;
import org.beequeue.sql.SqlPrepare;
import org.beequeue.sql.Update;
import org.beequeue.util.Tuple;

public class IdFactory {
	private static final JdbcFactory<Long, Object> LONG_JDBC_FACTORY = new JdbcFactory<Long, Object>() {
		@Override
		public Long newInstance(ResultSet rs, Object input, Index idx) throws SQLException {
			return rs.getLong(idx.next());
		}
	};

	private static final SqlPrepare<String> STRING_SQL_PREPARE = new SqlPrepare<String>() {
		@Override
		public void invoke(PreparedStatement pstmt, String input, Index idx)
				throws SQLException {
			pstmt.setString(idx.next(), input);
			
		}
	};

	private static long GRAB_N_NUMBERS_AT_THE_TIME = 10 ;
	
	private static Select<Long, String> SELECT_NEXT_NUM = new Select<Long, String>(
			"SELECT COUNTER from NN_ID_FACTORY where TABLE_NAME = ?", 
			LONG_JDBC_FACTORY, STRING_SQL_PREPARE);
	
	private static Update<Tuple<Long,String>> UPDATE_NEXT_NUM = new Update<Tuple<Long,String>>(
			"UPDATE NN_ID_FACTORY SET COUNTER=? WHERE COUNTER = ? AND TABLE_NAME = ? " , 
			new SqlPrepare<Tuple<Long,String>>() {

		@Override
		public void invoke(PreparedStatement pstmt, Tuple<Long, String> input,
				Index idx) throws SQLException {
			pstmt.setLong(idx.next(), input.o1+GRAB_N_NUMBERS_AT_THE_TIME);
			pstmt.setLong(idx.next(), input.o1);
			pstmt.setString(idx.next(), input.o2);
		}
	});
	
	public static class IdRange {
		public final String tableName;
		long start = 0 ;
		long stop = 0;
		
		public IdRange(String tableName) {
			this.tableName = tableName;
		}

		synchronized public long getNext(DbCoordinator dbc){
			while( start >= stop ){
				Connection conn = dbc.connection();
				Long one = SELECT_NEXT_NUM.queryOne(conn, tableName);
				if(1 == UPDATE_NEXT_NUM.update(conn, new Tuple<Long, String>(one, tableName)) ){
					start = one; stop = start + GRAB_N_NUMBERS_AT_THE_TIME;
				}
			}
			return start++; 
		}
	}
	private static ConcurrentMap<String,IdRange> rangesPerTable = new ConcurrentHashMap<String, IdFactory.IdRange>();
	
	public static IdRange getIdRange(String tableName,DbCoordinator dbc) {
		IdRange idRange;
		while( (idRange = rangesPerTable.get(tableName)) == null){
			rangesPerTable.putIfAbsent(tableName, new IdRange(tableName));
		}
		return idRange;
	}
}
