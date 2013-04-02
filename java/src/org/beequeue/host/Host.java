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
	public Cloud cloud = new Cloud();
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
