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

import java.util.List;

import org.beequeue.coordinator.db.DbCoordinator;
import org.beequeue.hash.HashStore;
import org.beequeue.host.Host;
import org.beequeue.msg.BeeQueueDomain;
import org.beequeue.msg.BeeQueueEvent;
import org.beequeue.msg.BeeQueueEventDrilldown;
import org.beequeue.msg.BeeQueueProcess;
import org.beequeue.msg.BeeQueueRun;
import org.beequeue.msg.BeeQueueStage;
import org.beequeue.worker.WorkerData;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

@JsonTypeInfo(use=Id.NAME, include=As.PROPERTY, property="type")
@JsonSubTypes({
    @JsonSubTypes.Type(value=DbCoordinator.class, name="db")
})
public interface Coordiantor extends HashStore {
	String LOAD_FROM = "$BQ_HOME/coordinator.json";

	String selectAnyTable(String table) ;

	String query(String q) ; 
	
	void ensureHost(WorkerData wh);
	
	void storeStatistics(WorkerData wh);

	void ensureWorker(WorkerData wh);

	void ensureDomains(List<BeeQueueDomain> domains);

	void storeMessage(BeeQueueEvent msg);

	BeeQueueEventDrilldown checkMessage(long messageId);

	void processEmittedMessages();

	BeeQueueStage pickStageToRun();

	void storeRun(BeeQueueRun run);

	List<BeeQueueRun> allCurrentRuns(WorkerData instance);

	List<BeeQueueProcess> allActiveProcessesOnHost(Host host);

	void storeProcess(BeeQueueProcess process);

	boolean updateStage(BeeQueueStage stage);

	void updateFinishedJobsAndMessages();

}
