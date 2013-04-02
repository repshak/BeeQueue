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

import org.beequeue.msg.BeeQueueDomain;
import org.beequeue.msg.DomainState;
import org.beequeue.sql.Index;
import org.beequeue.sql.JdbcFactory;
import org.beequeue.sql.Select;
import org.beequeue.sql.SqlPrepare;
import org.beequeue.sql.Update;

public interface DomainQueries {
	Select<BeeQueueDomain, Void> LOAD_DOMAINS = new Select<BeeQueueDomain, Void>(
	"SELECT DOMAIN_CD,DOMAIN_STATE FROM NN_DOMAIN",
	new JdbcFactory<BeeQueueDomain, Void>() {
		@Override
		public BeeQueueDomain newInstance(ResultSet rs, Void input, Index idx)
				throws SQLException {
			BeeQueueDomain domain = new BeeQueueDomain();
			domain.name = rs.getString(idx.next());
			domain.state = DomainState.valueOf(rs.getString(idx.next()));
			return domain;
		}
	}, null);

	SqlPrepare<BeeQueueDomain> PREPARE_DOMAIN = new SqlPrepare<BeeQueueDomain>() {
		@Override
		public void invoke(PreparedStatement pstmt, BeeQueueDomain input,
				Index idx) throws SQLException {
			pstmt.setString(idx.next(), input.state.name());
			pstmt.setString(idx.next(), input.name);
		}
	};

	Update<BeeQueueDomain> INSERT_DOMAIN = new Update<BeeQueueDomain>(
			"INSERT INTO NN_DOMAIN (DOMAIN_STATE, DOMAIN_CD) VALUES (?,?) ", 
			PREPARE_DOMAIN);
	
	Update<BeeQueueDomain> UPDATE_DOMAIN = new Update<BeeQueueDomain>(
			"UPDATE NN_DOMAIN SET DOMAIN_STATE=? WHERE  DOMAIN_CD=?", 
			PREPARE_DOMAIN);

}
