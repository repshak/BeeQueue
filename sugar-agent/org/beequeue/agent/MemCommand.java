package org.beequeue.agent;

import org.hyperic.sigar.Mem;
import org.hyperic.sigar.Swap;

public class MemCommand {
	public static class MemInfo {
		public Mem mem;
		public Swap swap;
		
	}
	
	public static int go(Agent agent) throws Exception {
	    MemInfo memInfo = new MemInfo();
		memInfo.mem = agent.sigar.getMem();
		memInfo.swap = agent.sigar.getSwap();
		agent.dump("mem",memInfo);
		return 0;
	}

}
