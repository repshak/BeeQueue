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

import java.io.IOException;

import org.beequeue.coordinator.db.DbCoordinator;
import org.beequeue.msg.BeeQueueStage;
import org.beequeue.sql.TransactionContext;
import org.beequeue.template.DomainTemplate;
import org.beequeue.template.JobTemplate;
import org.beequeue.template.EventTemplate;
import org.beequeue.template.StageTemplate;
import org.beequeue.util.ToStringUtil;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class StageFunctional {
	private static final String SEED_TREE = "test/seed";
	
	@Test
	public void test() 
			throws JsonParseException, JsonMappingException, IOException {
		DbCoordinator c = DbCoordinatorFunctional.getCoordinator();
		TransactionContext.push();
		c.processEmittedMessages();
		BeeQueueStage pickStageToRun = c.pickStageToRun();
		ToStringUtil.out(pickStageToRun);
		DomainTemplate domainTemplate = pickStageToRun.job.message.domainTemplate();
		EventTemplate messageTemplate = pickStageToRun.job.message.messageTemplate();
		JobTemplate jobTemplate = pickStageToRun.job.jobTemplate();
		StageTemplate stageTemplate = pickStageToRun.stageTemplate();
		messageTemplate.jobTemplate("report");
		ToStringUtil.out(domainTemplate);
		TransactionContext.pop();
		
	}


}
