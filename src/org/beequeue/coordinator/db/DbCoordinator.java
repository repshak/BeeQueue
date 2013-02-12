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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.beequeue.coordinator.Coordiantor;
import org.beequeue.hash.ContentTree;
import org.beequeue.hash.FileCollection;
import org.beequeue.hash.FileEntry;
import org.beequeue.hash.HashKey;
import org.beequeue.hash.HashKeyResource;
import org.beequeue.hash.HashOutput;
import org.beequeue.hash.HashStoreQueries;
import org.beequeue.host.Host;
import org.beequeue.launcher.BeeQueueHome;
import org.beequeue.msg.BeeQueueDomain;
import org.beequeue.msg.BeeQueueJob;
import org.beequeue.msg.BeeQueueEvent;
import org.beequeue.msg.BeeQueueEventDrilldown;
import org.beequeue.msg.BeeQueueProcess;
import org.beequeue.msg.BeeQueueRun;
import org.beequeue.msg.BeeQueueStage;
import org.beequeue.msg.DomainState;
import org.beequeue.msg.JobState;
import org.beequeue.msg.MessageState;
import org.beequeue.msg.StageState;
import org.beequeue.sql.JdbcResourceTracker;
import org.beequeue.sql.SqlUtil;
import org.beequeue.sql.TransactionContext;
import org.beequeue.template.JobTemplate;
import org.beequeue.template.MessageTemplate;
import org.beequeue.template.StageTemplate;
import org.beequeue.util.BeeException;
import org.beequeue.util.Dirs;
import org.beequeue.util.JsonTable;
import org.beequeue.util.LoggingUtil;
import org.beequeue.util.Throwables;
import org.beequeue.util.ToStringUtil;
import org.beequeue.util.Triple;
import org.beequeue.util.Tuple;
import org.beequeue.worker.HostState;
import org.beequeue.worker.Singletons;
import org.beequeue.worker.Worker;
import org.beequeue.worker.WorkerData;
import org.beequeue.worker.WorkerState;

public class DbCoordinator implements Coordiantor {
	private static final String CONNECTION_TRACKER = "conn";
	public String driver;
	public String url;
	public String user;
	public String password;
	public String[] initSql;
	
	public Connection connection() throws BeeException {
		try {
			JdbcResourceTracker tracker = TransactionContext.searchResource(
					JdbcResourceTracker.class, CONNECTION_TRACKER);
			if (tracker == null) {
				Connection conn;
				Class.forName(driver);
				Connection connection = DriverManager.getConnection(url, user,
						password);
				if (initSql != null) {
					for (String query : initSql) {
						connection.createStatement().execute(query);
					}
				}
				conn = connection;
				tracker = new JdbcResourceTracker(CONNECTION_TRACKER, conn);
				TransactionContext.register(tracker);
			}
			return tracker.getResource();
		} catch (Exception e) {
			throw new BeeException(e);
		}
	}
	
	

	public long getNewId(String tableName)throws BeeException {
		return IdFactory.getIdRange(tableName, this).getNext(this);
	}
	
	@Override
	public String selectAnyTable(String table) {
		Connection connection = null;
		try {
			connection = connection();
			String t;
			String drill = null;
			String parent = null;
			if( table != null && !table.equals("") && !table.equals("/") ){
				t = table;
				parent = "db/";
			}else{
				t = "sys.systables where TABLETYPE = 'T'";
				drill = "db/%1%";
				parent = "index.txt";
			}
			String q = "select * from "+t;
			ResultSet rs = connection.createStatement().executeQuery(q);
			return JsonTable.queryToJson(rs, q, parent, drill);
		} catch (SQLException e) {
			throw new BeeException(e);
		}
	}
	


	@Override
	public String query(String q) {
		Connection connection = null;
		try {
			connection = connection();
			ResultSet rs = connection.createStatement().executeQuery(q);
			return JsonTable.queryToJson(rs, q, null, null);
		} catch (SQLException e) {
			throw new BeeException(e);
		}
	}

	
	@Override
	public void ensureHost(WorkerData wh) {
		try {
			String hostName = BeeQueueHome.instance.getHostName();
			List<Host> r = HostWorkerQueries.LOAD_HOST.query(connection(), hostName);
			if( r.size() == 1 ){
				wh.host = r.get(0);
				return;
			}
			wh.host = Host.localHost();
			wh.host.state = HostState.READY;
			wh.host.cloud.name = Singletons.getGlobalConfig().findCloudForHost(wh.host.hostName);
			if( 1 != HostWorkerQueries.INSERT_HOST.update(connection(), wh.host)){
				throw new BeeException("Cannot save host.");
			}
		} catch (RuntimeException e) {
			TransactionContext.setRollbackOnly();
			throw e;
		}
		
	}


