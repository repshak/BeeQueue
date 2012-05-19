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
package org.beequeue.worker;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.beequeue.agent.CpuRawData;
import org.beequeue.agent.MemRawData;
import org.beequeue.agent.ProcRawData;
import org.beequeue.hash.ContentTree;
import org.beequeue.host.CapacityRatio;
import org.beequeue.host.Host;
import org.beequeue.host.HostStatistcs;

public class WorkerData {
	
	public static final String BEE_QUEUE_CONFIG = "BeeQueue.config";
	public static WorkerData instance = new WorkerData();
	private WorkerData() {
		config.name = BEE_QUEUE_CONFIG;
	}
	public Host host;
	public HostStatistcs hostStat ;
	public Worker worker ;
	public ContentTree config = new ContentTree();
	
	

	public void calculateNextBeat(List<Worker> allWorkersInTheGroup){
		long maxBeatTime = -1;
		Set<String> uniqueHosts = new HashSet<String>();
		for (Iterator<Worker> it = allWorkersInTheGroup.iterator(); it.hasNext();) {
			Worker worker = it.next();
			uniqueHosts.add(worker.hostName);
			maxBeatTime = Math.max(worker.nextBeat, maxBeatTime);
		}
		WorkerConfig config = Singletons.getGlobalConfig().workerConfig;
		long delta = Math.max(config.maxFireInterval/uniqueHosts.size(), config.minFireInterval) + config.callRandomizationTerm();
		this.worker.nextBeat = Math.max(maxBeatTime, System.currentTimeMillis()) + delta;
	}
	
	public void calcHostStatistics(CpuRawData cpuData, MemRawData memoryData) {
		CapacityRatio cpu = new CapacityRatio();
		cpu.max = cpuData.info.getTotalCores() ;
		cpu.value = cpu.max * cpuData.total.getCombined();
		CapacityRatio mem = new CapacityRatio();
		mem.max = memoryData.mem.getTotal();
		mem.value = memoryData.mem.getActualUsed();
		HostStatistcs hostStatistcs = new HostStatistcs();
		hostStatistcs.memory = mem;
		hostStatistcs.cpu = cpu;
		hostStat = hostStatistcs;
		
	}

	public void updateStatusOfProcesses(ProcRawData[] statusOfProcesses) {
		// TODO Auto-generated method stub
		
	}

	public void calculateNextStatus() {
		this.worker.state = HostStatistcs.HEALTHY_HOST_CHECK.doIt(this.hostStat) ?
			host.toWorkerState() : WorkerState.PAUSED ;	
		
	}

	public boolean nextBeat() {
		return worker == null || System.currentTimeMillis() >= worker.nextBeat;
	}

	
	
	

}
