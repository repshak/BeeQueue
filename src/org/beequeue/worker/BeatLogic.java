package org.beequeue.worker;

import org.beequeue.GlobalConfig;
import org.beequeue.agent.Agent;
import org.beequeue.coordinator.Coordiantor;
import org.beequeue.hash.ContentTree;
import org.beequeue.launcher.BeeQueueHome;
import org.beequeue.launcher.BeeQueueJvmHelpeer;
import org.beequeue.launcher.BeeQueueJvmStatus;
import org.beequeue.sql.TransactionContext;

public class BeatLogic implements Runnable{
	Agent agent = new Agent();
	

	public void doLaundryList(Coordiantor coordinator) throws Exception{
		/* if configuration was updated and load all updates
		 */
		ContentTree sync = coordinator.sync(WorkerData.instance.config, BeeQueueHome.instance.getConfig());
		if(sync != null){
			WorkerData.instance.config = sync;
			Singletons.refreshAll(GlobalConfig.$BQ_CONFIG);
		}
		
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
		
		/* Process all messages and create stages, create runs for 
		 * unblocked stages. Pick runs to execute. Execute.
		 */

		/* Run ps. Identify all processes that currently executed.
		 * Check all processes that finished on host an update ther stages 
		 * appropriately
		 */
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
