package org.beequeue.host;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.beequeue.worker.HostState;
import org.beequeue.worker.WorkerState;


public class Host {
	public String hostName;
	public String fqdn;
	public String ip;
	public HostState state;
	public HostGroup group = new HostGroup();
	public HostStatistcs stat;
	
	public static Host localHost() {
		InetAddress inetAddress;
		try {
			inetAddress = InetAddress.getLocalHost();
			Host host = new Host();
			host.ip = inetAddress.getHostAddress();
			host.fqdn = inetAddress.getCanonicalHostName();
			host.hostName = inetAddress.getHostName();
			return host;
		} catch (UnknownHostException e) {
			throw new RuntimeException(e);
		}
	}
	public WorkerState toWorkerState(){
		return WorkerState.valueOf(state.name());
	}
}
