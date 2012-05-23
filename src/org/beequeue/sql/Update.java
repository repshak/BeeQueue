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
import java.util.logging.Logger;

import org.beequeue.time.StopWatch;
import org.beequeue.util.BeeException;


public class Update<I> extends Operation<I> {
	private static final Logger log = Logger.getLogger(Select.class.getName());
	

	public Update(String sql, SqlPrepare<I> setParams, SqlMorph<? super I> sqlTransition) {
		super(sql, setParams, sqlTransition);
	}


	public Update(String sql, SqlPrepare<I> setParams) {
		super(sql, setParams);
	}


	public int update(Connection connection, I input)
	throws BeeException {
    StopWatch sw = new StopWatch();
		PreparedStatement pstmt = null;
		try{
			pstmt = prepare(connection, input);
			int rc = pstmt.executeUpdate();
			log.fine("update: rc="+rc);
			return rc;
		} catch (Exception e) {
			log.fine("update: ex="+e);
			throw BeeException.cast(e);
		}finally{
			log.fine("update: time=" + sw.getSeconds());
			try { pstmt.close(); } catch (Exception ignore) {}
		}
	}
  
  public void updateOne(Connection connection, I input) {
    int rc = update(connection, input);
    if( 1 != rc ){
      throw new BeeException("expected 1 record to be modified, but not:"+rc).addPayload(sql);
    }
  }

}
