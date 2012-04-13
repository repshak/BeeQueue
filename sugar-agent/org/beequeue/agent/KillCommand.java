package org.beequeue.agent;

import org.hyperic.sigar.SigarException;

public class KillCommand {
	
    public final static String SIGNAL = "SIGTERM";


	public static int go(Agent agent, String[] split) throws SigarException {
		for (int i = 0; i < split.length; i++) {
			long pid = Long.parseLong(split[i]);
			agent.sigar.kill(pid, SIGNAL);
		}
		return 0;
	}

}
