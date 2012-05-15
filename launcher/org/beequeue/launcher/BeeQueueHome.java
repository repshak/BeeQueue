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
package org.beequeue.launcher;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;

import org.beequeue.launcher.JarUnpacker.EntryFilter;


public class BeeQueueHome {
	public static final String BQ_HOME = "BQ_HOME";
	public static final String BQ_CONFIG = "BQ_CONFIG";
	public static final String BQ_WEB = "BQ_WEB";
	public static final String BQ_HOST = "BQ_HOST";
	public static final String BQ_WORKER_PID = "BQ_WORKER_PID";
	
	public static final BeeQueueHome instance = new BeeQueueHome();
	
	private File home;
	private File config;
	private File web;
	private File host;
	private long pid;
	private String hostName;
	
	private BeeQueueHome() {
		try{
			String homeEnv = System.getenv(BQ_HOME);
			if( homeEnv == null || homeEnv.trim().length()==0 ){
				home = new File( new File(".").getAbsoluteFile(), "bq-home" );
			}else{
				home = new File(homeEnv).getAbsoluteFile();
			}
			home = home.getCanonicalFile();
			if(!home.isDirectory() && !home.mkdirs()){
				throw new IOException("Cannot create:"+home);
			}
			this.config = new File( home, "config" );
			this.config.mkdirs();

			this.hostName = java.net.InetAddress.getLocalHost().getHostName();
			this.host = new File( home, "hosts/"+hostName );
			this.host.mkdirs();
			
			String webEnv = System.getenv(BQ_WEB);
			if( webEnv == null || webEnv.trim().length()==0 ){
				this.web = new File(home, "web");
				JarUnpacker.unpack(BeeQueueHome.class, new EntryFilter() {
					@Override
					public boolean include(ZipEntry ze) {
						return ze.getName().startsWith("web");
					}
				}, home);
			}else{
				this.web = new File(webEnv).getAbsoluteFile();
			}
		}catch (Exception e) {
			die(e, "Cannot establish home directory: " + home+" error:");
		}
		try{
			String[] split = ManagementFactory.getRuntimeMXBean().getName().split("@");
			this.pid = Long.parseLong(split[0]);
		}catch (Exception e) {
			die(e,"Cannot identify process id.\n error:" );
		}
		
	}

	public void die(Exception e, String msg) {
		System.err.println(msg );
		e.printStackTrace();
		System.exit(-1);
	}
	
	public String[] getEnv(Map<String, String> myenv){
		Map<String, String> envMap = new HashMap<String, String>(System.getenv());
		if(myenv!=null) envMap.putAll(myenv);
		envMap.putAll(getHomeVariables());
		List<String> envList = new ArrayList<String>();
		for (String k : envMap.keySet()) {
			envList.add(k+"="+envMap.get(k));
		}
		return envList.toArray(new String[0]);
	}
	
	public Map<String, String> getHomeVariables( ) {
		Map<String,String> envMap = new LinkedHashMap<String, String>();
		envMap.put(BQ_HOME,home.toString());
		envMap.put(BQ_WEB,web.toString());
		envMap.put(BQ_HOST,host.toString());
		envMap.put(BQ_CONFIG,config.toString());
		envMap.put(BQ_WORKER_PID,String.valueOf(pid));
		return envMap;
	}

	public String[] getCmd(String cmd, String runId){
		List<String> cmdList = new ArrayList<String>();
		String osName = System.getProperty("os.name").toLowerCase();
		if(osName.indexOf("win") >= 0){
			cmdList.add("cmd");
			cmdList.add("/c");
		}else{
			cmdList.add("/bin/bash");
			cmdList.add("-c");
		}
		File runDir = new File(host,"runs/"+runId);
		runDir.mkdirs();
		cmdList.add(cmd+" >"+runDir+"/log 2>"+ runDir+"/err" );
		return cmdList.toArray(new String[0]);
	}

	public File getHome() {
		return home;
	}

	public File getWeb() {
		return web;
	}

	public File getHost() {
		return host;
	}
	
	public File getConfig() {
		return config;
	}

	public String getHostName() {
		return hostName;
	}

	public long getPid() {
		return pid;
	}

}
