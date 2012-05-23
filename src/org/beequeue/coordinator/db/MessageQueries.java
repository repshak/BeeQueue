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
import java.util.LinkedHashMap;

import org.beequeue.msg.BeeQueueJob;
import org.beequeue.msg.BeeQueueMessage;
import org.beequeue.msg.BeeQueueRun;
import org.beequeue.msg.BeeQueueStage;
import org.beequeue.msg.JobState;
import org.beequeue.msg.MessageState;
import org.beequeue.msg.RunState;
import org.beequeue.msg.StageState;
import org.beequeue.sql.Index;
import org.beequeue.sql.JdbcFactory;
import org.beequeue.sql.Select;
import org.beequeue.sql.SqlPrepare;
import org.beequeue.sql.SqlUtil;
import org.beequeue.sql.Update;
import org.beequeue.util.ToStringUtil;

import com.fasterxml.jackson.core.type.TypeReference;

public interface MessageQueries {

	public static final String NN_MESSAGE = "NN_MESSAGE";
	public static final String NN_JOB = "NN_JOB";
	public static final String NN_STAGE = "NN_STAGE";
	public static final String NN_RUN = "NN_RUN";

	public static final String SELECT_MESSAGE = "SELECT MSG_ID,MSG_NAME,MSG_STATE,MSG_PARAMETERS,CONTEXT_SETTINGS,USER_CD,DOMAIN_CD,LOCK_TS,CURRENT_TIMESTAMP FROM NN_MESSAGE ";
	public static final JdbcFactory<BeeQueueMessage, Object> MESSAGE_FACTORY = new JdbcFactory<BeeQueueMessage, Object>() {
		@Override
		public BeeQueueMessage newInstance(ResultSet rs, Object input, Index idx)
				throws SQLException {
			BeeQueueMessage msg = new BeeQueueMessage();
			msg.id = rs.getLong(idx.next());
			msg.name = rs.getString(idx.next());
			msg.state = MessageState.valueOf(rs.getString(idx.next()));
			msg.parameters = ToStringUtil.toObject(rs.getString(idx.next()), new TypeReference<LinkedHashMap<String,String>>() {});
			msg.contextSettings = ToStringUtil.toObject(rs.getString(idx.next()), new TypeReference<LinkedHashMap<String,String>>() {});
			idx.next();
			msg.domain = rs.getString(idx.next());
			msg.lock = rs.getTimestamp(idx.next());
			msg.current = rs.getTimestamp(idx.next());
			return msg;
		}
	};

	Select<BeeQueueMessage, Long> LOAD_MESSAGE = new Select<BeeQueueMessage, Long>(
	SELECT_MESSAGE +  "WHERE MSG_ID = ?",
	MESSAGE_FACTORY
	, DbConstants.LONG_SQL_PREPARE);
	
	Select<BeeQueueMessage, Void> PICK_EMMITED_MESSAGES = new Select<BeeQueueMessage, Void>(
	SELECT_MESSAGE + "WHERE LOCK_TS < CURRENT_TIMESTAMP AND MSG_STATE = \'"+MessageState.EMITTED+"\'",
	MESSAGE_FACTORY
	, null);
	
	Select<BeeQueueJob, Long> LOAD_MESSAGE_JOBS = new Select<BeeQueueJob, Long>(
		"SELECT J.JOB_ID,J.MSG_ID,J.JOB_STATE,J.RESPONSIBLE,J.JOB_NAME, M.MSG_NAME, M.DOMAIN_CD, M.MSG_STATE " +
		"FROM NN_JOB J, NN_MESSAGE M " +
		"WHERE J.MSG_ID = M.MSG_ID AND M.MSG_ID = ?",
	new JdbcFactory<BeeQueueJob, Object>() {
		@Override
		public BeeQueueJob newInstance(ResultSet rs, Object input, Index idx)
				throws SQLException {
			BeeQueueJob job = new BeeQueueJob();
			job.message = new BeeQueueMessage();
			job.id = rs.getLong(idx.next());
			job.message.id = job.msgId = rs.getLong(idx.next());
			job.state = JobState.valueOf(rs.getString(idx.next()));
			job.responsible = SqlUtil.toBoolean(rs.getString(idx.next()));
			job.jobName = rs.getString(idx.next());
			job.message.name = rs.getString(idx.next());
			job.message.domain = rs.getString(idx.next());
			job.message.state = MessageState.valueOf(rs.getString(idx.next()));
			return job;
		}
	}, DbConstants.LONG_SQL_PREPARE);