	@Override
	public void storeStatistics(WorkerData wh) {
		if( 1 == HostWorkerQueries.INSERT_HOST_STATISTICS.update(connection(), wh)){
			//TODO update worker status
		}
		
	}


	@Override
	public void ensureWorker(WorkerData wh) {
		//ensure worker
		if( wh.worker == null ){
			Worker worker = new Worker();
			worker.id = getNewId(HostWorkerQueries.WORKER_TABLE);
			worker.nextBeat = System.currentTimeMillis();
			worker.pid = BeeQueueHome.instance.getPid();
			worker.state = wh.host.toWorkerState();
			worker.hostName = wh.host.hostName;
			if( 1 == HostWorkerQueries.INSERT_WORKER.update(connection(), worker) ){
				wh.worker = worker;
			}else{
				throw new BeeException("Cannot create worker")
								.memo("worker", worker);
			}
		}else{
			wh.worker = HostWorkerQueries.LOAD_WORKER_BY_ID.queryOne(connection(), wh.worker.id);
		}
		List<Worker> allWorkersInTheGroup = HostWorkerQueries.LOAD_WORKERS.query(connection(), wh.host.cloud.name);
		wh.calculateNextBeat(allWorkersInTheGroup);
		wh.calculateNextStatus();
		HostWorkerQueries.UPDATE_WORKER.update(connection(), wh.worker);
		for (Worker worker : allWorkersInTheGroup) {
			if( worker.hostName.equals(wh.worker.hostName) && worker.id != wh.worker.id ){
				worker.state = WorkerState.TERMINATED;
				HostWorkerQueries.UPDATE_WORKER.update(connection(), worker);
			}
		}
	}



	@Override
	public HashKey push(File file) {
		try {
			FileCollection scan = FileCollection.scan(file);
			if( scan.entries.length == 0 ){
				return null;
			}
			Set<HashKey> hashes = scan.getAllCodes();
			List<HashKey> inDbAlready = HashStoreQueries.CHECK_HASH_KEY.query(connection(), hashes);
			hashes.removeAll(inDbAlready);
			Map<HashKey,FileEntry> entriesToStreamIntoDb =scan.selectByHashes(hashes);
			for (FileEntry fileEntry : entriesToStreamIntoDb.values() ) {
				HashStoreQueries.STREAM_CONTENT_IN.update(connection(), fileEntry.input(file));
			}
			if(scan.isFile()){
				return scan.getFileKey();
			}else{
				HashKey entriesDataKey = scan.getEntriesDataKey();
				inDbAlready = HashStoreQueries.CHECK_HASH_KEY.query(connection(), Collections.singleton(entriesDataKey));
				System.out.println("push:"+ToStringUtil.toString(scan.entries));
				if(inDbAlready.size()==0){
					HashStoreQueries.STREAM_CONTENT_IN.update(connection(), scan.getEntriesData());
				}
				return entriesDataKey;
			}
		} catch (FileNotFoundException e) {
			throw new BeeException(e);
		}
	}

	@Override
	public void pull(HashKey code, File destination) {
		File writeTo =  destination.exists() ? addPrefix(destination, "tmp") : destination ;
		try {
			if(code.type==HashKeyResource.F){
					HashStoreQueries.STREAM_CONTENT_OUT.query(connection(),new HashOutput(code, new FileOutputStream(writeTo)) );
			}else{
				File hashStoreFile = new File(writeTo, FileCollection.HASH_STORE_FILE);
				Dirs.ensureParentDirExists(hashStoreFile);
				HashStoreQueries.STREAM_CONTENT_OUT.query(connection(), 
						new HashOutput(code, new FileOutputStream(hashStoreFile)));
				FileCollection tree = FileCollection.read(new FileInputStream(hashStoreFile));
				for (FileEntry fileEntry : tree.entries) {
					HashStoreQueries.STREAM_CONTENT_OUT.query(connection(),fileEntry.output(writeTo));
					fileEntry.updateAttributes(writeTo);
				}
				if(writeTo!=destination){
					File backup = addPrefix(destination, "backup");
					Dirs.deleteDirectory(backup);
					destination.renameTo(backup);
					writeTo.renameTo(destination);
				}
			}
		} catch (Exception e) {
			throw BeeException.cast(e);
		}
		
	}



