package org.beequeue;

import java.util.Map;

import org.beequeue.host.CloudConfig;


public class GlobalConfig {
	public static final String LOAD_FROM = "$BQ_CONFIG/global.json";
	
	public String defaultCloud;
	
	public Map<String, CloudConfig> clouds ;
	
	public String findCloudForHost(String hostName) {
		if(clouds!=null){
			for (String cloud : clouds.keySet()) {
				if(clouds.get(cloud).hasHost(hostName) ){
					return cloud;
				}
			}
		}
		return defaultCloud;
	}


	
}
