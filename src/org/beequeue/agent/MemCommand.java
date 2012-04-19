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