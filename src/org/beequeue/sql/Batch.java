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

import org.beequeue.time.StopWatch;
import org.beequeue.util.BeeException;
import org.beequeue.util.ToStringUtil;


public class Batch<I> extends Operation<I> {
	private static final Logger log = Logger.getLogger(Select.class.getName());


	public Batch(String sql, SqlPrepare<I> executeBatch, SqlMorph<I> sqlTransition) {
		super(sql, executeBatch, sqlTransition);
	}


	public Batch(String sql, SqlPrepare<I> executeBatch) {
		super(sql, executeBatch);
	}


	public int[] executeBatch(Connection connection, I input)
	throws BeeException {
		StopWatch sw = new StopWatch();
		PreparedStatement pstmt = null;
		try{
			pstmt = createStatement(connection, input);
			prepare.invoke(pstmt,input, new Index());
			int[] rcs = pstmt.executeBatch();
			log.fine("batch: rcs="+ToStringUtil.toString(rcs) );
			return rcs;
		} catch (SQLException e) {
			log.fine("batch: ex="+e);
			throw new BeeException(e);
		}finally{
			log.fine("batch: time=" + sw.getSeconds());
			try { pstmt.close(); } catch (Exception ignore) {}
		}
	}

}
