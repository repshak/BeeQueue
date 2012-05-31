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
package org.beequeue.coordinator;

import java.io.File;
import java.util.List;

import org.beequeue.hash.ContentTree;
import org.beequeue.hash.HashKey;
import org.beequeue.host.Host;
import org.beequeue.msg.BeeQueueDomain;
import org.beequeue.msg.BeeQueueMessage;
import org.beequeue.msg.BeeQueueMessageDrilldown;
import org.beequeue.msg.BeeQueueProcess;
import org.beequeue.msg.BeeQueueRun;
import org.beequeue.msg.BeeQueueStage;
import org.beequeue.worker.WorkerData;


public class ZooKeeperCoordinator implements Coordiantor {

	@Override
	public String selectAnyTable(String table) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String query(String q) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void ensureHost(WorkerData wh) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void storeStatistics(WorkerData wh) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void ensureWorker(WorkerData instance) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public HashKey push(File file) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void sweep() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pull(HashKey code, File destination) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ContentTree push(File source, String content) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ContentTree sync(ContentTree contentTree, File destination) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void ensureDomains(List<BeeQueueDomain> domains) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void storeMessage(BeeQueueMessage msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public BeeQueueMessageDrilldown checkMessage(long messageId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void processEmittedMessages() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public BeeQueueStage pickStageToRun() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void storeRun(BeeQueueRun run) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<BeeQueueRun> allCurrentRuns(WorkerData instance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<BeeQueueProcess> allActiveProcessesOnHost(Host host) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void storeProcess(BeeQueueProcess process) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean updateStage(BeeQueueStage stage) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void updateFinishedJobsAndMessages() {
		// TODO Auto-generated method stub
		
	}


}