	public File addPrefix(File file, String prefix) {
		return new File(file.getParentFile(),"." +
				prefix +
				"-"+file.getName());
	}



	@Override
	public void sweep() {
		// TODO Auto-generated method stub
		
	}



	@Override
	public ContentTree push(File source, String content) {
		ContentTree contentTree = new ContentTree();
		contentTree.name = content;
		contentTree.hashKey = push(source);
		SqlUtil.doUpdateInsertUpdate( connection(), 
				HashStoreQueries.UPDATE_CONTENT_TREE_SUMMARY, 
				HashStoreQueries.INSERT_CONTENT_TREE_SUMMARY, 
				contentTree );
		HashStoreQueries.CONTENT_TREE_HISTORY.update(connection(), contentTree);
		return contentTree ;
	}

	public ContentTree sync(ContentTree contentTree, File destination) {
		List<ContentTree> query = HashStoreQueries.CHECK_CONTENT_TREE_UPDATE.query(connection(),contentTree);
		if(query.size()==0){
			return null;
		}
		ContentTree toDownloadTree = query.get(0);
		pull(toDownloadTree.hashKey,destination);
		return toDownloadTree;
	}



	@Override
	public void ensureDomains(List<BeeQueueDomain> activeDomains) {
		List<BeeQueueDomain> insert = new ArrayList<BeeQueueDomain>();
		List<BeeQueueDomain> update = new ArrayList<BeeQueueDomain>();
		List<BeeQueueDomain> dejavu = new ArrayList<BeeQueueDomain>();
		List<BeeQueueDomain> query = DomainQueries.LOAD_DOMAINS.query(connection(), null);
		for (BeeQueueDomain fromDb : query) {
			boolean match = false; 
			for (BeeQueueDomain active : activeDomains) {
				if(fromDb.name.equals(active.name)){
					active.state = fromDb.state == DomainState.DEJA_VU ? DomainState.UP : fromDb.state ;
					if(active.state != fromDb.state){
						update.add(active);
					}
					match = true;
					break;
				}
			}
			if(!match){
				if( fromDb.state != DomainState.DEJA_VU ){
					fromDb.state = DomainState.DEJA_VU;
					update.add(fromDb);
				}
				dejavu.add(fromDb);
			}
		}
		for (BeeQueueDomain active : activeDomains) {
			if(active.state == null){
				active.state = DomainState.UP;
				insert.add(active);
			}
		}
		for (BeeQueueDomain beeQueueDomain : insert) {
			DomainQueries.INSERT_DOMAIN.update(connection(), beeQueueDomain);
		}
		for (BeeQueueDomain beeQueueDomain : update) {
			DomainQueries.UPDATE_DOMAIN.update(connection(), beeQueueDomain);
			
		}
	}



	@Override
	public void storeMessage(BeeQueueEvent msg) {
		if( msg.id > 0 ){
			MessageQueries.UPDATE_MESSAGE_STATE.update(connection(),msg);
		}else{
			msg.id = getNewId(MessageQueries.NN_MESSAGE);
			MessageQueries.INSERT_MESSAGE.update(connection(),msg);
		}
	}



	@Override
	public BeeQueueEventDrilldown checkMessage(long messageId) {
		BeeQueueEventDrilldown d = new BeeQueueEventDrilldown();
		d.msg = MessageQueries.LOAD_MESSAGE.query(connection(), messageId).get(0);
		d.jobs = MessageQueries.LOAD_MESSAGE_JOBS.query(connection(), messageId);
		d.stages = MessageQueries.LOAD_MESSAGE_STAGES.query(connection(), messageId);
		d.runs = MessageQueries.LOAD_MESSAGE_RUNS.query(connection(), messageId);
		return d;
	}



