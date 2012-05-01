package org.beequeue.agent;

import java.util.Map;

import org.beequeue.util.Creator;
import org.hyperic.sigar.ProcCpu;
import org.hyperic.sigar.ProcCredName;
import org.hyperic.sigar.ProcMem;
import org.hyperic.sigar.ProcState;
import org.hyperic.sigar.ProcUtil;

public class ProcRawData {
	public long pid ;
	public String name ;
	public String[] args ;
	public Map env ;
	public String cmd ;
	public ProcState state ;
	public ProcMem mem ;
	public ProcCpu cpu ;
	public ProcCredName username ;
	
	

	public ProcRawData() {
	}



	public ProcRawData(final long pid, final Agent agent) {
		this.pid = pid;
		this.args = Creator.IgnoreExceptions.create(new Creator<String[]>() { public String[] create() throws Exception { return agent.proxy.getProcArgs(pid);}});			
		this.env = Creator.IgnoreExceptions.create(new Creator<Map>() { public Map create() throws Exception { return agent.proxy.getProcEnv(pid);}});
		this.name = Creator.IgnoreExceptions.create(new Creator<String>() { public String create() throws Exception {return ProcUtil.getDescription(agent.sigar, pid);}}); 
		this.state = Creator.IgnoreExceptions.create(new Creator<ProcState>() { public ProcState create() throws Exception {return agent.sigar.getProcState(pid);}});
		this.mem = Creator.IgnoreExceptions.create(new Creator<ProcMem>() { public ProcMem create() throws Exception {return agent.sigar.getProcMem(pid);}});
		this.cpu = Creator.IgnoreExceptions.create(new Creator<ProcCpu>() { public ProcCpu create() throws Exception {return agent.sigar.getProcCpu(pid);}});
		this.username = Creator.IgnoreExceptions.create(new Creator<ProcCredName>() { public ProcCredName create() throws Exception {return agent.sigar.getProcCredName(pid);}});
	}
  }