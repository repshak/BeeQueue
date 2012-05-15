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
import java.io.IOException;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.beequeue.coordinator.Coordiantor;
import org.beequeue.hash.FileCollection;
import org.beequeue.hash.FileEntry;
import org.beequeue.hash.HashKey;
import org.beequeue.hash.HashKeyResource;
import org.beequeue.hash.HashOutput;
import org.beequeue.hash.HashStoreQueries;
import org.beequeue.host.Cloud;
import org.beequeue.host.Host;
import org.beequeue.launcher.BeeQueueHome;
import org.beequeue.sql.JdbcResourceTracker;
import org.beequeue.sql.TransactionContext;
import org.beequeue.util.BeeException;
import org.beequeue.util.Dirs;
import org.beequeue.util.ToStringUtil;
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
			if( table != null && !table.equals("") && !table.equals("/") ){
				t = table.substring(1);
			}else{
				t = "sys.systables";
			}
			String q = "select * from "+t;
			ResultSet rs = connection.createStatement().executeQuery(q );
			return queryToJson(rs, q);
		} catch (SQLException e) {
			throw new BeeException(e);
		}
	}
	
	public String queryToJson(ResultSet rs, String q) throws SQLException {
		Map<String,Object> d = new LinkedHashMap<String, Object>();
		ResultSetMetaData md = rs.getMetaData();
		ArrayList<Object> header = new ArrayList<Object>();
		d.put("query", q);
		d.put("aoColumns", header);
		for (int i = 0; i < md.getColumnCount(); i++) {
			header.add(buildColumn(md.getColumnName(i+1)));
		}
		ArrayList<Object> data = new ArrayList<Object>();
		d.put("aaData", data);
		while(rs.next()){
			ArrayList<Object> row = new ArrayList<Object>();
			data.add(row);
			for (int i = 0; i < md.getColumnCount(); i++) {
				Object object = rs.getObject(i+1);
				if(object instanceof Clob){
				Clob c = (Clob) object;
				object = c.getSubString(1, (int)c.length());
				}
				row.add(object);
				
			}
		}
		rs.close();
		return ToStringUtil.toString(d);
	}
	
	public LinkedHashMap<String, Object> buildColumn(String columnName) {
		LinkedHashMap<String, Object> col = new LinkedHashMap<String, Object>();
		col.put("sTitle", columnName);
		return col;
	}


	@Override
	public String query(String q) {
		// TODO Auto-generated method stub
		return null;
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
								.withPayload(worker);
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
			List<FileEntry> entriesToStreamIntoDb =scan.selectByHashes(hashes);
			for (FileEntry fileEntry : entriesToStreamIntoDb) {
				HashStoreQueries.STREAM_CONTENT_IN.update(connection(), fileEntry.input(file));
			}
			if(scan.isFile()){
				return scan.getFileKey();
			}else{
				HashKey entriesDataKey = scan.getEntriesDataKey();
				inDbAlready = HashStoreQueries.CHECK_HASH_KEY.query(connection(), Collections.singleton(entriesDataKey));
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
		if(code.type==HashKeyResource.F){
			try {
				HashStoreQueries.STREAM_CONTENT_OUT.query(connection(),new HashOutput(code, new FileOutputStream(writeTo)) );
			} catch (FileNotFoundException e) {
				throw new BeeException(e);
			} 
		}else{
			try {
				File hashStoreFile = new File(writeTo, FileCollection.HASH_STORE_FILE);
				Dirs.ensureParentDirExists(hashStoreFile);
				HashStoreQueries.STREAM_CONTENT_OUT.query(connection(), 
						new HashOutput(code, new FileOutputStream(hashStoreFile)));
				FileCollection tree = FileCollection.read(new FileInputStream(hashStoreFile));
				for (FileEntry fileEntry : tree.entries) {
					fileEntry.output(writeTo);
					HashStoreQueries.STREAM_CONTENT_OUT.query(connection(),fileEntry.output(destination)); 
				}
				if(writeTo!=destination){
					File backup = addPrefix(destination, "backup");
					destination.renameTo(backup);
					writeTo.renameTo(destination);
				}
			} catch (IOException e) {
				throw new BeeException(e);
			}
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
}
