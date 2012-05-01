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

import java.sql.Clob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.beequeue.coordinator.Coordiantor;
import org.beequeue.host.Host;
import org.beequeue.host.HostGroup;
import org.beequeue.launcher.BeeQueueHome;
import org.beequeue.sql.DalException;
import org.beequeue.sql.Index;
import org.beequeue.sql.JdbcFactory;
import org.beequeue.sql.JdbcResourceTracker;
import org.beequeue.sql.Select;
import org.beequeue.sql.SqlPrepare;
import org.beequeue.sql.TransactionContext;
import org.beequeue.sql.Update;
import org.beequeue.util.ToStringUtil;
import org.beequeue.worker.HostState;
import org.beequeue.worker.WorkerHelper;

public class DbCoordinator implements Coordiantor {
	private static final String CONNECTION_TRACKER = "conn";
	public String driver;
	public String url;
	public String user;
	public String password;
	public String[] initSql;
	
	public Connection connection() throws DalException {
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
			throw new DalException(e);
		}
	}
	
	

	public long getNewId(String tableName)throws DalException {
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
			throw new DalException(e);
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

	

	private static final Select<Host, String> LOAD_HOST = new Select<Host, String>(
			"SELECT H.HOST_CD, H.HOST_STATE, H.HOST_IP, H.HOST_FQDN, G.HOST_GROUP_CD, G.GROUP_DESCRIPTION " +
					" FROM NN_HOST H, NN_HOST_GROUP G WHERE H.HOST_CD = ? AND H.HOST_GROUP_CD = G.HOST_GROUP_CD" , new JdbcFactory<Host, String>() {
				
				@Override
				public Host newInstance(ResultSet rs, String input, Index idx)
						throws SQLException {
					
					Host host = new Host();
					host.hostName = rs.getString(idx.next());
					host.state = HostState.valueOf(rs.getString(idx.next()));
					host.ip = rs.getString(idx.next());
					host.fqdn = rs.getString(idx.next());
					host.group.name = rs.getString(idx.next());
					host.group.description = rs.getString(idx.next());
					return host;
				}
			}, DbConstants.STRING_SQL_PREPARE);

	private static final Select<HostGroup, String> LOAD_HOST_GROUP = new Select<HostGroup, String>(
			"SELECT HOST_GROUP_CD, GROUP_DESCRIPTION " +
			" FROM NN_HOST_GROUP WHERE HOST_GROUP_CD = ? " , 
			new JdbcFactory<HostGroup, String>() {
				@Override
				public HostGroup newInstance(ResultSet rs, String input, Index idx)
						throws SQLException {
					HostGroup group = new HostGroup();
					group.name = rs.getString(idx.next());
					group.description = rs.getString(idx.next());
					return group;
				}
			}, DbConstants.STRING_SQL_PREPARE);
	
	public static Update<Host> INSERT_HOST = new Update<Host>("INSERT INTO NN_HOST (HOST_CD, HOST_STATE, HOST_IP, HOST_FQDN, HOST_GROUP_CD) " +
			"VALUES (?,?,?,?,?) ",new SqlPrepare<Host>() {
		
		@Override
		public void invoke(PreparedStatement pstmt, Host input, Index idx)
				throws SQLException {
			pstmt.setString(idx.next(), input.hostName);
			pstmt.setString(idx.next(), input.state.name());
			pstmt.setString(idx.next(), input.ip);
			pstmt.setString(idx.next(), input.fqdn);
			pstmt.setString(idx.next(), input.group.name);
		}
	});
	public static Update<HostGroup> INSERT_HOST_GROUP = new Update<HostGroup>("INSERT INTO NN_HOST_GROUP (HOST_GROUP_CD,GROUP_DESCRIPTION) " +
			"VALUES (?,?) ",new SqlPrepare<HostGroup>() {

		@Override
		public void invoke(PreparedStatement pstmt, HostGroup input, Index idx)
				throws SQLException {
			pstmt.setString(idx.next(), input.name);
			pstmt.setString(idx.next(), input.description);
		}
	});
	
	@Override
	public void ensureHost(WorkerHelper wh) {
		String hostName = BeeQueueHome.instance.getHostName();
		List<Host> r = LOAD_HOST.query(connection(), hostName);
		if( r.size() == 1 ){
			wh.host = r.get(0);
			return;
		}
		wh.host = Host.localHost();
		wh.host.state = HostState.READY;
		List<HostGroup> groups = LOAD_HOST_GROUP.query(connection(), HostGroup.DEFAULT_GROUP);
		if( groups.size() == 1 ){
			wh.host.group = groups.get(0);
		}else if( groups.size() == 0 ){
			wh.host.group.name = HostGroup.DEFAULT_GROUP;
			if( 1 != INSERT_HOST_GROUP.update(connection(), wh.host.group)){
				TransactionContext.setRollbackOnly();
				throw new RuntimeException("Cannot save hostgroup.");
			}
		}else{
			TransactionContext.setRollbackOnly();
			throw new RuntimeException("NWIH");
		}
		if( 1 != INSERT_HOST.update(connection(), wh.host)){
			TransactionContext.setRollbackOnly();
			throw new RuntimeException("Cannot save host.");
		}
		
	}
}
