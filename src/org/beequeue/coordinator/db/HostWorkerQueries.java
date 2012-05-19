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
import java.sql.Timestamp;

import org.beequeue.host.Host;
import org.beequeue.sql.Index;
import org.beequeue.sql.JdbcFactory;
import org.beequeue.sql.Select;
import org.beequeue.sql.SqlPrepare;
import org.beequeue.sql.Update;
import org.beequeue.util.ToStringUtil;
import org.beequeue.worker.HostState;
import org.beequeue.worker.Worker;
import org.beequeue.worker.WorkerData;
import org.beequeue.worker.WorkerState;

public interface HostWorkerQueries {

	Select<Host, String> LOAD_HOST = new Select<Host, String>(
	"SELECT HOST_CD, HOST_STATE, HOST_IP, HOST_FQDN, CLOUD_CD " +
	"FROM NN_HOST " +
	"WHERE HOST_CD = ? ",
	new JdbcFactory<Host, String>() {
		@Override
		public Host newInstance(ResultSet rs, String input, Index idx)
				throws SQLException {

			Host host = new Host();
			host.hostName = rs.getString(idx.next());
			host.state = HostState.valueOf(rs.getString(idx.next()));
			host.ip = rs.getString(idx.next());
			host.fqdn = rs.getString(idx.next());
			host.cloud.name = rs.getString(idx.next());
			return host;
		}
	}, DbConstants.STRING_SQL_PREPARE);

	Update<Host> INSERT_HOST = new Update<Host>(
	"INSERT INTO NN_HOST " +
	"(HOST_CD, HOST_STATE, HOST_IP, HOST_FQDN, CLOUD_CD) "+
	"VALUES (?,?,?,?,?) ", 
	new SqlPrepare<Host>() {
		@Override
		public void invoke(PreparedStatement pstmt, Host input,
				Index idx) throws SQLException {
			pstmt.setString(idx.next(), input.hostName);
			pstmt.setString(idx.next(), input.state.name());
			pstmt.setString(idx.next(), input.ip);
			pstmt.setString(idx.next(), input.fqdn);
			pstmt.setString(idx.next(), input.cloud.name);
		}
	});

	Update<WorkerData> INSERT_HOST_STATISTICS = new Update<WorkerData>(
	"INSERT INTO NN_HOST_STATISTCS " +
	"(HOST_CD,DATA_COLLECTED,STAT_DATA) " +
	"VALUES (?,?,?)", 
	new SqlPrepare<WorkerData>() {
		@Override
		public void invoke(PreparedStatement pstmt, WorkerData input,
				Index idx) throws SQLException {
			pstmt.setString    ( idx.next(), input.host.hostName );
			pstmt.setTimestamp ( idx.next(), new Timestamp(System.currentTimeMillis()) );
			pstmt.setString    ( idx.next(), ToStringUtil.toString(input.hostStat) );
		}
	});

	Update<Worker> INSERT_WORKER = new Update<Worker>(
	"INSERT INTO NN_WORKER " +
	"(WORKER_ID,HOST_CD,PID,WORKER_STATE,NEXT_BEAT) "+
	"VALUES (?,?,?,?,?)", 
	new SqlPrepare<Worker>() {
		@Override
		public void invoke(PreparedStatement pstmt, Worker input, Index idx)
				throws SQLException {
			pstmt.setLong(idx.next(), input.id);
			pstmt.setString(idx.next(), input.hostName);
			pstmt.setLong(idx.next(), input.pid);
			pstmt.setString(idx.next(), input.state.name());
			pstmt.setLong(idx.next(), input.nextBeat);
		}
	});

	Update<Worker> UPDATE_WORKER = new Update<Worker>(
	"UPDATE NN_WORKER " +
	"SET WORKER_STATE = ? , NEXT_BEAT = ? " +
	"WHERE WORKER_ID = ?",
	new SqlPrepare<Worker>() {
		@Override
		public void invoke(PreparedStatement pstmt, Worker input, Index idx) 
				throws SQLException {
			pstmt.setString(idx.next(), input.state.name());
			pstmt.setLong(idx.next(), input.nextBeat);
			pstmt.setLong(idx.next(), input.id);
		}
	});
	
	JdbcFactory<Worker, Object> WORKER_FACTORY = new JdbcFactory<Worker, Object>() {
		@Override
		public Worker newInstance(ResultSet rs, Object input, Index idx)
				throws SQLException {
			Worker worker = new Worker();
			worker.id = rs.getLong(idx.next());
			worker.hostName = rs.getString(idx.next());
			worker.pid = rs.getLong(idx.next());
			worker.state = WorkerState.valueOf(rs.getString(idx.next()));
			worker.nextBeat = rs.getLong(idx.next());
			return worker;
		}
	};
			
	Select<Worker, String> LOAD_WORKERS = new Select<Worker, String>(
	"SELECT WORKER_ID,W.HOST_CD,PID,WORKER_STATE,NEXT_BEAT " +
	"FROM NN_WORKER W, NN_HOST H " +
	"WHERE H.HOST_CD=W.HOST_CD " +
	"AND (WORKER_STATE IN ('READY','PAUSED') AND H.CLOUD_CD = ?)", 
	WORKER_FACTORY ,
	DbConstants.STRING_SQL_PREPARE);

	Select<Worker, Long> LOAD_WORKER_BY_ID = new Select<Worker, Long>(
	"SELECT WORKER_ID,HOST_CD,PID,WORKER_STATE,NEXT_BEAT " +
	"FROM NN_WORKER " +
	"WHERE WORKER_ID = ? ", 
	WORKER_FACTORY ,
	DbConstants.LONG_SQL_PREPARE);

	String WORKER_TABLE = "NN_WORKER";
}
