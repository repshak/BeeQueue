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
package org.beequeue.agent;

import java.util.Map;

import org.beequeue.util.Creator;
import org.beequeue.util.Strings;
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
	
	public String allArgs(){
		return args != null && args.length > 0 ? Strings.join(" ", args) : name ; 
	}

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
