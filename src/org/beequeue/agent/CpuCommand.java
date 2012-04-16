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

import org.hyperic.sigar.CpuInfo;
import org.hyperic.sigar.CpuPerc;

public class CpuCommand {
	public static class Cpu {
		public CpuInfo info;
		public CpuPerc total;
		public CpuPerc[] all;
	}
	
	public static int go(Agent agent) throws Exception {
		Cpu o = new Cpu();
		o.info = agent.sigar.getCpuInfoList()[0];
		o.all = agent.sigar.getCpuPercList();
		o.total = agent.sigar.getCpuPerc();
		agent.dump("cpu", o);
		return 0;
	}

}
