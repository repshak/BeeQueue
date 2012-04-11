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
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Logger;

import org.beequeue.util.ToStringUtil;

public class Operation<I> {
	private static final Logger log = Logger.getLogger(Operation.class.getName());

	public final String sql ;
	public final SqlPrepare<I> prepare ;
	public final SqlMorph<? super I> morph  ;

	public Operation(String sql, SqlPrepare<I> afterPrepare) {
		this(sql, afterPrepare, null);
	}
	public Operation(String sql, SqlPrepare<I> sqlPrepare, SqlMorph<? super I> sqlMorph) {
		this.sql = sql;
		this.prepare = sqlPrepare;
		this.morph = sqlMorph ; 
	}

	protected PreparedStatement prepare(Connection connection, I input)
	throws SQLException {
		PreparedStatement pstmt = createStatement(connection, input);
		try{
			if(prepare != null){
				prepare.invoke(pstmt,input, new Index());
			}
			return pstmt;
		} catch (SQLException e) {
			try { pstmt.close(); } catch (Exception ignore) {}
			throw e;
		}
	}
	
	protected  PreparedStatement createStatement(Connection connection, I input) throws SQLException {
		String sqlToRun = this.sql;
		if (morph != null) {
			sqlToRun = morph.change(sqlToRun,input);
		}
		log.fine("prepare:"+sqlToRun);
		if(input != null){
			log.fine("input:"+ToStringUtil.toString(input));
		}
		PreparedStatement pstmt = connection.prepareStatement(sqlToRun);
		return pstmt;
	}

}