	@Override
	public void processEmittedMessages() {
		List<BeeQueueEvent> query = MessageQueries.PICK_EMMITED_MESSAGES.query(connection(), null);
		for (BeeQueueEvent msg : query) {
			if(1 == MessageQueries.UPDATE_MESSAGE_STATE.update(connection(), msg)){
				MessageTemplate mt = Singletons.getGlobalConfig().activeDomains().get(msg.domain).messageTemplate(msg.name);
				try{
					JobTemplate[] jobs = mt.jobs;
					boolean alreadyHaveOneResposible = false;
					for (int i = 0; i < jobs.length; i++) {
						JobTemplate jt = jobs[i];
						if( !jt.checkFilters(msg) ){
							continue;
						}
						if(jt.responsbile){
							if(alreadyHaveOneResposible){
								continue;
							}else{
								alreadyHaveOneResposible = true;
							}
						}
						BeeQueueJob job = new BeeQueueJob();
						job.id = getNewId(MessageQueries.NN_JOB);
						job.msgId = msg.id;
						job.jobName = jt.jobName;
						job.state = JobState.IN_PROCESS;
						job.responsible = jt.responsbile ;
						MessageQueries.INSERT_JOB.update(connection(), job);
						StageTemplate[] stageTemplates = jt.stages;
						for (int j = 0; j < stageTemplates.length; j++) {
							BeeQueueStage stage = new BeeQueueStage();
							stage.stageId = getNewId(MessageQueries.NN_STAGE);
							stage.jobId = job.id;
							StageTemplate st = stageTemplates[j];
							stage.retriesLeft = st.retryStrategy.maxTries;
							stage.state = st.dependOnStage == null  || st.dependOnStage.length == 0 ? StageState.READY : StageState.BLOCKED ;
							stage.stageName = st.stageName;
							MessageQueries.INSERT_STAGE.update(connection(), stage);
						}
					}
					msg.state = MessageState.IN_PROCESS;
					msg.lock.value = msg.lock.newLock();
					if( 1 != MessageQueries.UPDATE_MESSAGE_STATE.update(connection(), msg) ){
						throw new BeeException("cannot update status");
					}
				}catch (Exception e) {
					throw BeeException.cast(e)
					.memo("msg",msg)
					.memo("mt",mt);
				}
			}
		}
		
	}

	@Override
	public BeeQueueStage pickStageToRun() {
		for (BeeQueueStage readyStage : MessageQueries.LOAD_READY_STAGES.query(connection(), null)) {
			readyStage.retriesLeft--;
			readyStage.newState = StageState.PREPARE_TO_RUN;
			if(updateStage(readyStage)){
				return readyStage;
			}
		}
		return null;
	}

	@Override
	public void storeRun(BeeQueueRun run) {
		if(run.id > 0){
			if(run.isInFinalState()){
				MessageQueries.REFRESH_DOWN_TS_RUN.update(connection(), run);
			}else if(run.justUpTimeStamp){
				MessageQueries.REFRESH_UP_TS_RUN.update(connection(), run);
			}else{
				MessageQueries.UPDATE_RUN.update(connection(), run);
			}
		}else{
			run.id = getNewId(MessageQueries.NN_RUN);
			MessageQueries.INSERT_RUN.update(connection(), run);
		}
		
	}



	@Override
	public List<BeeQueueRun> allCurrentRuns(WorkerData instance) {
		return MessageQueries.LOAD_CURRENT_MESSAGE_RUNS.query(connection(), instance.host.hostName);
	}



	@Override
	public List<BeeQueueProcess> allActiveProcessesOnHost(Host host) {
		return MessageQueries.ALIVE_PROCESSES.query(connection(), host.hostName);
	}


	@Override
	public void storeProcess(BeeQueueProcess process) {
		SqlUtil.doUpdateInsertUpdate(connection(), MessageQueries.UPDATE_PROCESS, MessageQueries.INSERT_PROCESS, process);
	}


	@Override
	public boolean updateStage(BeeQueueStage readyStage) {
		if(1 == MessageQueries.UPDATE_STAGE_STATUS.update(connection(), readyStage)){
			readyStage.state = readyStage.newState;
			return true;
		}
		return false;
	}



	@Override
	public void updateFinishedJobsAndMessages() {
		updateInProcessJobs();
		updateInProcessMessages();
	}



