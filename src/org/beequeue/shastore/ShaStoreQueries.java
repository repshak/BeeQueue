package org.beequeue.shastore;

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



public interface ShaStoreQueries {

	JdbcFactory<ShaCode, Object> SHACODE_JDBC_FACTORY = new JdbcFactory<ShaCode, Object>() {
		@Override
		public ShaCode newInstance(ResultSet rs, Object input, Index idx) throws SQLException {
			return ShaCode.valueOf(rs.getString(idx.next()));
		}
	};


	SqlPrepare<ShaCode> SHACODE_SQL_PREPARE = new SqlPrepare<ShaCode>() {
		@Override
		public void invoke(PreparedStatement pstmt, ShaCode input, Index idx)
				throws SQLException {
			pstmt.setString(idx.next(), input.toString());
		}
	};
	
	Select<ShaCode, Set<ShaCode>> CHECK_SHA_CODE = new Select<ShaCode, Set<ShaCode>>(
			"SELECT SHA_ID FROM NN_SHA_STORAGE WHERE SHA_ID IN (%%%)",
			SHACODE_JDBC_FACTORY, new SqlPrepare<Set<ShaCode>>() {
				@Override
				public void invoke(PreparedStatement pstmt, Set<ShaCode> input, Index idx)
						throws SQLException {
					for (ShaCode code : input) {
						SHACODE_SQL_PREPARE.invoke(pstmt, code, idx);
					}
				}
			}, (SqlMorph<? super Set<ShaCode>>) new SqlMorph<Set<ShaCode>>() {
			
				@Override
				public String change(String sql, Set<ShaCode> input) {
					return sql.replace("%%%", Strings.repeat("?"," ,",input.size()));
				}
			});


	

	Select<Long, ShaOutput> STREAM_SHA_CODE = 
			new Select<Long, ShaOutput>(
		"SELECT CONFIG_DATA FROM NN_SHA_STORAGE WHERE SHA_ID = ?",
		new JdbcFactory<Long, ShaOutput>() {
		
		@Override
		public Long newInstance(ResultSet rs,
			ShaOutput input, Index idx)
			throws SQLException {
			try {
				InputStream in = rs.getBinaryStream(idx.next());
				return (long) Streams.copyAndClose(in, input.out);
			} catch (IOException e) {
				throw new DalException(e);
			}
		}
		}, 
		new SqlPrepare<ShaOutput>() {
			@Override
			public void invoke(PreparedStatement pstmt, ShaOutput input, Index idx)
					throws SQLException {
				SHACODE_SQL_PREPARE.invoke(pstmt, input.code, idx);
				
			}
		});
	
	
	

	Update<ShaInput> INSERT_DATA = new Update<ShaInput>(
			"INSERT INTO NN_SHA_STORAGE (SHA_ID,CONFIG_DATA,SWEEPT_ON) VALUES (?,?,CURRENT_TIMESTAMP)", 
			new SqlPrepare<ShaInput>() {
				@Override
				public void invoke(PreparedStatement pstmt, ShaInput input,
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
