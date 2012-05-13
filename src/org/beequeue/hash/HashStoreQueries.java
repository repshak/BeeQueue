package org.beequeue.hash;

import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

import org.beequeue.host.Cloud;
import org.beequeue.sql.DalException;
import org.beequeue.sql.Index;
import org.beequeue.sql.JdbcFactory;
import org.beequeue.sql.Select;
import org.beequeue.sql.SqlMorph;
import org.beequeue.sql.SqlPrepare;
import org.beequeue.sql.Update;
import org.beequeue.util.Streams;
import org.beequeue.util.Strings;



public interface HashStoreQueries {

	JdbcFactory<HashKey, Object> SHACODE_JDBC_FACTORY = new JdbcFactory<HashKey, Object>() {
		@Override
		public HashKey newInstance(ResultSet rs, Object input, Index idx) throws SQLException {
			return HashKey.valueOf(rs.getString(idx.next()));
		}
	};


	SqlPrepare<HashKey> SHACODE_SQL_PREPARE = new SqlPrepare<HashKey>() {
		@Override
		public void invoke(PreparedStatement pstmt, HashKey input, Index idx)
				throws SQLException {
			pstmt.setString(idx.next(), input.toString());
		}
	};
	
	Select<HashKey, Set<HashKey>> CHECK_HASH_KEY = new Select<HashKey, Set<HashKey>>(
			"SELECT SHA_ID FROM NN_SHA_STORAGE WHERE SHA_ID IN (%%%)",
			SHACODE_JDBC_FACTORY, new SqlPrepare<Set<HashKey>>() {
				@Override
				public void invoke(PreparedStatement pstmt, Set<HashKey> input, Index idx)
						throws SQLException {
					for (HashKey code : input) {
						SHACODE_SQL_PREPARE.invoke(pstmt, code, idx);
					}
				}
			}, (SqlMorph<? super Set<HashKey>>) new SqlMorph<Set<HashKey>>() {
			
				@Override
				public String change(String sql, Set<HashKey> input) {
					return sql.replace("%%%", Strings.repeat("?"," ,",input.size()));
				}
			});


	

	Select<Long, HashOutput> STREAM_CONTENT_OUT = 
			new Select<Long, HashOutput>(
		"SELECT CONFIG_DATA FROM NN_SHA_STORAGE WHERE SHA_ID = ?",
		new JdbcFactory<Long, HashOutput>() {
		
		@Override
		public Long newInstance(ResultSet rs,
			HashOutput input, Index idx)
			throws SQLException {
			try {
				InputStream in = rs.getBinaryStream(idx.next());
				return (long) Streams.copyAndClose(in, input.out);
			} catch (IOException e) {
				throw new DalException(e);
			}
		}
		}, 
		new SqlPrepare<HashOutput>() {
			@Override
			public void invoke(PreparedStatement pstmt, HashOutput input, Index idx)
					throws SQLException {
				SHACODE_SQL_PREPARE.invoke(pstmt, input.code, idx);
				
			}
		});
	
	
	

	Update<HashInput> STREAM_CONTENT_IN = new Update<HashInput>(
			"INSERT INTO NN_SHA_STORAGE (SHA_ID,CONFIG_DATA,SWEEPT_ON) VALUES (?,?,CURRENT_TIMESTAMP)", 
			new SqlPrepare<HashInput>() {
				@Override
				public void invoke(PreparedStatement pstmt, HashInput input,
						Index idx) throws SQLException {
					SHACODE_SQL_PREPARE.invoke(pstmt, input.code, idx);
					pstmt.setBinaryStream(idx.next(), input.in);
				}
			});
	
	Update<String> SWEEP_BY_SELECT = new Update<String>(
	"UPDATE NN_SHA_STORAGE " +
	"SET SWEEPT_ON=CURRENT_TIMESTAMP " +
	"WHERE SHA_ID IN (%%%)", 
	null, new SqlMorph<String>() {

		@Override
		public String change(String sql, String input) {
			return sql.replace("%%%", input);
		}
	});

	
}
