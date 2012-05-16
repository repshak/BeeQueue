package org.beequeue.host;

import org.beequeue.worker.Singletons;

public class Cloud {
	public String name;
	
	public CloudConfig config() {
		return Singletons.getGlobalConfig().cloudConfig(name);	}


	

}
