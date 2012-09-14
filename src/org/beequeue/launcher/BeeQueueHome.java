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
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;

import org.beequeue.buzz.BuzzServer;
import org.beequeue.launcher.JarUnpacker.EntryFilter;
import org.beequeue.worker.BeatLogic;


public class BeeQueueHome implements VariablesProvider{
	public static final String BQ_HOME = "BQ_HOME";
	public static final String BQ_CONFIG = "BQ_CONFIG";
	public static final String BQ_WEB = "BQ_WEB";
	public static final String BQ_HOST = "BQ_HOST";
	public static final String BQ_CLOUD = "BQ_CLOUD";
	public static final String BQ_WORKER_PID = "BQ_WORKER_PID";
	
	public static final BeeQueueHome instance = new BeeQueueHome();
	
	private File home;
	private File config;
	private File host;
	private long pid;
	private String cloud;
	private String hostName;
	private BuzzServer buzz;
	
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
			this.buzz = new BuzzServer(7532);
			String webEnv = System.getenv(BQ_WEB);
			if( webEnv == null || webEnv.trim().length()==0 ){
				this.buzz.setRoot(new File(home, "web"));
				JarUnpacker.unpack(BeeQueueHome.class, new EntryFilter() {
					@Override
					public boolean include(ZipEntry ze) {
						return ze.getName().startsWith("web");
					}
				}, home);
			}else{
				this.buzz.setRoot(new File(webEnv).getAbsoluteFile());
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
	
	public File jvmCsv(int port){
		return new File(host,"jvm."+port+".csv");
	}
	
	
	public static void die(Exception e, String msg) {
		System.err.println(msg );
		if(e!=null) e.printStackTrace();
		System.exit(-1);
	}
	
	
	public Map<String, String> getHomeVariables( ) {
		Map<String,String> envMap = new LinkedHashMap<String, String>();
		envMap.put(BQ_HOME,home.toString());
		envMap.put(BQ_WEB,getWeb().toString());
		envMap.put(BQ_HOST,host.toString());
		envMap.put(BQ_CONFIG,config.toString());
		envMap.put(BQ_WORKER_PID,String.valueOf(pid));
		return envMap;
	}


	public File getHome() {
		return home;
	}

	public File getWeb() {
		return getBuzz().getRoot();
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

	public BuzzServer getBuzz(){
		return buzz;
	}

	void setJavaLibraryPath(){

		System.setProperty( "java.library.path", new File(getWeb(),"WEB-INF/lib").getPath() );
		Field fieldSysPath;
		try {
			fieldSysPath = ClassLoader.class.getDeclaredField( "sys_paths" );
			fieldSysPath.setAccessible( true );
			fieldSysPath.set( null, null );
		} catch (Exception e) {
			System.err.println("cannot reset sys_paths");
		}
	}
	
	@Override
	public Map<String, ?> getVariables() {
		return getHomeVariables();
	}

	public void setCloudName(String cloud) {
		this.cloud = cloud;
		
	}
	
	private BeatLogic beatLogic = null ;
	private ScheduledExecutorService scheduler = null;

	public void runServer() {
		beatLogic = new BeatLogic(); 
		scheduler = Executors.newScheduledThreadPool(1);
		scheduler.scheduleAtFixedRate(beatLogic , 0, 15, TimeUnit.SECONDS);
		buzz.start();
	}

	public int getPort() {
		return buzz.getPort();
	}

}