	public void updateInProcessJobs() {
		List<BeeQueueStage> query = MessageQueries.LOAD_IN_PROCESS_JOBS_STAGES.query(connection(), null);
		Map<Long,Tuple<BeeQueueJob,Map<String, BeeQueueStage>>> jobs = new LinkedHashMap<Long, Tuple<BeeQueueJob,Map<String, BeeQueueStage>>>();
		for (int i = 0; i < query.size(); i++) {
			BeeQueueStage stage = query.get(i);
			BeeQueueJob job = stage.job;
			Tuple<BeeQueueJob, Map<String, BeeQueueStage>> jobTuple;
			if(!jobs.containsKey(job.id)){
				jobTuple = new Tuple<BeeQueueJob, Map<String, BeeQueueStage>>(job, new HashMap<String,BeeQueueStage>());
				jobs.put(job.id, jobTuple);
			}else{
				jobTuple = jobs.get(job.id);
			}
			jobTuple.o2.put(stage.stageName, stage);
		}
		for (Long id : jobs.keySet()) {
			Tuple<BeeQueueJob, Map<String,BeeQueueStage>> tuple = jobs.get(id);
			boolean allSuccess = true ;
			for (String stageName: tuple.o2.keySet()) {
				BeeQueueStage stage = tuple.o2.get(stageName);
				if( stage.state == StageState.BLOCKED ){
					String[] dependOnStages = stage.stageTemplate().dependOnStage;
					boolean allDependOnStagesAreInSuccessState = true ;
					for (int i = 0; i < dependOnStages.length; i++) {
						BeeQueueStage dependOnStage = tuple.o2.get(dependOnStages[i]);
						if(dependOnStage == null){
							LoggingUtil.warn("Stage:" +dependOnStages[i] + " is not defined. Assuming success end state.", tuple);
						}else{
							if(!dependOnStage.state.successEndState()){
								allDependOnStagesAreInSuccessState = false ;
							}
						}
					}
					if(allDependOnStagesAreInSuccessState) {
						stage.newState = StageState.READY ;
						updateStage(stage);
					}
				}else if( stage.state == StageState.FAILURE ){
					tuple.o1.state = JobState.FAILURE;
					allSuccess = false;
					break;
				}
				if( !stage.state.successEndState() ){
					allSuccess = false;
				}
			}
			if(allSuccess){
				tuple.o1.state = JobState.SUCCESS;
			}
			if( tuple.o1.state != JobState.IN_PROCESS ){
				MessageQueries.UPDATE_JOB_STATUS.update(connection(), tuple.o1);
			}
		}
	}

	public void updateInProcessMessages() {
		List<BeeQueueJob> query = MessageQueries.LOAD_IN_PROCESS_MESSAGE_JOBS.query(connection(), null);
		Map<Long,Triple<BeeQueueEvent,List<BeeQueueJob>,BeeQueueJob>> messages = new LinkedHashMap<Long, Triple<BeeQueueEvent,List<BeeQueueJob>,BeeQueueJob>>();
		for (int i = 0; i < query.size(); i++) {
			BeeQueueJob job = query.get(i);
			BeeQueueEvent message = job.message;
			Triple<BeeQueueEvent,List<BeeQueueJob>,BeeQueueJob> t;
			if(!messages.containsKey(message.id)){
				t = new Triple<BeeQueueEvent, List<BeeQueueJob>, BeeQueueJob>(message, new ArrayList<BeeQueueJob>(),null);
				messages.put(message.id, t);
			}else{
				t = messages.get(message.id);
			}
			t.o2.add(job);
			if(job.responsible){
				if(t.o3!=null){
					throw new BeeException("only one responsible job should be assigned per message:")
					.memo("",t);
				}
				t.o3 = job;
			}
		}
		
		for (Long id : messages.keySet()) {
			Triple<BeeQueueEvent,List<BeeQueueJob>,BeeQueueJob> t = messages.get(id);
			BeeQueueJob responsibleJob = t.o3;
			BeeQueueEvent msg = t.o1;
			if(responsibleJob != null){
				if( responsibleJob.state == JobState.FAILURE ){
					msg.state = MessageState.FAILURE;
				}else if( responsibleJob.state == JobState.SUCCESS ){
					msg.state = MessageState.SUCCESS;
				}
			}else{
				boolean allSuccess = true ;
				for (BeeQueueJob job : t.o2) {
					if( job.state == JobState.FAILURE ){
						msg.state = MessageState.FAILURE;
						allSuccess = false;
						break;
					}
					if( job.state != JobState.SUCCESS &&  job.state != JobState.CANCELED ){
						allSuccess = false;
					}
				}
				if(allSuccess){
					msg.state = MessageState.SUCCESS;
				}
			}
			if( msg.state != MessageState.IN_PROCESS ){
				MessageQueries.UPDATE_MESSAGE_STATUS.update(connection(), msg);
			}
		}
	}
	
}
