package org.beequeue;

import java.util.Map;

import org.beequeue.host.CloudConfig;
import org.beequeue.worker.WorkerConfig;


public class GlobalConfig {
	public static final String $BQ_CONFIG = "$BQ_CONFIG";
	public static final String LOAD_FROM = $BQ_CONFIG + "/global.json";
	
	public String defaultCloud;
	
	public Map<String, CloudConfig> clouds ;
	
	public WorkerConfig workerConfig = new WorkerConfig();
	
	public String findCloudForHost(String hostName) {
		if(clouds!=null){
			for (String cloud : clouds.keySet()) {
				if(Boolean.TRUE.equals(clouds.get(cloud).doesHostBelong(hostName)) ){
					return cloud;
				}
			}
		}
		return defaultCloud;
	}

	public CloudConfig cloudConfig(String cloudname) {
		if(this.clouds!=null){
			return this.clouds.get(cloudname);
		}
		return null;
	}


	
}
