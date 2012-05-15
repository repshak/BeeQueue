package org.beequeue.coordinator.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.beequeue.sql.Index;
import org.beequeue.sql.JdbcFactory;
import org.beequeue.sql.Select;
import org.beequeue.sql.SqlPrepare;
import org.beequeue.sql.Update;
import org.beequeue.util.BeeException;
import org.beequeue.util.Tuple;

public class IdFactory {

	private static long GRAB_N_NUMBERS_AT_THE_TIME = 10 ;
	
	private static Select<Long, String> SELECT_NEXT_NUM = new Select<Long, String>(
			"SELECT COUNTER from NN_ID_FACTORY where TABLE_NAME = ?", 
			DbConstants.LONG_JDBC_FACTORY, DbConstants.STRING_SQL_PREPARE);
	
	private static Update<Tuple<Long,String>> CREATE_TABLE_ENTRY = new Update<Tuple<Long,String>>(
			"INSERT INTO NN_ID_FACTORY (TABLE_NAME, COUNTER) VALUES (?,?)" , 
			new SqlPrepare<Tuple<Long,String>>() {
				@Override
				public void invoke(PreparedStatement pstmt, Tuple<Long, String> input,
						Index idx) throws SQLException {
					pstmt.setString(idx.next(), input.o2);
					pstmt.setLong(idx.next(), input.o1);
				}
			});
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
				List<Long> oneOrZero = SELECT_NEXT_NUM.query(conn, tableName);
				if(oneOrZero.size()==0){
					if(1 == CREATE_TABLE_ENTRY.update(conn, new Tuple<Long, String>(GRAB_N_NUMBERS_AT_THE_TIME, tableName)) ){
						start = 1L; stop = GRAB_N_NUMBERS_AT_THE_TIME;
					}
				}else if(oneOrZero.size()==1){
					if(1 == UPDATE_NEXT_NUM.update(conn, new Tuple<Long, String>(oneOrZero.get(0), tableName)) ){
						start = oneOrZero.get(0); stop = start + GRAB_N_NUMBERS_AT_THE_TIME;
					}
				}else{
					throw new BeeException("Hell broke loose:"+oneOrZero);
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