	public static final String SELECT_STAGE_SQL = "SELECT S.STAGE_ID, J.JOB_ID, J.MSG_ID, S.STAGE_STATE, S.RETRIES_LEFT, S.STAGE_NAME, " +
			"J.JOB_STATE,J.JOB_NAME, M.MSG_NAME, M.DOMAIN_CD, M.MSG_STATE, M.MSG_PARAMETERS, M.CONTEXT_SETTINGS " +
			"FROM NN_JOB J, NN_STAGE S , NN_MESSAGE M " +
			"WHERE J.JOB_ID = S.JOB_ID AND J.MSG_ID = M.MSG_ID ";
	public static final JdbcFactory<BeeQueueStage, Object> STAGE_FACTORY = new JdbcFactory<BeeQueueStage, Object>() {
		@Override
		public BeeQueueStage newInstance(ResultSet rs, Object input, Index idx)
				throws SQLException {
			BeeQueueStage stage = new BeeQueueStage();
			stage.job = new BeeQueueJob();
			stage.job.message = new BeeQueueMessage();
			
			stage.stageId = rs.getLong(idx.next());
			stage.job.id = stage.jobId = rs.getLong(idx.next());
			stage.job.message.id  = stage.job.msgId = rs.getLong(idx.next());
			stage.state = StageState.valueOf(rs.getString(idx.next()));
			stage.retriesLeft = rs.getLong(idx.next());
			stage.stageName = rs.getString(idx.next());
			stage.job.state = JobState.valueOf(rs.getString(idx.next()));
			stage.job.jobName = rs.getString(idx.next());
			stage.job.message.name = rs.getString(idx.next());
			stage.job.message.domain = rs.getString(idx.next());
			stage.job.message.state = MessageState.valueOf(rs.getString(idx.next()));
			stage.job.message.parameters = ToStringUtil.toObject(rs.getString(idx.next()), new TypeReference<LinkedHashMap<String,String>>() {});
			stage.job.message.contextSettings = ToStringUtil.toObject(rs.getString(idx.next()), new TypeReference<LinkedHashMap<String,String>>() {});

			return stage;
		}
	};


	Select<BeeQueueStage, Long> LOAD_MESSAGE_STAGES = new Select<BeeQueueStage, Long>(
			SELECT_STAGE_SQL + "AND M.MSG_ID = ?",
			STAGE_FACTORY, DbConstants.LONG_SQL_PREPARE);
	
	Select<BeeQueueStage, Long> LOAD_READY_STAGES = new Select<BeeQueueStage, Long>(
				SELECT_STAGE_SQL + "AND S.STAGE_STATE='" + StageState.READY + "'",
				STAGE_FACTORY, null);

	Update<BeeQueueStage> UPDATE_STAGE_STATUS = new Update<BeeQueueStage>(
			"UPDATE NN_STAGE SET STAGE_STATE=?, RETRIES_LEFT=? WHERE STAGE_ID=? AND STAGE_STATE=?",
			new SqlPrepare<BeeQueueStage>() {
				@Override
				public void invoke(PreparedStatement pstmt, BeeQueueStage input, Index idx)
						throws SQLException {
					pstmt.setString(idx.next(), input.newState.name());
					pstmt.setLong(idx.next(), input.retriesLeft);
					pstmt.setLong(idx.next(), input.stageId);
					pstmt.setString(idx.next(), input.state.name());
				}
			});

	Select<BeeQueueRun, Long> LOAD_MESSAGE_RUNS = new Select<BeeQueueRun, Long>(
	"SELECT R.RUN_ID, S.STAGE_ID, J.JOB_ID, J.MSG_ID, R.WORKER_ID, R.PID, R.RUN_STATE, R.CMD, R.UP_TS, R.DOWN_TS " +
	"FROM NN_JOB J, NN_STAGE S, NN_RUN R " +
	"WHERE J.JOB_ID = S.JOB_ID AND S.STAGE_ID = R.STAGE_ID AND J.MSG_ID = ?",
	new JdbcFactory<BeeQueueRun, Object>() {
		@Override
		public BeeQueueRun newInstance(ResultSet rs, Object input, Index idx)
				throws SQLException {
			BeeQueueRun run = new BeeQueueRun();
			run.id = rs.getLong(idx.next());
			run.stageId = rs.getLong(idx.next());
			run.jobId = rs.getLong(idx.next());
			run.msgId = rs.getLong(idx.next());
			run.workerId = rs.getLong(idx.next());
			run.pid = rs.getString(idx.next());
			run.state = RunState.valueOf(rs.getString(idx.next()));
			run.cmd = rs.getString(idx.next());
			run.upTimeStamp = rs.getTimestamp(idx.next());
			run.downTimeStamp = rs.getTimestamp(idx.next());
			return run;
		}
	}, DbConstants.LONG_SQL_PREPARE);

