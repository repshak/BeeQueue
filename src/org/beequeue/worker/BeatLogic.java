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

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.beequeue.agent.Agent;
import org.beequeue.agent.ProcRawData;
import org.beequeue.coordinator.Coordiantor;
import org.beequeue.launcher.BeeQueueHome;
import org.beequeue.launcher.BeeQueueJvmHelpeer;
import org.beequeue.launcher.BeeQueueJvmStatus;
import org.beequeue.msg.BeeQueueDomain;
import org.beequeue.msg.BeeQueueProcess;
import org.beequeue.msg.BeeQueueRun;
import org.beequeue.msg.BeeQueueStage;
import org.beequeue.msg.RunState;
import org.beequeue.msg.StageState;
import org.beequeue.piles.LazyList;
import org.beequeue.run.RunHelper;
import org.beequeue.sql.TransactionContext;
import org.beequeue.template.DomainTemplate;
import org.beequeue.util.Files;
import org.beequeue.util.ToStringUtil;

public class BeatLogic implements Runnable{
	Agent agent = new Agent();
	

	public void doLaundryList(Coordiantor coordinator) throws Exception{
		/* if configuration was updated and load all updates
		 */
		WorkerData.instance.checkGlobalConfig(coordinator);
		
		/* Check if host object initialized and if not make sure that 
		 * host/group exists in db */
		coordinator.ensureHost(WorkerData.instance);
		
		/* Check CPU & Memory and up update 
		 * statistics */
		agent.runStatistics();
		coordinator.storeStatistics(WorkerData.instance);
		
		/* Check all active workers in the same group,
		 * calculate next beat time
		 * Update status of the workers and beet time 
		 * on the same machine if necessary.
		 */
		coordinator.ensureWorker(WorkerData.instance);
		
		Map<String, DomainTemplate> activeDomains = Singletons.getGlobalConfig().activeDomains();
		List<BeeQueueDomain> domains = LazyList.morph(BeeQueueDomain.TO_DOMAIN,  activeDomains.keySet());		
		coordinator.ensureDomains(domains);
		/* Process all messages and create stages, create runs for 
		 * unblocked stages. Pick runs to execute. Execute.
		 */
		coordinator.processEmittedMessages();
		
		BeeQueueStage readyToRun = coordinator.pickStageToRun();
		if(readyToRun!=null){
			RunHelper runHelper = new RunHelper(readyToRun,WorkerData.instance.worker);
			coordinator.storeRun(runHelper.run);
			runHelper.start();
			coordinator.storeRun(runHelper.run);
			coordinator.updateStage(runHelper.stage);
		}
		/* Run ps. Identify all processes that currently executed.
		 * Check all processes that finished on host an update ther stages 
		 * appropriately
		 */
		Map<Long,BeeQueueRun> notFinishedRuns = new LinkedHashMap<Long, BeeQueueRun>();
		for (BeeQueueRun run : coordinator.allCurrentRuns(WorkerData.instance)) {
			File rc = new File (run.dir, "rc.log");
			if(rc.exists()){
				int returnCode = Integer.parseInt(Files.readAll(rc).trim());
				run.state = returnCode == 0 ? RunState.SUCCESS : RunState.FAILURE ;
				coordinator.storeRun(run);
				run.stage.newState = run.state == RunState.SUCCESS ?   StageState.SUCCESS :   run.stage.retriesLeft > 0 ? StageState.READY : StageState.FAILURE;
				coordinator.updateStage(run.stage);
			}else{
				notFinishedRuns.put(run.id, run);
			}
			
		}
		ProcRawData[] ps = agent.runProcessStatistics();
		List<BeeQueueProcess> allActiveProcesses = coordinator.allActiveProcessesOnHost(WorkerData.instance.host);
		for (int i = 0; i < ps.length; i++) {
			ProcRawData procRawData = ps[i];
			if( procRawData.env != null && procRawData.env.get(RunHelper.BQ_RUN_ID) != null ){
				BeeQueueProcess process = new BeeQueueProcess();
				process.runId = Long.parseLong((String)procRawData.env.get(RunHelper.BQ_RUN_ID)); 
				process.pid = ""+procRawData.pid;
				process.ppid = ""+procRawData.state.getPpid();
				process.processName = procRawData.allArgs();
				coordinator.storeProcess(process);
				allActiveProcesses.remove(process);
				BeeQueueRun runThatStillHaveProcesses = notFinishedRuns.remove(process.runId);
				if(runThatStillHaveProcesses!=null){
					runThatStillHaveProcesses.justUpTimeStamp = true ;
					coordinator.storeRun(runThatStillHaveProcesses);
				}
			}
		}
		for (BeeQueueProcess process : allActiveProcesses) {
			process.down = true;
			coordinator.storeProcess(process);
		}
		if(notFinishedRuns.size()>0){
			ToStringUtil.out("Cannot detect any info about processes:\n",notFinishedRuns);
		}
		coordinator.updateFinishedJobsAndMessages();
	}

	@Override
	public void run() {
		checkShutDown();
		if( WorkerData.instance.nextBeat() ){
			try{
				TransactionContext.push();
				doLaundryList(Singletons.getCoordinator());
			}catch (Exception e) {
				e.printStackTrace();
			}finally{
				TransactionContext.pop();
			}
		}
		System.gc();
	}
	

	public void checkShutDown() {
		int port = BeeQueueHome.instance.getPort();
		BeeQueueJvmHelpeer jvmStatus = new BeeQueueJvmHelpeer(port);
		if( jvmStatus.starting != null && !jvmStatus.starting.isMe() ){
			jvmStatus.ensureMeInList(port, BeeQueueJvmStatus.TERMINATED);
			jvmStatus.write(port);
			System.out.println("Shutting down:"+BeeQueueHome.instance.getPid());
			System.exit(0);
		}else{
			jvmStatus.ensureMeInList(port, BeeQueueJvmStatus.LISTENING);
			jvmStatus.write(port);
		}
	}


}
