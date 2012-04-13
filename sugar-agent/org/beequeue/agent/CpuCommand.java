package org.beequeue.agent;

import org.hyperic.sigar.CpuPerc;

public class CpuCommand {
	public static class CpuInfo {
		public CpuPerc total;
		public CpuPerc[] all;
	}
	
	public static int go(Agent agent) throws Exception {
		CpuInfo o = new CpuInfo();
		o.all = agent.sigar.getCpuPercList();
		o.total = agent.sigar.getCpuPerc();
		agent.dump("cpu", o);
		return 0;
	}

}