	Update<BeeQueueMessage> INSERT_MESSAGE = new Update<BeeQueueMessage>(
			"INSERT INTO NN_MESSAGE (MSG_ID,MSG_NAME,MSG_STATE,MSG_PARAMETERS,CONTEXT_SETTINGS,USER_CD,DOMAIN_CD) VALUES (?,?,?,?,?,?,?)",
			new SqlPrepare<BeeQueueMessage>() {
				@Override
				public void invoke(PreparedStatement pstmt, BeeQueueMessage input, Index idx)
						throws SQLException {
					pstmt.setLong(idx.next(), input.id);
					pstmt.setString(idx.next(), input.name);
					pstmt.setString(idx.next(), input.state.name());
					pstmt.setString(idx.next(), ToStringUtil.toString(input.parameters) );
					pstmt.setString(idx.next(), ToStringUtil.toString(input.contextSettings) );
					pstmt.setString(idx.next(), null );
					pstmt.setString(idx.next(), input.domain );
				}
			});
	
	Update<BeeQueueJob> INSERT_JOB = new Update<BeeQueueJob>(
			"INSERT INTO NN_JOB (JOB_ID,MSG_ID,JOB_STATE,RESPONSIBLE,JOB_NAME) VALUES (?,?,?,?,?)",
			new SqlPrepare<BeeQueueJob>() {
				@Override
				public void invoke(PreparedStatement pstmt, BeeQueueJob input, Index idx)
						throws SQLException {
					pstmt.setLong(idx.next(), input.id);
					pstmt.setLong(idx.next(), input.msgId);
					pstmt.setString(idx.next(), input.state.name());
					pstmt.setString(idx.next(), SqlUtil.fromBoolean(input.responsible));
					pstmt.setString(idx.next(), input.jobName );
				}
			});
	Update<BeeQueueStage> INSERT_STAGE = new Update<BeeQueueStage>(
			"INSERT INTO NN_STAGE (STAGE_ID,JOB_ID,STAGE_STATE,RETRIES_LEFT,STAGE_NAME) VALUES (?,?,?,?,?)",
			new SqlPrepare<BeeQueueStage>() {
				@Override
				public void invoke(PreparedStatement pstmt, BeeQueueStage input, Index idx)
						throws SQLException {
					pstmt.setLong(idx.next(), input.stageId);
					pstmt.setLong(idx.next(), input.jobId);
					pstmt.setString(idx.next(), input.state.name());
					pstmt.setLong(idx.next(), input.retriesLeft);
					pstmt.setString(idx.next(), input.stageName );
				}
			});
	Update<BeeQueueRun> INSERT_RUN = new Update<BeeQueueRun>(
			"INSERT INTO NN_RUN (RUN_ID,STAGE_ID,WORKER_ID,PID,RUN_STATE,CMD,UP_TS,DOWN_TS) VALUES (?,?,?,?,?,?,CURRENT_TIMESTAMP,NULL)",
			new SqlPrepare<BeeQueueRun>() {
				@Override
				public void invoke(PreparedStatement pstmt, BeeQueueRun input, Index idx)
						throws SQLException {
					pstmt.setLong(idx.next(), input.id);
					pstmt.setLong(idx.next(), input.stageId);
					pstmt.setLong(idx.next(), input.workerId);
					pstmt.setString(idx.next(), input.pid);
					pstmt.setString(idx.next(), input.state.name());
					pstmt.setString(idx.next(), input.cmd );
				}
			});
	Update<BeeQueueRun> UPDATE_RUN = new Update<BeeQueueRun>(
			"UPDATE NN_RUN PID = ? ,RUN_STATE = ? ,DOWN_TS = CURRENT_TIMESTAMP " +
			"WHERE RUN_ID = ?",
			new SqlPrepare<BeeQueueRun>() {
				@Override
				public void invoke(PreparedStatement pstmt, BeeQueueRun input, Index idx)
						throws SQLException {
					pstmt.setString(idx.next(), input.pid);
					pstmt.setString(idx.next(), input.state.name());
					pstmt.setLong(idx.next(), input.id);
				}
			});
	
	Update<BeeQueueMessage> UPDATE_MESSAGE_STATE = new Update<BeeQueueMessage>(
			"UPDATE " + NN_MESSAGE + " SET MSG_STATE=?, LOCK_TS=? WHERE MSG_ID=? AND LOCK_TS = ? ", 
			new SqlPrepare<BeeQueueMessage>() {
				@Override
				public void invoke(PreparedStatement pstmt, BeeQueueMessage input, Index idx)
						throws SQLException {
					pstmt.setString(idx.next(), input.state.name());
					pstmt.setTimestamp(idx.next(), input.newLock());
					pstmt.setLong(idx.next(), input.id);
					pstmt.setTimestamp(idx.next(), input.lock);
				}
			});

}
