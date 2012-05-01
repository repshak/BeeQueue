package org.beequeue.worker;

import org.beequeue.host.Host;

public class WorkerHelper {
	
	public static WorkerHelper instance = new WorkerHelper();
	private WorkerHelper() {
	}
	
	public Host host = null;
	public Worker worker = null;
	
	

}
